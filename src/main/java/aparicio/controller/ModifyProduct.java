package aparicio.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.getAllParts;

/** This class lets a user modify the data from a Product.*/
public class ModifyProduct implements Initializable {


    public TextField invPartsSearch3;
    public TableView partsTable3;
    public TableColumn partIDCol3;
    public TableColumn partNameCol3;
    public TableColumn partInvLevelCol3;
    public TableColumn partPriceCostCol3;

    public TableView assocPrtTable2;
    public TableColumn assocPrtIdCol2;
    public TableColumn assocPrtNameCol2;
    public TableColumn assocPrtInvCol2;
    public TableColumn assocPrtPriceCol2;

    public TextField modPdId;
    public TextField modPdName;
    public TextField modPdInv;
    public TextField modPdPrice;
    public TextField modPdMax;
    public TextField modPdMin;

    private static Product selProd;
    private static int selPdIndex;

    private static ObservableList<Part> modifiedPartList = FXCollections.observableArrayList();

    /** This method receives the data from the Main screen.
     * The product selected in the Main screen is sent to the fields in this method.*/
    public static void passSelPd(int SPdIndex, Product selPd) {
        selPdIndex = SPdIndex;
        selProd = selPd;
    }

    /** This method initializes the fields and top tableView in the Modify Product screen.
     * The fields are initialized with the selected product from the Main screen.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        modPdId.setText(Integer.toString(selProd.getId()));
        modPdName.setText(selProd.getName());
        modPdInv.setText(Integer.toString(selProd.getStock()));
        modPdPrice.setText(Double.toString(selProd.getPrice()));
        modPdMax.setText(Integer.toString(selProd.getMax()));
        modPdMin.setText(Integer.toString(selProd.getMin()));

        partIDCol3.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol3.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelCol3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostCol3.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable3.setItems(getAllParts());

        assocPrtIdCol2.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPrtNameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPrtInvCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPrtPriceCol2.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifiedPartList = selProd.getAllAssociatedParts();
        assocPrtTable2.setItems(modifiedPartList);
    }

    /** This method searches the Parts list. It calls the lookUpPart method from Inventory Model class,
     * returns a list of matches and searches by Part ID and Name.*/
    public void onInvPartsSearch3(ActionEvent actionEvent) {
        String input2 = invPartsSearch3.getText();
        ObservableList<Part> returnedList = Inventory.lookUpPart(input2);
        partsTable3.setItems(returnedList);

        try {
            if (returnedList.size() == 0) {
                int inputNum = Integer.parseInt(input2);
                Part returnedPart = Inventory.lookUpPart(inputNum);
                if (returnedPart != null) {
                    ObservableList<Part> searchList2 = FXCollections.observableArrayList();
                    searchList2.add(returnedPart);
                    partsTable3.setItems(searchList2);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Part could not be found.");
                    alert.showAndWait();
                }
            }
        }

        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Part could not be found.");
            alert.showAndWait();
        }
    }

    /** This method adds a part to the associated parts list.
     * It copies a part from the top tableView and adds it to the bottom tableView.
     * It also has an Alert message to make sure a part is selected.*/
    public void onAddAssocPt2(ActionEvent actionEvent) {
        Part SPtAdd = (Part) partsTable3.getSelectionModel().getSelectedItem();

        if(SPtAdd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a Part to add.");
            alert.showAndWait();
        }
        else {
            modifiedPartList.add(SPtAdd);
            assocPrtTable2.setItems(modifiedPartList);
        }
    }

    /** This method removes a part from the associated parts list.
     * It permanently deletes the selected part from the associated parts list
     * and displays an Alert message to confirm deletion.*/
    public void onRemoveAssocPrt2(ActionEvent actionEvent) {

        Part SPtRemove = (Part) assocPrtTable2.getSelectionModel().getSelectedItem();

        if (SPtRemove == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a Part to remove.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will delete the selected part " +
                    "associated to the product on the left, do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                modifiedPartList.remove(SPtRemove);
            }
        }
    }

    /** This method saves a modified product to the all products list.
     * It saves the data from the text fields, creates a Product object,
     * links the associated parts list to this object, and redirects to the main screen.*/
    public void onSaveMod2(ActionEvent actionEvent) throws IOException{

        String error = "";

        try
        {
            String idNum = modPdId.getText();
            int idNum1 = Integer.parseInt(idNum);

            String name = modPdName.getText();

            error = "Price";
            String priceCst = modPdPrice.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            error = "Inv";
            String invNum = modPdInv.getText();
            int invNum1 = Integer.parseInt(invNum);

            error = "Min";
            String mn = modPdMin.getText();
            int mn1 = Integer.parseInt(mn);

            error = "Max";
            String mx = modPdMax.getText();
            int mx1 = Integer.parseInt(mx);

            if (name.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Name can not be blank");
                alert.showAndWait();;
            }
            else if (mn1 > mx1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Max must be more than Min.");
                alert.showAndWait();
            }
            else if (invNum1 < mn1 || invNum1 > mx1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Inv must be between Min and Max.");
                alert.showAndWait();
            }
            else {
                Product addProd = new Product(idNum1, name, priceCst1, invNum1, mn1, mx1);
                for (Part p : modifiedPartList) {
                    addProd.addAssociatedPart(p);
                }
                Inventory.updateProduct(selPdIndex, addProd);

                Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 900, 600);
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText(error + " must be a number.");
            alert.showAndWait();
        }
    }

    /** This method is called when the Cancel button is clicked. It cancels the
     * modify product request and redirects to the main screen.*/
    public void backToMainAgain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }
}

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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.getAllParts;
import static model.Inventory.getAllProducts;

/** This class adds a product with an associated parts list to the allProducts list.*/
public class AddProduct implements Initializable {

    public TextField prodName;
    public TextField prodInv;
    public TextField prodPriceCost;
    public TextField prodMax;
    public TextField prodMin;

    public TableView partsTable2;
    public TableColumn partIDCol2;
    public TableColumn partNameCol2;
    public TableColumn partInvLevelCol2;
    public TableColumn partPriceCostCol2;
    public TextField invPartsSearch2;

    public TableView assocPrtTable;
    public TableColumn assocPrtIdCol;
    public TableColumn assocPrtNameCol;
    public TableColumn assocPrtInvCol;
    public TableColumn assocPrtPriceCol;

    private ObservableList<Part> addedAssocPartList = FXCollections.observableArrayList();

    /** This method initializes and sets data to the top tableView.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partIDCol2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostCol2.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable2.setItems(getAllParts());

        assocPrtIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPrtNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPrtInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPrtPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** This method searches the Parts list. It calls the lookUpPart method from Inventory Model class,
     * returns a list of matches and searches by Part ID and Name.*/
    public void onInvPartsSearch2(ActionEvent actionEvent) {
        String input2 = invPartsSearch2.getText();
        ObservableList<Part> returnedList = Inventory.lookUpPart(input2);
        partsTable2.setItems(returnedList);

        try {
            if (returnedList.size() == 0) {
                int inputNum = Integer.parseInt(input2);
                Part returnedPart = Inventory.lookUpPart(inputNum);
                if (returnedPart != null) {
                    ObservableList<Part> searchList2 = FXCollections.observableArrayList();
                    searchList2.add(returnedPart);
                    partsTable2.setItems(searchList2);
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
    public void onAddAssocPt(ActionEvent actionEvent) {

        Part SPtAdd = (Part) partsTable2.getSelectionModel().getSelectedItem();

        if(SPtAdd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a Part fo add.");
            alert.showAndWait();
        }
        else {
            addedAssocPartList.add(SPtAdd);
            assocPrtTable.setItems(addedAssocPartList);
        }
    }


    /** This method removes a part from the associated parts list.
     * It permanently deletes the selected part from the associated parts list
     * and displays an Alert message to confirm deletion.*/
    public void onRemoveAssocPrt(ActionEvent actionEvent) {

        Part SPtRemove = (Part) assocPrtTable.getSelectionModel().getSelectedItem();

        if (SPtRemove == null) {
            return;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will delete the selected part associated to the product on the left, do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                addedAssocPartList.remove(SPtRemove);
            }
        }
    }

    /** This method adds and saves a product to the all products list.
     * It saves the data from the text fields, creates a Product object,
     * links the associated parts list to this object, and redirects to the main screen.*/
    public void onSaveProd(ActionEvent actionEvent) throws IOException{

        String error = "";

        try {

            ObservableList<Product> currentList = FXCollections.observableArrayList();
            currentList = Inventory.getAllProducts();
            int ID = currentList.size() + 1;

            String name = prodName.getText();

            error = "Price";
            String priceCst = prodPriceCost.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            error = "Inv";
            String invNum = prodInv.getText();
            int invNum1 = Integer.parseInt(invNum);

            error = "Min";
            String mn = prodMin.getText();
            int mn1 = Integer.parseInt(mn);

            error = "Max";
            String mx = prodMax.getText();
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
                Product addProd = new Product(ID, name, priceCst1, invNum1, mn1, mx1);
                for (Part p : addedAssocPartList) {
                    addProd.addAssociatedPart(p);
                }
                Inventory.addProduct(addProd);

                Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 900, 600);
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText(error + " must be a number.");
            alert.showAndWait();
        }
    }

    /** This method is called when the Cancel button is clicked. It cancels the
     * add product request and redirects to the main screen.*/
    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }
}

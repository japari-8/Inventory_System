package aparicio.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static model.Inventory.getAllParts;
import static model.Inventory.getAllProducts;

/** This class adds an Outsourced part to the allParts list.*/
public class AddPartOutsourced implements Initializable {

    public TextField ptNameOutS;
    public TextField invOutS;
    public TextField priceCostOutS;
    public TextField maxOutS;
    public TextField minOutS;
    public TextField coName;
    public Button partSaveOutS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /** This method is called when the Cancel button is clicked. It cancels the
     * add part request and redirects to the main screen.*/
    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    /** This method is called when the In-House radio button is clicked.
     * It redirects to the In-House Add Part screen.*/
    public void backToInHouse(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/AddPartInHouse.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Add Part Form");
        stage.setScene(scene);
        stage.show();
    }

    /** This method is called when the Save button is clicked.
     * It saves the part data, has validation checks for Min, Max, and empty fields.
     * Calls addPart method from Inventory class to add to allParts list and redirects to main screen.*/
    public void onPartSaveOutS(ActionEvent actionEvent) throws IOException{

        String error = "";

        try
        {
            ObservableList<Part> currentList = FXCollections.observableArrayList();
            currentList = Inventory.getAllParts();
            int ID = currentList.size() + 1;
            System.out.println(ID);

            String name = ptNameOutS.getText();

            error = "Inv";
            String invNum = invOutS.getText();
            int invNum1 = Integer.parseInt(invNum);

            error = "Price/Cost";
            String priceCst = priceCostOutS.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            error = "Min";
            String mn = minOutS.getText();
            int mn1 = Integer.parseInt(mn);

            error = "Max";
            String mx = maxOutS.getText();
            int mx1 = Integer.parseInt(mx);

            String comName = coName.getText();

            if (name.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Name can not be blank");
                alert.showAndWait();;
            }
            else if (comName.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Company Name can not be blank");
                alert.showAndWait();
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

                OutsourcedPart addPt = new OutsourcedPart(ID, name, priceCst1, invNum1, mn1, mx1, comName);
                Inventory.addPart(addPt);


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

}

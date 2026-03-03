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
import model.InHousePart;
import model.Inventory;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.addPart;
import static model.Inventory.getAllParts;

/** This class adds an In-House part to the allParts list.*/
public class AddPartInHouse implements Initializable {

    public TextField ptName;
    public TextField inv;
    public TextField priceCost;
    public TextField max;
    public TextField min;
    public TextField machineId;
    public Button partSave;

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

    /** This method is called when the Outsourced radio button is clicked.
     * It redirects to the Outsourced Add Part screen.*/
    public void addPartOutsourced(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/AddPartOutsourced.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Add Part Form");
        stage.setScene(scene);
        stage.show();
    }

    /** This method is called when the Save button is clicked.
     * It saves the part data, has validation checks for Min, Max, and empty fields.
     * Calls addPart method from Inventory class to add to allParts list and redirects to main screen.
     * RUNTIME ERROR: A runtime error occurs when this button is clicked and the user does not enter data
     * in one or more of the fields. This was fixed by adding a try/catch block.*/
    public void onPartSave(ActionEvent actionEvent) throws IOException {

        String error = "";

        try
        {
            ObservableList<Part> currentList = FXCollections.observableArrayList();
            currentList = getAllParts();
            int ID = currentList.size() + 1;

            String name = ptName.getText();

            error = "Price/Cost";
            String priceCst = priceCost.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            error = "Inv";
            String invNum = inv.getText();
            int invNum1 = Integer.parseInt(invNum);

            error = "Min";
            String mn = min.getText();
            int mn1 = Integer.parseInt(mn);

            error = "Max";
            String mx = max.getText();
            int mx1 = Integer.parseInt(mx);

            error = "Machine ID";
            String machId = machineId.getText();
            int machId1 = Integer.parseInt(machId);

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
                InHousePart addPt = new InHousePart(ID, name, priceCst1, invNum1, mn1, mx1, machId1);
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
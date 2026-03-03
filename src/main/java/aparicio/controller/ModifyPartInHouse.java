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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.ResourceBundle;

import static model.Inventory.getAllParts;

/** This class lets a user modify the data from an In-House part.*/
public class ModifyPartInHouse implements Initializable {

    public TextField modId;
    public TextField modName;
    public TextField modInv;
    public TextField modPriceCost;
    public TextField modMax;
    public TextField modMin;
    public TextField modMachId;
    private static InHousePart selPart = null;
    private static int selPtIndex;

    /** This method receives the data from the Main screen.
     * The part selected in the Main screen is sent to the fields in this method.*/
    public static void passSelPt(int SPIndex, InHousePart selPt) {
        selPart = selPt;
        selPtIndex = SPIndex;
    }

    /** This method receives the data from the Outsourced Modify Part screen.
     * The part selected in the Outsourced Modify Part screen is sent to the fields in this method.*/
    public static void modOutSToInHouse(int selPrtIndexInHouse, InHousePart modToInHouse){
        selPart = modToInHouse;
        selPtIndex = selPrtIndexInHouse;
    }

    /** This method initializes the fields in the In-House Modify Part screen.
     * The fields are initialized with the selected part from the Main screen or Outsourced Modify Part screen.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        modId.setText(Integer.toString(selPart.getId()));
        modName.setText(selPart.getName());
        modInv.setText(Integer.toString(selPart.getStock()));
        modPriceCost.setText(Double.toString(selPart.getPrice()));
        modMax.setText(Integer.toString(selPart.getMax()));
        modMin.setText(Integer.toString(selPart.getMin()));
        modMachId.setText(Integer.toString(selPart.getMachineId()));

    }

    /** This method is called when the Cancel button is clicked. It cancels the
     * modify In-House part request and redirects to the main screen.*/
    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    /** This method allows the user to switch to the Outsourced Modify Part screen.
     * It takes the In-House part, converts it to an Outsourced part and then redirects
     * to Outsourced Modify Part screen. It also has a validation check for empty fields.*/
    public void modifyPartOutsourced(ActionEvent actionEvent) throws IOException {

        try {

            String idNum = modId.getText();
            int idNum1 = Integer.parseInt(idNum);

            String name = modName.getText();

            String priceCst = modPriceCost.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            String invNum = modInv.getText();
            int invNum1 = Integer.parseInt(invNum);

            String mn = modMin.getText();
            int mn1 = Integer.parseInt(mn);

            String mx = modMax.getText();
            int mx1 = Integer.parseInt(mx);

            String machId = modMachId.getText();


            OutsourcedPart modPtOutS = new OutsourcedPart(idNum1, name, priceCst1, invNum1, mn1, mx1, machId);
            ModifyPartOutsourced.modInHouseToOutS(selPtIndex, modPtOutS);

            Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/ModifyPartOutsourced.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setTitle("Modify Part Form");
            stage.setScene(scene);
            stage.show();
        }

        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each field.");
            alert.showAndWait();
        }
    }

    /** This method saves the Modified part. It updates the modified part,
     * validates the data entered, and redirects to the Main screen.*/
    public void onSaveModPart(ActionEvent actionEvent) throws IOException {

        String error = "";

        try
        {
            String idNum = modId.getText();
            int idNum1 = Integer.parseInt(idNum);

            String name = modName.getText();

            error = "Price/Cost";
            String priceCst = modPriceCost.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            error = "Inv";
            String invNum = modInv.getText();
            int invNum1 = Integer.parseInt(invNum);

            error = "Min";
            String mn = modMin.getText();
            int mn1 = Integer.parseInt(mn);

            error = "Max";
            String mx = modMax.getText();
            int mx1 = Integer.parseInt(mx);

            error = "Machine ID";
            String machId = modMachId.getText();
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
                InHousePart modPt = new InHousePart(idNum1, name, priceCst1, invNum1, mn1, mx1, machId1);
                Inventory.updatePart(selPtIndex, modPt);

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
}

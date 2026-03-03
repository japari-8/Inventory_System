package aparicio.controller;

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

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** This class lets a user modify the data from an Outsourced part.*/
public class ModifyPartOutsourced implements Initializable {

    public TextField modIdOutS;
    public TextField modNameOutS;
    public TextField modInvOutS;
    public TextField modPriceCostOutS;
    public TextField modMaxOutS;
    public TextField modMinOutS;
    public TextField modCoNameOutS;
    private static OutsourcedPart selPartOutS = null;
    private static int selPtIndexOutS;

    /** This method receives the data from the Main screen.
     * The part selected in the Main screen is sent to the fields in this method.*/
    public static void passSelPtOutS(int SPIndex, OutsourcedPart selPt) {
        selPartOutS = selPt;
        selPtIndexOutS = SPIndex;
    }

    /** This method receives the data from the In-House Modify Part screen.
     * The part selected in the In-House Modify Part screen is sent to the fields in this method.*/
    public static void modInHouseToOutS(int selPrtIndexOutS, OutsourcedPart modToOutS) {
        selPartOutS = modToOutS;
        selPtIndexOutS = selPrtIndexOutS;
    }

    /** This method initializes the fields in the Outsourced Modify Part screen.
     * The fields are initialized with the selected part from the Main screen or In-House Modify Part screen.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        modIdOutS.setText(Integer.toString(selPartOutS.getId()));
        modNameOutS.setText(selPartOutS.getName());
        modInvOutS.setText(Integer.toString(selPartOutS.getStock()));
        modPriceCostOutS.setText(Double.toString(selPartOutS.getPrice()));
        modMaxOutS.setText(Integer.toString(selPartOutS.getMax()));
        modMinOutS.setText(Integer.toString(selPartOutS.getMin()));
        modCoNameOutS.setText(selPartOutS.getCompanyName());
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


    /** This method allows the user to switch to the In-House Modify Part screen.
     * It takes the Outsourced part, converts it to an In-House part and then redirects
     * to the In-House Modify Part screen. It also has a validation check for empty fields.*/
    public void backToModifyPartInHouse(ActionEvent actionEvent) throws IOException {

        try {

            String idNum = modIdOutS.getText();
            int idNum1 = Integer.parseInt(idNum);

            String name = modNameOutS.getText();

            String priceCst = modPriceCostOutS.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            String invNum = modInvOutS.getText();
            int invNum1 = Integer.parseInt(invNum);

            String mn = modMinOutS.getText();
            int mn1 = Integer.parseInt(mn);

            String mx = modMaxOutS.getText();
            int mx1 = Integer.parseInt(mx);

            int machID1 = 0;

            InHousePart modPtInHouse = new InHousePart(idNum1, name, priceCst1, invNum1, mn1, mx1, machID1);
            ModifyPartInHouse.modOutSToInHouse(selPtIndexOutS, modPtInHouse);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/aparicio/view/ModifyPartInHouse.fxml")));
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
    public void onSaveModPtOutS(ActionEvent actionEvent) throws IOException {

        String error = "";

        try {

            String idNum = modIdOutS.getText();
            int idNum1 = Integer.parseInt(idNum);

            String name = modNameOutS.getText();

            error = "Price/Cost";
            String priceCst = modPriceCostOutS.getText();
            double priceCst1 = Double.parseDouble(priceCst);

            error = "Inv";
            String invNum = modInvOutS.getText();
            int invNum1 = Integer.parseInt(invNum);

            error = "Min";
            String mn = modMinOutS.getText();
            int mn1 = Integer.parseInt(mn);

            error = "Max";
            String mx = modMaxOutS.getText();
            int mx1 = Integer.parseInt(mx);

            String comName = modCoNameOutS.getText();
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
                OutsourcedPart modPt = new OutsourcedPart(idNum1, name, priceCst1, invNum1, mn1, mx1, comName);
                Inventory.updatePart(selPtIndexOutS, modPt);

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

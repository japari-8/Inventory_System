package aparicio.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;


/** This class creates the main screen for the Inventory Management System.
 * It contains all features and buttons and redirects to new screens.*/
public class MainController implements Initializable {

    // Imported fx:id's from Parts
    public TableView partsTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInvLevelCol;
    public TableColumn partPriceCostCol;
    public Button deletePart;
    public TextField invPartsSearch;

    // Imported fx:id's from Products
    public TableView productsTable;
    public TableColumn productIDCol;
    public TableColumn productNameCol;
    public TableColumn productInvLevelCol;
    public TableColumn productPriceCostCol;
    public Button deleteProduct;
    public TextField invProductsSearch;

    private static boolean firstTime = true;

    /** This method creates test data for Parts and Products.
     * This data is populated to simplify testing for Search, Modify, and Delete functions.*/
    private void addTestData() {
        if (!firstTime) {
            return;
        }
        firstTime = false;

        InHousePart testPt1 = new InHousePart(1, "Brakes", 15.00, 10, 1, 20, 111);
        Inventory.addPart(testPt1);
        InHousePart testPt2 = new InHousePart(2, "Handle", 17.00, 10, 1, 20, 112);
        Inventory.addPart(testPt2);
        OutsourcedPart testPt3 = new OutsourcedPart(3, "Wheel", 22.00, 10, 1, 20, "ABC Wheels");
        Inventory.addPart(testPt3);

        Product testPd1 = new Product(1, "Bicycle", 120.00, 10, 1, 20);
        Inventory.addProduct(testPd1);
        Product testPd2 = new Product(2, "Tricycle", 100.00, 10, 1, 20);
        testPd2.addAssociatedPart(testPt2);
        Inventory.addProduct(testPd2);
    }

    /** This method calls the addTestData method. It initializes and sets test data to the main screen tableViews.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");

        addTestData();

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(getAllParts());
        productsTable.setItems(getAllProducts());
    }

    /** This method is called when the Add button for Parts is clicked. This method opens up the Add Parts screen.*/
    public void onAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/AddPartInHouse.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Add Part Form");
        stage.setScene(scene);
        stage.show();
    }

    /** This method is called when the Modify button for Parts is clicked.
     * This method reads the part selected and opens up the Modify Parts screen with the part selected data populated.
     * It also checks to see if the part selected is In-House or Outsourced and redirects to appropriate screen.*/
    public void onModifyPart(ActionEvent actionEvent) throws IOException {

        try
        {
            Part SPt = (Part) partsTable.getSelectionModel().getSelectedItem();
            int SPIndex = getAllParts().indexOf(SPt);

            if (SPt instanceof InHousePart) {
                ModifyPartInHouse.passSelPt(SPIndex, (InHousePart) SPt);

                Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/ModifyPartInHouse.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 900, 600);
                stage.setTitle("Modify Part Form");
                stage.setScene(scene);
                stage.show();
            }
            else {
                ModifyPartOutsourced.passSelPtOutS(SPIndex, (OutsourcedPart) SPt);

                Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/ModifyPartOutsourced.fxml"));
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 900, 600);
                stage.setTitle("Modify Part Form");
                stage.setScene(scene);
                stage.show();
            }
        }

        catch(LoadException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to modify.");
            alert.showAndWait();
        }

    }

    /** This method is called when the Parts Delete button is clicked.
     * This method deletes the part that is selected. If no selection is made, will display Alert message.*/
    public void onDeletePart(ActionEvent actionEvent) {

        Part SP = (Part) partsTable.getSelectionModel().getSelectedItem();

        if (SP == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a Part to remove.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will permanently delete part selected from Inventory, do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(SP);
            }
        }
    }

    /** This method searches the Parts list. It calls the lookUpPart method from Inventory Model class,
     * returns a list of matches and searches by Part ID and Name.
     * RUNTIME ERROR: A runtime error occurs when the user enters text that does not match a part from the
     * parts list into the Search text field. This was fixed by adding a try/catch block.*/
    public void onInvPartsSearch(ActionEvent actionEvent) {
        String input = invPartsSearch.getText();
        ObservableList<Part> returnedList = Inventory.lookUpPart(input);
        partsTable.setItems(returnedList);

        try
        {
            if (returnedList.size() == 0) {
                int inputNum = Integer.parseInt(input);
                Part returnedPart = Inventory.lookUpPart(inputNum);
                if (returnedPart != null) {
                    ObservableList<Part> searchList2 = FXCollections.observableArrayList();
                    searchList2.add(returnedPart);
                    partsTable.setItems(searchList2);
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

    /** This method is called when the Add button for Products is clicked. This method opens up the Add Products screen.*/
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Add Product Form");
        stage.setScene(scene);
        stage.show();
    }

    /** This method is called when the Modify button for Products is clicked.
     * This method reads the Product selected and opens up the Modify Products screen with the Product selected data populated.*/
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {

        try {
            Product SPdT = (Product) productsTable.getSelectionModel().getSelectedItem();
            int SPIndex = getAllProducts().indexOf(SPdT);
            ModifyProduct.passSelPd(SPIndex, SPdT);

            Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/ModifyProduct.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setTitle("Modify Product Form");
            stage.setScene(scene);
            stage.show();
        }

        catch (LoadException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a product to modify.");
            alert.showAndWait();
        }
    }

    /** This method searches the Products list. It calls the lookUpProduct method from Inventory Model class,
     * returns a list of matches and searches by Product ID and Name.
     * RUNTIME ERROR: A runtime error occurs when the user enters text that does not match a product from the
     * products list into the Search text field. This was fixed by adding a try/catch block.*/
    public void onInvProductsSearch(ActionEvent actionEvent) {
        String input = invProductsSearch.getText();
        ObservableList<Product> returnedList = Inventory.lookUpProduct(input);
        productsTable.setItems(returnedList);

        try {
            if (returnedList.size() == 0) {
                int inputNum = Integer.parseInt(input);
                Product returnedProduct = Inventory.lookUpProduct(inputNum);
                if (returnedProduct != null) {
                    ObservableList<Product> searchList2 = FXCollections.observableArrayList();
                    searchList2.add(returnedProduct);
                    productsTable.setItems(searchList2);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Product could not be found.");
                    alert.showAndWait();
                }
            }
        }

        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Product could not be found.");
            alert.showAndWait();
        }

    }

    /** This method is called when the Products Delete button is clicked.
     * This method deletes the product that is selected. If no selection is made, will display Alert message.*/
    public void onDeleteProduct(ActionEvent actionEvent) {
        Product SPd = (Product) productsTable.getSelectionModel().getSelectedItem();

        if (SPd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a Product to remove.");
            alert.showAndWait();
        }
        else if (!SPd.getAllAssociatedParts().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText("This product has parts.");
        alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This action will permanently delete Product selected from Inventory, do you want to continue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(SPd);
            }
        }

    }

    /** This method is called when the Exit button is clicked. This method closes the program.*/
    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
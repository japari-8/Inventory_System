package aparicio.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** This class creates an app for an Inventory Management System.
 * FUTURE ENHANCEMENTS: Add columns in the TableViews so all data
 * from parts and products can be seen.*/
public class Main extends Application {

    /** This is the first method that is called. This method loads the main screen. */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/aparicio/view/Main.fxml"));
        stage.setTitle("Main Form");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    /** This is the main method. This method launches the main screen.
     * Javadoc folder included in FirstScreen folder.*/
    public static void main(String[] args) {

        launch();
    }
}
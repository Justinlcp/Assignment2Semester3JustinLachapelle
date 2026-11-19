package com.mycompany.assignement2semester3justinlachapelle;

import com.mycompany.assignement2semester3justinlachapelle.controller.CarRaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Where the program starts. It loads the FXML (the view), and JavaFX makes
 * the controller, which makes the model.
 *
 * @author Justin Lachapelle
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CarRaceFXML.fxml"));
        Parent root = loader.load();
        CarRaceController controller = loader.getController();

        stage.setTitle("CarRace simulation");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        controller.startSlideshow();
    }

    /**
     * Starts the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
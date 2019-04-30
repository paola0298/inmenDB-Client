package GUI;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application {

    public static void main(String[] args) {

        String stringPrueba;
        stringPrueba=JOptionPane.showInputDialog(null,"inserte json");
        JSON prueba = new JSON();
        prueba.recive_data(stringPrueba);
        launch(args);

    }

    @Override
    public void start(Stage primaryStage)throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("NewRegister.fxml"));
        primaryStage.setTitle("Create New Register");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }
}



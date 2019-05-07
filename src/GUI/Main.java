package GUI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.TableView;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);

        System.out.println("termine");

    }

    @Override
    public void start(Stage primaryStage)throws Exception {

        String stringPrueba;
        stringPrueba=JOptionPane.showInputDialog(null,"inserte json");
        JSON prueba = new JSON();
        NewData window= new NewData();
        window.start(primaryStage, prueba.recive_data(stringPrueba).getAttr_name());

    }
}



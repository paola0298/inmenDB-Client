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

        String stringPrueba;
        stringPrueba=JOptionPane.showInputDialog(null,"inserte json");
        JSON prueba = new JSON();
        prueba.recive_data(stringPrueba);

        launch(args);

        System.out.println("termine");

    }

    @Override
    public void start(Stage primaryStage)throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("NewRegister.fxml").openStream());

        NewRegister NewRegisterWindow = (NewRegister)fxmlLoader.getController();
        String[] dynamicLabelArray = {"Name","Last Name","ID Number"};
        NewRegisterWindow.setInfoEnviar(dynamicLabelArray);
        primaryStage.setTitle("Create New Register");
        primaryStage.setScene(new Scene(root, 430, 360));
        primaryStage.show();

    }
}



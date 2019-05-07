package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;


public class NewData {

    public void start(Stage stage,String []arrayLabel ) {
        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        int posX = 0;
        int posY = 0;
        for(int i=0; arrayLabel.length > i; i++){
            System.out.println(arrayLabel[i]);
            Label label1 = new Label();
            label1.setText("Inserte más " + arrayLabel[i]);
            label1.setAlignment(Pos.CENTER);
            grid.add(label1,0,posX);
            TextField textFieldLabel1 = new TextField();
            textFieldLabel1.setText("Ingrese el dato");
            grid.add(textFieldLabel1,1,posX);
            posX++;
        }
//        VBox vBox = new VBox();

        stage.setMaxHeight(430);
        stage.setMaxWidth(380);
        stage.setTitle("Ingresar nuevo dato");
//        root.getChildren().add(vBox);
        root.getChildren().add(grid);

        Scene windowNewData = new Scene(root, 430, 380);
        stage.setScene(windowNewData);
        stage.show();




    }
}
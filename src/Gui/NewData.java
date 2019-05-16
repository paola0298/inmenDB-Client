package Gui;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class NewData extends Application {
    private JSONArray arrayJsonArray;
    private JSONObject scheme;
    private JSONObject newRegister;

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(5);
        grid.setHgap(5);

//        grid.setBackground(new Background(new BackgroundFill(Color.web("#222831"), CornerRadii.EMPTY, Insets.EMPTY)));

        int posX = 0;



        for(int i=0; arrayLabel.length > i; i++){
//            System.out.println(arrayLabel[i]);
            Label attribute = new Label();
            attribute.setText(arrayLabel[i]);
            attribute.setAlignment(Pos.CENTER);
//            attribute.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
//            attribute.setTextFill(Color.web("#00adb5"));
            grid.add(attribute,0,posX);
            TextField textFieldattribute = new TextField();
//            textFieldattribute.setText("Ingrese el dato");
//            textFieldattribute.setBackground(new Background(new BackgroundFill(Color.web("#222831"),CornerRadii.EMPTY, Insets.EMPTY)));
//            textFieldattribute.setBorder(new Border(new BorderStroke(Color.web("#00adb5"),
//                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//            textFieldattribute.setStyle("-fx-text-inner-color: white;");
            grid.add(textFieldattribute,1,posX);
            posX++;
        }
        Button SaveButton = new Button();
//        SaveButton.setBackground(new BackgroundFill(Color.web("#eeeeee"),CornerRadii.EMPTY, Insets.EMPTY)));
//        SaveButton.setStyle("-fx-text-inner-color:#eeeeee");
        SaveButton.setAlignment(Pos.BOTTOM_RIGHT);
        SaveButton.setTextFill(Color.web("#393e46"));
        SaveButton.setText("Guardar");
//        SaveButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        grid.add(SaveButton,1,arrayLabel.length);
        stage.setMaxHeight(430);
        stage.setMaxWidth(380);
        stage.setTitle("Ingresar un nuevo registro");
//        root.getChildren().add(vBox);
        root.getChildren().add(grid);

        Scene windowNewData = new Scene(root, 430, 380);
        stage.setScene(windowNewData);
        stage.show();

    }


    /**
     * Método que muestra la ventana de creación de nuevo esquema.
     */
    public static void newData() {
        new NewData().start(new Stage());
    }

}
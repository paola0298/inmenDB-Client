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
    private String[] arrayLabel={"hola","adios", "Hello"};
    private JSONObject scheme;
    private JSONObject newRegister;
    //Se define el array con su tamano
    //private String[] array = new String[arrayJsonArray.length()];
    //se accede a los campos del array usando indices
//    array[0] = "dfg";
//    public NewData(JSONObject scheme) {
//        this.scheme = scheme;
//        arrayJsonArray = scheme.getJSONArray("attrName");
//        for(int i = 0; array.length() > i; i++){
//            arrayLabel[i] = array[i];
//            System.out.println(arrayLabel[i]);
//        }
//    }
//
 //   arrayLabel = {"h","o","l","a"};

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(Color.web("#222831"), CornerRadii.EMPTY, Insets.EMPTY)));
        int posX = 0;
        for(int i=0; arrayLabel.length > i; i++){
            System.out.println(arrayLabel[i]);
            Label label1 = new Label();
            label1.setText("Ingrese " + arrayLabel[i]);
            label1.setAlignment(Pos.CENTER);
            label1.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            label1.setTextFill(Color.web("#00adb5"));
            grid.add(label1,0,posX);
            TextField textFieldLabel1 = new TextField();
            textFieldLabel1.setText("Ingrese el dato");
            textFieldLabel1.setBackground(new Background(new BackgroundFill(Color.web("#222831"),CornerRadii.EMPTY, Insets.EMPTY)));
            textFieldLabel1.setBorder(new Border(new BorderStroke(Color.web("#00adb5"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            textFieldLabel1.setStyle("-fx-text-inner-color: white;");
            grid.add(textFieldLabel1,1,posX);
            posX++;
        }
        Button SaveButton = new Button();
//        SaveButton.setBackground(new BackgroundFill(Color.web("#eeeeee"),CornerRadii.EMPTY, Insets.EMPTY)));
        SaveButton.setStyle("-fx-text-inner-color:#eeeeee");
        SaveButton.setAlignment(Pos.BOTTOM_RIGHT);
        SaveButton.setTextFill(Color.web("#393e46"));
        SaveButton.setText("Guardar");
        SaveButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        grid.add(SaveButton,1,arrayLabel.length);
        stage.setMaxHeight(430);
        stage.setMaxWidth(380);
        stage.setTitle("Ingresar nuevo dato");
//        root.getChildren().add(vBox);
        root.getChildren().add(grid);

        Scene windowNewData = new Scene(root, 430, 380);
        stage.setScene(windowNewData);
        stage.show();

    }

    public JSONObject show() {
        launch(NewData.class);

        return newRegister;
    }

}
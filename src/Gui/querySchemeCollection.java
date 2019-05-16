package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Clase que muestra la interfaz necesaria para realizar busquedas en los esquemas creados
 * @author paola
 * @version 1.0
 */

public class querySchemeCollection extends Application {
    private static Controller controller;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 400;

    /**
     * Método que inicializa y configura la interfaz.
     * @param stage Ventana de la aplicación.
     */
    @Override
    public void start(Stage stage) {
        this.controller = Controller.getInstance();

        VBox container = new VBox();

        HBox attrBox = new HBox();
        attrBox.setSpacing(15);
        attrBox.setPadding(new Insets(15));
        attrBox.setAlignment(Pos.CENTER);

        HBox index =  new HBox();
        index.setSpacing(15);
        index.setPadding(new Insets(15));
        index.setAlignment(Pos.CENTER);

        BorderPane button = new BorderPane();
        button.setPadding(new Insets(5, 5, 5, 20));



        //Seleccionar el atributo
        Text schemeAttrText = new Text("Seleccione la columna");
        ComboBox<String> schemeAttr = new ComboBox<>();
        //TODO colocar los atributos segun el esquema

        schemeAttr.setOnMouseClicked(mouseEvent -> {
            //TODO ver si hay join y si lo hay colocar un nuevo combobox con los atributos del join
        });
        VBox attrVBox = new VBox();
        attrVBox.setAlignment(Pos.CENTER);
        attrVBox.setSpacing(5);
        attrVBox.setPadding(new Insets(5));
        attrVBox.getChildren().addAll(schemeAttrText, schemeAttr);


        //Colocar lo que se desea buscar
        Text toSearch = new Text("Ingrese el dato a buscar");
        TextField attrEntry = new TextField();
        VBox toSearchVBox = new VBox();
        toSearchVBox.setAlignment(Pos.CENTER);
        toSearchVBox.setSpacing(5);
        toSearchVBox.setPadding(new Insets(5));
        toSearchVBox.getChildren().addAll(toSearch, attrEntry);


        //Indicar si se desea buscar por indice
        Text indexText = new Text("Seleccione el indice por el cual \n   desea realizar la búsqueda");
        ComboBox<String> actualIndex = new ComboBox<>();
        //TODO colocar esquemas existentes de la columna seleccionada
        VBox indexVBox = new VBox();
        indexVBox.setAlignment(Pos.CENTER);
        indexVBox.setSpacing(5);
        indexVBox.setPadding(new Insets(5));
        indexVBox.getChildren().addAll(indexText, actualIndex);



        //Boton para buscar
        ImageView searchButton = new ImageView(loadImg("res/images/searchButton.png"));
        searchButton.setFitWidth(40);
        searchButton.setFitHeight(40);
        searchButton.setOnMouseClicked(event -> {
            String attrEntryText = attrEntry.getText();
            System.out.println("Buscando registros para " + attrEntryText);
        });




        //Imagen de fondo
        ImageView lookinFor = new ImageView(loadImg("res/images/lookingFor.jpg"));
        lookinFor.setFitWidth(170);
        lookinFor.setFitHeight(204);

        attrBox.getChildren().addAll(attrVBox, toSearchVBox);
        index.getChildren().addAll(indexVBox);
        button.setRight(searchButton);
        button.setLeft(lookinFor);
        button.setAlignment(searchButton, Pos.BOTTOM_RIGHT);
        button.setAlignment(lookinFor, Pos.CENTER_LEFT);

        container.getChildren().addAll(attrBox, index, button);


        Scene scene = new Scene(container, SCREEN_WIDTH, SCREEN_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Consultar información");
        stage.show();


    }

    private Image loadImg(String relativePath) {
        String cwd = System.getProperty("user.dir");
        return new Image("file://" + cwd + "/" + relativePath);
    }


    public static void queryScheme() {
        new querySchemeCollection().start(new Stage());
    }
}

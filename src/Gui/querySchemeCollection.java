package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase que muestra la interfaz necesaria para realizar busquedas en los esquemas creados
 * @author paola
 * @version 1.0
 */

public class querySchemeCollection extends Application {
    private static Controller controller;
    private boolean join;
    private JSONObject generatedJson;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 400;

    /**
     * Método que inicializa y configura la interfaz.
     * @param stage Ventana de la aplicación.
     */
    @Override
    public void start(Stage stage) {

        generatedJson = new JSONObject();
        join = false;

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

        JSONArray attrNames =  controller.getSelectedSchemeAttr();
        for (int i=0; i<attrNames.length(); i++) {
            schemeAttr.getItems().add(attrNames.getString(i));
        }

//        schemeAttr.setOnMouseClicked(mouseEvent -> {
//            //TODO colocar los atributos segun el esquema
//
//
//        });

        //TODO ver si hay join y si lo hay colocar un nuevo combobox con los atributos del join

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
        actualIndex.getItems().add("NO");
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
            String attrComboBox = schemeAttr.getValue();
            String indexComboBox = actualIndex.getValue();
            String joinCombobox = "";


            if (attrComboBox != null){
                if (!join){
                    //TODO asignar valor a joinCombobox
                    joinCombobox = "";
                }
                if (!attrEntryText.isBlank()){
                    if (indexComboBox != null){

                        System.out.println("Enviando datos al servidor");
                        generatedJson.put("action", "queryData");
                        JSONObject parameters = new JSONObject();
                        parameters.put("scheme", controller.getActualSchemeName());
                        if (!joinCombobox.isBlank()){ //TODO cambiar condicion por joinCombobox!=null
                            parameters.put("searchByJoin", true);
//                            parameters.put("searchBy", attrComboBox);
                            //TODO colocar como searchBy el atributo correspondiente al join
                        } else {
                            parameters.put("searchBy", attrComboBox);
                            parameters.put("searchByJoin", false);
                        }
                        parameters.put("dataToSearch", attrEntryText);
                        if (indexComboBox.equals("NO")) {
                            parameters.put("index", false);
                        }
                        else {
                            parameters.put("index", true);
                            parameters.put("tree", indexComboBox);
                        }

                        generatedJson.put("parameters", parameters.toString());
                        controller.sendQuery(generatedJson);


                    } else {
                        showAlert("Debe seleccionar algun indice para realizar una busqueda, o NO en caso de" +
                                "que no desee usar indices", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Debe ingresar un dato para buscarlo", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Debe seleccionar una columna", Alert.AlertType.ERROR);
            }

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

    /**
     * Éste método se encarga de mostrar una alerta al usuario.
     * @param message Mensaje de la alerta.
     * @param type Tipo de alerta (Info, Error..).
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.show();
    }

    public static void queryScheme() {
        new querySchemeCollection().start(new Stage());
    }
}

package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.collections.ObservableList;
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

import java.util.Hashtable;
import java.util.Set;

/**
 * Clase que muestra la interfaz necesaria para realizar busquedas en los esquemas creados
 * @author paola
 * @version 1.0
 */

public class querySchemeCollection extends Application {
    private static Controller controller;
    private boolean join;
    private JSONObject generatedJson;
    private String joinName = null;
    private String actualScheme;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 400;
    private ComboBox<String> actualIndex;
    private String attrJoin;

    /**
     * Método que inicializa y configura la interfaz.
     * @param stage Ventana de la aplicación.
     */
    @Override
    public void start(Stage stage) {
        controller = Controller.getInstance();

        generatedJson = new JSONObject();
        join = false;
        actualScheme  = controller.getActualSchemeName();


        actualIndex = new ComboBox<>();
        actualIndex.getItems().add("NO");
        actualIndex.getSelectionModel().select(0);

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

        Text joinAttrText = new Text("Seleccione la columna del join");
        ComboBox<String> joinAttrComboBox = new ComboBox<>();

        JSONArray attrNames =  controller.getSelectedSchemeAttr();
        for (int i=0; i<attrNames.length(); i++) {
            schemeAttr.getItems().add(attrNames.getString(i));
        }

        JSONObject actualSchemeStructure = controller.getSelectedScheme();
        JSONArray attrSize = actualSchemeStructure.getJSONArray("attrSize");
        JSONArray attrType = actualSchemeStructure.getJSONArray("attrType");
        Hashtable<String, String> localSchemes = controller.getLocalSchemes();



        VBox joinVBox = new VBox();
        joinVBox.setAlignment(Pos.CENTER);
        joinVBox.setSpacing(5);
        joinVBox.setPadding(new Insets(5));

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

        attrBox.getChildren().add(attrVBox);

        schemeAttr.setOnAction(actionEvent -> {
            actualIndex.getItems().clear();
            actualIndex.getItems().add("NO");
            actualIndex.getSelectionModel().select(0);
            join = false;
            int size = attrBox.getChildren().size();
            if (size>1) {
                attrBox.getChildren().remove(toSearchVBox);
                attrBox.getChildren().remove(joinVBox);
                joinVBox.getChildren().clear();
            }
            int indexAttr = schemeAttr.getSelectionModel().getSelectedIndex();
            String atType = attrType.getString(indexAttr);
            attrJoin = null;
            if (atType.equals("join")){
                attrJoin = attrSize.getString(indexAttr);
                joinName = attrJoin;
                System.out.println("La columna seleccionada es de tipo join");
                join = true;
            }
            if (join) {
                if (attrJoin!=null){
                    JSONObject schemeSelected = new JSONObject(localSchemes.get(attrJoin));
                    JSONArray attrNameJoin = schemeSelected.getJSONArray("attrName");
                    for (int j = 0; j < attrNameJoin.length(); j++) {
                        joinAttrComboBox.getItems().add(attrNameJoin.getString(j));
                    }
                    joinVBox.getChildren().addAll(joinAttrText, joinAttrComboBox);
                    attrBox.getChildren().add(joinVBox);


                    joinAttrComboBox.setOnAction(actionEvent1 -> {
                        JSONArray indexOfJoin = findIndex(true, joinAttrComboBox.getSelectionModel().getSelectedItem());
                        for (int i=0; i<indexOfJoin.length(); i++){
                            actualIndex.getItems().add(indexOfJoin.getString(i));
                        }

                        //true es si hay join attrjoin es el nombre del join

                    });
                    //todo buscar si el join tiene indice en la columna seleccionada combobox actualindex
                }
            } else {
                //todo buscar si hay indice en la columna seleccionada combobox actualindex
                JSONArray indexOfJoin = findIndex(false, schemeAttr.getSelectionModel().getSelectedItem());
                for (int i=0; i<indexOfJoin.length(); i++){
                    actualIndex.getItems().add(indexOfJoin.getString(i));
                }
            }
            attrBox.getChildren().add(toSearchVBox);
        });



        //Indicar si se desea buscar por indice
        Text indexText = new Text("Seleccione el indice por el cual \n   desea realizar la búsqueda");
//        addIndex(actualIndex);
        //TODO colocar indices existentes de la columna seleccionada
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
            String attrComboBox = schemeAttr.getSelectionModel().getSelectedItem();
            String indexComboBox = actualIndex.getSelectionModel().getSelectedItem();
            String joinCombobox = "";

            boolean generated  = generateJson(attrEntryText, attrComboBox, indexComboBox, joinCombobox, joinAttrComboBox);

            if (generated) {
                System.out.println("JSON Generated " + generatedJson);
                controller.sendQuery(generatedJson);
                stage.close();
            }

        });




        //Imagen de fondo
        ImageView lookinFor = new ImageView(loadImg("res/images/lookingFor.jpg"));
        lookinFor.setFitWidth(170);
        lookinFor.setFitHeight(204);



        index.getChildren().addAll(indexVBox);
        button.setRight(searchButton);
        button.setLeft(lookinFor);
        BorderPane.setAlignment(searchButton, Pos.BOTTOM_RIGHT);
        BorderPane.setAlignment(lookinFor, Pos.CENTER_LEFT);

        container.getChildren().addAll(attrBox, index, button);


        Scene scene = new Scene(container, SCREEN_WIDTH, SCREEN_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Consultar información");
        stage.show();


    }

    private JSONArray findIndex(boolean join, String joinColumn) {
        JSONObject indexCreated = controller.getListOfIndex();
        JSONArray indexToAdd = new JSONArray();
        Set<String> keySet = indexCreated.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {

                String scheme = null;

                if (join)
                    scheme = attrJoin;
                else
                    scheme = actualScheme;

                if (key.equals(scheme)) {
                    JSONArray actualIndexList = indexCreated.getJSONArray(key);

                    for (int i = 0; i < actualIndexList.length(); i++) {

                        JSONArray innerList = actualIndexList.getJSONArray(i);
                        String column = innerList.getString(1);
                        String indexName = innerList.getString(0);
                        if (column.equals(joinColumn)){
                            indexToAdd.put(indexName);
                        }
                    }
                    break;
                }
            }
        }

        return indexToAdd;
    }

    private void addIndex(ComboBox<String> actualIndexCombo) {
        JSONObject indexArray = controller.getListOfIndex();
        Set<String> keySet = indexArray.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (key.equals(actualScheme)) {
                    JSONArray actualIndexList = indexArray.getJSONArray(key);

                    for (int i = 0; i < actualIndexList.length(); i++) {
                        actualIndexCombo.getItems().add(actualIndexList.getString(i));
                    }
                    break;
                }
            }
        }
    }

    private boolean generateJson(String attrEntryText, String attrComboBox, String indexComboBox, String joinCombobox,
                              ComboBox<String> joinAttrComboBox) {
        if (attrComboBox != null){
            if (join){

                joinCombobox = joinAttrComboBox.getValue();
            }
            if (!attrEntryText.isBlank()){
                if (indexComboBox != null){

                    System.out.println("Enviando datos al servidor");
                    generatedJson.put("action", "queryData");
                    JSONObject parameters = new JSONObject();
                    parameters.put("scheme", actualScheme);
                    if (!joinCombobox.isBlank()){
                        parameters.put("searchByJoin", true);
                        parameters.put("searchBy", joinCombobox);
                        parameters.put("joinName", joinName);
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

                    return true;



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

        return false;

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

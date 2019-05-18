package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.LinkedHashMap;


public class NewData extends Application {
    private static Controller controller;
    private JSONObject generatedJson;
    private boolean join;
    private int posOfPk;

    @Override
    public void start(Stage stage) {
        controller = Controller.getInstance();
        generatedJson = new JSONObject();
        join = false;

        StackPane root = new StackPane();
        HBox container = new HBox();
        VBox labels = new VBox();
        VBox text = new VBox();

        labels.setAlignment(Pos.CENTER_LEFT);
        labels.setSpacing(20);

        text.setAlignment(Pos.CENTER_LEFT);
        text.setSpacing(10);

        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        JSONObject actualScheme = controller.getSelectedScheme(); //estructura
//        System.out.println("[NEWDATA] Actual scheme");
//        System.out.println(actualScheme.toString(5));

        JSONArray attrNames = actualScheme.getJSONArray("attrName");
        JSONArray attrType = actualScheme.getJSONArray("attrType");
        JSONArray attrSize = actualScheme.getJSONArray("attrSize");

        String actualSchemeName = actualScheme.getString("name");

        Hashtable<String, Hashtable<String, JSONArray>> localCollections =  controller.getLocalCollections();
        Hashtable<String, JSONArray> actualCollectionScheme = localCollections.get(actualSchemeName);


        //////
        String actualPkAttr = actualScheme.getString("primaryKey");
        posOfPk = -1;
        for(int i=0; i<attrNames.length(); i++){
            if (attrNames.get(i).equals(actualPkAttr)){
                posOfPk = i;
            }
        }
        //////
        //agregar datos a combobox

        for(int i=0; i < attrNames.length(); i++){
            Label attribute = new Label(attrNames.getString(i));
            attribute.setAlignment(Pos.CENTER);
            labels.getChildren().add(attribute);

            if (attrType.get(i).equals("join")){
                join = true;
                Hashtable<String, JSONArray> joinCollection = localCollections.get(attrSize.getString(i));

                if (joinCollection != null && joinCollection.size() > 0) {
                    ComboBox<String> collections = new ComboBox<>(FXCollections.observableArrayList(joinCollection.keySet()));
                    collections.setUserData("join");
                    text.getChildren().add(collections);
                } else {
                    showAlert("No hay registros en el esquema con el que se hace join", Alert.AlertType.ERROR);
                    return;
                }
            } else {
                TextField textField = new TextField();
                textField.setUserData("normal");
                text.getChildren().add(textField);
            }
        }


        ImageView saveButton = new ImageView(loadImg("res/images/save.png"));
        saveButton.setFitWidth(60);
        saveButton.setFitHeight(60);
        saveButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("Saving records...");

            JSONArray attr = new JSONArray();
            String attribute;

            String pk = "";

            //Se obtienen los valores de los textfields y combobox si hay.
            for (int i=0; i<text.getChildren().size(); i++) {
                //TODO verificar el tipo de widget que es usando getUserData();
                if (!join) {
                    TextField data = (TextField) text.getChildren().get(i);
                    attribute = data.getText();

                } else {
                    try {
                        TextField data = (TextField) text.getChildren().get(i);
                        attribute = data.getText();
                    } catch (Exception e) {
                        ComboBox<String> collectionId = (ComboBox<String>) text.getChildren().get(i);
                        attribute = collectionId.getValue();
                    }
                }

                if (attribute.equals(pk)){
                    for (String key : actualCollectionScheme.keySet()){
                        if (key.equals(attribute)){
                            showAlert("No se permiten llaves primarias repetidas", Alert.AlertType.ERROR);
                            return;
                        }
                    }
                }

                //TODO verificar tamaño del dato y tipo de dato

                boolean numeric = StringUtils.isNumeric(attribute);
                String attriType = attrType.getString(i);

                if (attriType.equals("string") && numeric) {
                    System.out.println("string && numeric");
                }

                attr.put(attribute);
            }

            System.out.println("Attributes " + attr);

            boolean foundPkInCollection = foundPk(attr, actualCollectionScheme);

            if (!foundPkInCollection) {

                generatedJson.put("action", "insertData");
                generatedJson.put("schemeName", actualSchemeName);
                generatedJson.put("attr", attr);

                controller.insertData(generatedJson);
                stage.close();
            } else {
                showAlert("El valor en " + actualPkAttr + " ya existe", Alert.AlertType.ERROR);
            }

        });

//        container.add(saveButton,1,attrNames.length());
        container.getChildren().addAll(labels, text, saveButton);

        root.getChildren().add(container);
        Scene scene = new Scene(root, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Agregar nuevos registros");
        stage.show();

    }

    private boolean foundPk(JSONArray attr, Hashtable<String, JSONArray> actualCollectionScheme) {
        if (posOfPk!=-1){
            String actualPk = attr.getString(posOfPk);
            if (actualCollectionScheme != null) {
                for (String key : actualCollectionScheme.keySet()) {
                    if (key.equals(actualPk)) {
                        return true;
                    }
                }
            }
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


    /**
     * Método que muestra la ventana de creación de nuevo esquema.
     */
    public static void newData() {
        new NewData().start(new Stage());
    }

}
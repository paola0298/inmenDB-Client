package Gui;

import Logic.Controller;
import javafx.application.Application;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.LinkedHashMap;


public class NewData extends Application {
    private static Controller controller;
    private JSONObject generatedJson;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 400;
    private boolean join;

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

        JSONArray attrNames =  controller.getSelectedSchemeAttr();
        JSONArray attrType = actualScheme.getJSONArray("attrType");
        JSONArray attrSize = actualScheme.getJSONArray("attrSize");

        String actualSchemeName = controller.getActualSchemeName();
        Hashtable<String, String> localSchemes = controller.getLocalSchemes();
        Hashtable<String, Hashtable<String, String>> localCollections =  controller.getLocalCollections(); //TODO LinkedHashMap

        Hashtable<String, String> actualCollectionScheme = localCollections.get(controller.getActualSchemeName());

        //agregar datos a combobox

        for(int i=0; i < attrNames.length(); i++){
            Label attribute = new Label();
            attribute.setText(attrNames.getString(i));
            attribute.setAlignment(Pos.CENTER);
            labels.getChildren().add(attribute);

            if (attrType.get(i).equals("join")){
                join = true;
                ComboBox<String> schemes = new ComboBox<>();
                ComboBox<String> collections = new ComboBox<>();
                text.getChildren().add(schemes);

                if (localCollections.size()>0) {
                    for (String name : localCollections.keySet()) {
                        if (!name.equals(actualSchemeName)) {
                            schemes.getItems().add(name);
                        }
                    }

                    schemes.setOnAction(actionEvent -> {
                        String schemeSelected = schemes.getValue();
                        System.out.println("Scheme selected " + schemeSelected);
                        System.out.println("local collections " + localCollections);
                        Hashtable<String, String> actualCollection = localCollections.get(schemeSelected);

                        for (String id : actualCollection.keySet()) {
                            collections.getItems().add(id);
                        }

                        text.getChildren().add(collections);
                    });

                } else {
                    showAlert("Es necesario que hayan colecciones de datos de los joins con que se encuentra " +
                            "unido el esquema", Alert.AlertType.ERROR);
                    return;
                }

            } else {

                TextField textFieldAttribute = new TextField();
                text.getChildren().add(textFieldAttribute);
            }
        }



        ImageView saveButton = new ImageView(loadImg("res/images/save.png"));
        saveButton.setFitWidth(60);
        saveButton.setFitHeight(60);
        saveButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("Saving records...");
            System.out.println(generatedJson);

            //TODO verificar que no hayan pk repetidas
            JSONArray attr = new JSONArray();
            String attribute = "";

            String pk = ""; //TODO get pk of actual register to be saved

            for (int i=0; i<text.getChildren().size(); i++) {
                if (!join) {
                    TextField data = (TextField) text.getChildren().get(i);
                    attribute = data.getText();
                } else {
                    try {
                        TextField data = (TextField) text.getChildren().get(i);
                        attribute = data.getText();
                    } catch (Exception e) {
                        ComboBox<String> scheme = (ComboBox<String>) text.getChildren().get(i);
                        i++;
                        ComboBox<String> collectionId = (ComboBox<String>) text.getChildren().get(i);
                        attribute = collectionId.getValue();
                    }
                }

                if (attribute.equals(pk)){
                    //TODO verificar con las demas pk de las otras colecciones de ese esquema
                    for (String key : actualCollectionScheme.keySet()){
                        if (key.equals(attribute)){
                            showAlert("No se permiten llaves primarias repetidas", Alert.AlertType.ERROR);
                            return;
                        }
                    }
                }

                attr.put(attribute);



//                try {
//
//                    //TODO verificar tamaño del dato
//
//
//                    String attriType = attrType.getString(i);
//
//                    switch (attriType) {
//                        case "int":
//                            System.out.println("Checking int");
//                            break;
//                        case "float":
//                            System.out.println("Checking float");
//                            break;
//                        case "long":
//                            System.out.println("Checking long");
//                            break;
//                        case "double":
//                            System.out.println("Checking double");
//                            break;
//                        case "string":
//                            System.out.println("Checking string");
//                            break;
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
            }

            System.out.println("Attributes " + attr);

            generatedJson.put("action", "insertData");
            generatedJson.put("schemeName", actualSchemeName);
            generatedJson.put("attr", attr);


            controller.insertData(generatedJson);
            stage.close();

        });

//        container.add(saveButton,1,attrNames.length());
        container.getChildren().addAll(labels, text, saveButton);

        root.getChildren().add(container);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Agregar nuevos registros");
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


    /**
     * Método que muestra la ventana de creación de nuevo esquema.
     */
    public static void newData() {
        new NewData().start(new Stage());
    }

}
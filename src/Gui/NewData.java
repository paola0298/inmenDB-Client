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
import org.apache.commons.lang3.StringUtils;
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





        JSONArray attrNames =  controller.getSelectedSchemeAttr();
        JSONArray attrType = actualScheme.getJSONArray("attrType");
        JSONArray attrSize = actualScheme.getJSONArray("attrSize");

        String actualSchemeName = controller.getActualSchemeName();
        Hashtable<String, String> localSchemes = controller.getLocalSchemes();
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
            Label attribute = new Label();
            attribute.setText(attrNames.getString(i));
            attribute.setAlignment(Pos.CENTER);
            labels.getChildren().add(attribute);

            if (attrType.get(i).equals("join")){

                String joinScheme = attrSize.getString(i);
                Hashtable<String, JSONArray> joinCollection = localCollections.get(joinScheme);

                join = true;
                ComboBox<String> collections = new ComboBox<>();

                if (joinCollection!=null && joinCollection.size()>0 ) {
                    for (String id : joinCollection.keySet()) {
                        collections.getItems().add(id);

                    }

                    text.getChildren().add(collections);
                } else {
                    showAlert("No hay registros en el esquema con el que se hace join", Alert.AlertType.ERROR);
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

            JSONArray attr = new JSONArray();
            String attribute = "";

            String pk = "";

            for (int i=0; i<text.getChildren().size(); i++) {
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
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

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
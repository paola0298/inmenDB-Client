package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;


public class NewData extends Application {
    private static Controller controller;
    private JSONObject generatedJson;
    private boolean join;
    private int posOfPk;
    private boolean correctDataType;
    private VBox labels;
    private VBox text;

    @Override
    public void start(Stage stage) {
        controller = Controller.getInstance();
        generatedJson = new JSONObject();
        join = false;
        correctDataType = false;

        StackPane root = new StackPane();
        HBox container = new HBox();
        labels = new VBox();
        text = new VBox();

        labels.setAlignment(Pos.CENTER_LEFT);
        labels.setSpacing(20);

        text.setAlignment(Pos.CENTER_LEFT);
        text.setSpacing(10);

        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        JSONObject actualScheme = controller.getSelectedScheme(); //estructura


        JSONArray attrNames = actualScheme.getJSONArray("attrName");
        JSONArray attrType = actualScheme.getJSONArray("attrType");
        JSONArray attrSize = actualScheme.getJSONArray("attrSize");

        String actualSchemeName = actualScheme.getString("name");

        Hashtable<String, Hashtable<String, JSONArray>> localCollections =  controller.getLocalCollections();
        Hashtable<String, JSONArray> actualCollectionScheme = localCollections.get(actualSchemeName);


        //////
        String actualPkAttr = actualScheme.getString("primaryKey");

        posOfPk = foundPosOfPk(attrNames, actualPkAttr);

        //////

        //agregar attributos a la ventana

        addWidgetsToLayout(attrNames, attrType, attrSize, localCollections);


        ImageView saveButton = new ImageView(loadImg("res/images/save.png"));
        saveButton.setFitWidth(60);
        saveButton.setFitHeight(60);
        saveButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("Saving records...");

            JSONArray attr = new JSONArray();
            String attribute;



            for (int i=0; i<text.getChildren().size(); i++) {

                //Se obtienen los valores de los textfields y combobox si hay.
                attribute = getAttribute(text, i);
                String attriType = attrType.getString(i);

                boolean numeric = StringUtils.isNumeric(attribute);
                String name = attrNames.getString(i);

                if (!text.getChildren().get(i).getUserData().equals("combobox")) {

                    correctDataType = checkDataType(numeric, attriType, name, attribute, attrSize.getInt(i));

                    if (correctDataType)
                        attr.put(attribute);
                    else
                        return;
                } else {
                    attr.put(attribute);
                }
            }

            boolean foundPkInCollection = foundPk(attr, actualCollectionScheme);

            if (!foundPkInCollection) {
                generatedJson.put("action", "insertData");
                generatedJson.put("schemeName", actualSchemeName);
                generatedJson.put("attr", attr);

                controller.insertData(generatedJson);
                stage.close();

            } else {
                showAlert("No se permiten llaves primarias duplicadas, cambie el valor de : " + actualPkAttr, Alert.AlertType.ERROR);
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



    private void addWidgetsToLayout(JSONArray attrNames, JSONArray attrType, JSONArray attrSize, Hashtable<String, Hashtable<String, JSONArray>> localCollections) {
        for(int i=0; i < attrNames.length(); i++){
            Label attribute = new Label();
            attribute.setText(attrNames.getString(i));
            attribute.setAlignment(Pos.CENTER);
            labels.getChildren().add(attribute);

            if (attrType.get(i).equals("join")){

                Hashtable<String, JSONArray> joinCollection = localCollections.get(attrSize.getString(i));

                join = true;
                ComboBox<String> collections = new ComboBox<>();
                collections.setUserData("combobox");

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
                textFieldAttribute.setUserData("textField");
                text.getChildren().add(textFieldAttribute);
            }
        }
    }

    private int foundPosOfPk(JSONArray attrNames, String actualPkAttr) {
        for(int i=0; i<attrNames.length(); i++){
            if (attrNames.get(i).equals(actualPkAttr)){
                return i;
            }
        }
        return -1;
    }

    private String getAttribute(VBox text, int i) {

        String type = (String) text.getChildren().get(i).getUserData();

        if (type.equals("combobox")) {
            ComboBox<String> collectionId = (ComboBox<String>) text.getChildren().get(i);
            return collectionId.getSelectionModel().getSelectedItem();
        } else {
            TextField data = (TextField) text.getChildren().get(i);
            return data.getText();
        }

    }

    private boolean checkDataType(boolean numeric, String attriType, String name, String attribute, int attrSize) {

        int attrLength = attribute.length();

        System.out.println("Tamaño del dato ingresado " + attrLength);
        System.out.println("Tamaño correcto " + attrSize);
        if (attrLength <= attrSize) {

            if (attriType.equals("string") && numeric) {
                showAlert("El atributo " + name + " debe ser de tipo string", Alert.AlertType.ERROR);
                return false;

            } else if (attriType.equals("string") && !numeric) {
                return true;


            } else if (attriType.equals("int") || attriType.equals("long")) {
                if (numeric) {
                    if (attriType.equals("int")) {
                        try {
                            Integer.parseInt(attribute);
                            return true;
                        } catch (Exception e) {
                            showAlert("El atributo " + name + " debe ser de tipo entero", Alert.AlertType.ERROR);
                            return false;
                        }
                    } else {
                        try {
                            Long.parseLong(attribute);
                            return true;
                        } catch (Exception e) {
                            showAlert("El atributo " + name + " debe ser de tipo long", Alert.AlertType.ERROR);
                            return false;
                        }
                    }
                } else {
                    showAlert("El atributo " + name + " no puede ser decimal ni string", Alert.AlertType.ERROR);
                    return false;
                }
            } else if (attriType.equals("float") || attriType.equals("double")) {
                if (numeric) {
                    showAlert("El atributo " + name + " debe ser decimal", Alert.AlertType.ERROR);
                    return false;
                } else {
                    //si no es numerico puede ser string, float o double
                    System.out.println("checking float or double");

                    attribute = attribute.replaceAll(",", ".");

                    if (attriType.equals("float")) {
                        try {
                            Float.parseFloat(attribute);
                            return true;
                        } catch (Exception e) {
                            showAlert("El atributo " + name + " debe ser de tipo flotante", Alert.AlertType.ERROR);
                            return false;
                        }
                    } else {
                        try {
                            Double.parseDouble(attribute);
                            return true;
                        } catch (Exception e) {
                            showAlert("El atributo " + name + " debe ser de tipo double", Alert.AlertType.ERROR);
                            return false;
                        }
                    }


                }
            }
        } else {
            showAlert("El tamaño del atributo " + name + " debe ser menor o igual a " + attrSize, Alert.AlertType.ERROR);
            return false;
        }
        return false;
    }

    private boolean foundPk(JSONArray attr, Hashtable<String, JSONArray> actualCollectionScheme) {
        if (posOfPk!=-1){
            String actualPk = attr.getString(posOfPk);
            System.out.println("actual pk " + actualPk);

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
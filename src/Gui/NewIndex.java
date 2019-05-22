package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class NewIndex extends Application {

    private static Controller controller;
    private static JSONObject generatedJson;
    private String actualSchemeName = controller.getActualSchemeName();


    @Override
    public void start(Stage stage) {
        controller = Controller.getInstance();
        generatedJson = new JSONObject();

        GridPane mainLayout = new GridPane();

        Label title  = new Label("Crear índice del esquema: " + actualSchemeName);

        Label nameLabel = new Label("Nombre");
        TextField nameTextField = new TextField();

        Label attrLabel = new Label("Atributo");
        ComboBox<String> attrComboBox = new ComboBox<>();
        loadAttr(attrComboBox);

        Label treeLabel = new Label("Árbol");
        ComboBox<String> treeComboBox = new ComboBox<>(FXCollections.observableArrayList("AA", "AVL", "Binario",
                "B", "B+", "Rojo-Negro", "Splay"));

        ImageView saveButton = new ImageView(loadImg("res/images/save.png"));
        saveButton.setFitWidth(60);
        saveButton.setFitHeight(60);
        saveButton.setOnMouseClicked(mouseEvent -> {
            String indexName = nameTextField.getText();
            String column = attrComboBox.getSelectionModel().getSelectedItem();
            String tree = treeComboBox.getSelectionModel().getSelectedItem();

            if (checkEntry(indexName, column, tree)){
                generateJson(indexName, column, tree);
                controller.createIndex(generatedJson);
                stage.close();
            }
        });


        ImageView background = new ImageView(loadImg("res/images/index.png"));
        background.setFitWidth(150);
        background.setFitHeight(150);

        //mainLayout setup

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setVgap(5);
        mainLayout.setHgap(15);


        RowConstraints row = new RowConstraints();
        row.setPercentHeight(20);
        mainLayout.getRowConstraints().add(row);

        //columna, fila

        mainLayout.add(title, 0,0, 5, 1);
        GridPane.setHalignment(title, HPos.CENTER);
        mainLayout.add(nameLabel, 1, 1);
        mainLayout.add(nameTextField, 1, 2);
        mainLayout.add(attrLabel, 2, 1);
        mainLayout.add(attrComboBox, 2, 2);
        mainLayout.add(treeLabel, 3, 1);
        mainLayout.add(treeComboBox, 3, 2, 2, 1);
        mainLayout.add(background, 0, 3);
        GridPane.setHalignment(background, HPos.LEFT);
        mainLayout.add(saveButton, 4, 3);
        GridPane.setHalignment(saveButton, HPos.RIGHT);


        Scene scene = new Scene(mainLayout, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Crear un nuevo índice");
        stage.show();

    }

    private void generateJson(String indexName, String column, String tree) {
        generatedJson = new JSONObject();
        generatedJson.put("action", "createIndex");
        generatedJson.put("scheme", actualSchemeName);
        generatedJson.put("indexName", indexName);
        generatedJson.put("attr", column);
        generatedJson.put("tree", tree);
    }

    private boolean checkEntry(String indexName, String column, String tree) {
        if (indexName.isBlank()) {
            showAlert("Debe ingresar un nombre", Alert.AlertType.ERROR);
            return false;
        }
        if (column.isBlank()) {
            showAlert("Debe seleccionar un atributo", Alert.AlertType.ERROR);
            return false;
        }
        if (tree.isBlank()) {
            showAlert("Debe seleccionar un árbol", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void loadAttr(ComboBox<String> attrComboBox) {
        JSONArray names = controller.getSelectedSchemeAttr();
        for (int i=0; i<names.length(); i++) {
            attrComboBox.getItems().add(names.getString(i));
        }
    }

    private javafx.scene.image.Image loadImg(String relativePath) {
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
    public static void newIndex() {
        new NewIndex().start(new Stage());
    }
}

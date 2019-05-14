package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Clase que muestra la interfaz necesaria para generar un nuevo esquema, así como generar el JSONObject
 * correspondiente.
 *
 * @author marlon
 * @version 1.0
 */
public class NewScheme extends Application {

    private static Controller controller;
    private static JSONObject generatedJson;
    private static JSONObject editableJson;

    private ImageView addButton;
    private TextField schemeNameField;
    private ToggleGroup primaryKeyGroup;

    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 400;
    private static boolean modifyScheme = false;

    /**
     * Método que inicializa y configura la interfaz.
     * @param stage Ventana de la aplicación.
     */
    @Override
    public void start(Stage stage) {
        controller = Controller.getInstance();
        generatedJson = new JSONObject();
        this.primaryKeyGroup = new ToggleGroup();

        BorderPane mainLayout = new BorderPane();

        VBox upperContainer = new VBox();

        HBox namePanel = new HBox();
        namePanel.setSpacing(15);
        namePanel.setAlignment(Pos.CENTER);
        namePanel.setPadding(new Insets(10));
        Label schemeNameLabel = new Label("Nombre del esquema: ");
        schemeNameField = new TextField();
        namePanel.getChildren().addAll(schemeNameLabel, schemeNameField);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        GridPane attrGrid = new GridPane();
        setupGridpane(attrGrid);
        Label name = new Label("Nombre");
        Label type = new Label("Tipo");
        Label size = new Label("Tamaño");
        Label id = new Label("Llave Primaria");
        id.setMaxWidth(80);
        id.setWrapText(true);
        id.setTextAlignment(TextAlignment.CENTER);

        GridPane.setHalignment(name, HPos.CENTER);
        GridPane.setHalignment(type, HPos.CENTER);
        GridPane.setHalignment(size, HPos.CENTER);
        GridPane.setHalignment(id, HPos.CENTER);

        attrGrid.addRow(0, name, type, size, id, new HBox());
        scrollPane.setContent(attrGrid);

        addButton = new ImageView(loadImg("res/images/plus.png"));
        addButton.setFitWidth(26);
        addButton.setFitHeight(26);

        ContextMenu cm = new ContextMenu();
        MenuItem normalItem = new MenuItem("Normal");
        normalItem.setOnAction(actionEvent -> addAttribute(attrGrid));

        MenuItem joinItem = new MenuItem("Join");
        joinItem.setOnAction(actionEvent -> {
            if (!controller.getLocalSchemes().isEmpty()) {
                addJoinAttribute(attrGrid);
            } else {
                showAlert("No hay esquemas disponibles", Alert.AlertType.INFORMATION);
            }
        });
        cm.getItems().addAll(normalItem, joinItem);
        addButton.setOnMouseClicked(mouseEvent -> {
            if (!cm.isShowing()) cm.show(addButton, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            else cm.hide();
        });
        addButton.setOnMouseEntered(mouseEvent -> addButton.setEffect(new DropShadow(6, Color.BLACK)));
        addButton.setOnMouseExited(mouseEvent -> addButton.setEffect(null));


        upperContainer.getChildren().addAll(namePanel, scrollPane);

        HBox options = new HBox();
        options.setSpacing(15);
        options.setPadding(new Insets(5));
        options.setAlignment(Pos.CENTER_RIGHT);
        HBox leftContainer = new HBox(addButton);
        HBox.setHgrow(leftContainer, Priority.ALWAYS);
        leftContainer.setAlignment(Pos.CENTER_LEFT);

        Button cancel = new Button("Cancelar");
        cancel.setOnAction(actionEvent -> {
            generatedJson.put("status", "CANCELLED");
            stage.close();
        });
        Button accept = new Button("Aceptar");
        accept.setOnAction(actionEvent -> {
            if (attrGrid.getRowCount() > 1) {
                if (!schemeNameField.getText().isBlank()) {
                    if (createJson(attrGrid)) {
                        controller.sendScheme(generatedJson);
                        stage.close();
                    } else {

                        showAlert("Atributos con valores inválidos", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Debes escribir un nombre para el esquema", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("El esquema debe tener al menos un atributo", Alert.AlertType.ERROR);
            }
        });

        options.getChildren().addAll(leftContainer, cancel, accept);

        if (modifyScheme) {
            loadScheme(attrGrid);
        }

        mainLayout.setCenter(upperContainer);
        mainLayout.setBottom(options);

        Scene scene = new Scene(mainLayout, SCREEN_WIDTH, SCREEN_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Crear nuevo esquema");
        stage.show();
    }

    private void loadScheme(GridPane container) {
        System.out.println("Window set to modify..");

        schemeNameField.setText(editableJson.getString("name"));
        schemeNameField.setEditable(false);

        JSONArray names = editableJson.getJSONArray("attrName");
        JSONArray types = editableJson.getJSONArray("attrType");
        JSONArray sizes = editableJson.getJSONArray("attrSize");

        int attrCount = editableJson.getInt("attrCount");
        for (int i=0; i<attrCount; i++) {
            String name = names.getString(i);

            String type = types.getString(i);

            int size = sizes.getInt(i);

        }
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
     * Método encargado de generar los elementos de la interfaz necesarios para agregar un atributo de tipo Join.
     *
     * @param container Gridpane principal que contiene los atributos.
     */
    private void addJoinAttribute(GridPane container) {
        TextField attrName = new TextField();
        attrName.setUserData("join");
        Label attrType = new Label("Join");

        Hashtable<String, JSONObject> localSchemes = controller.getLocalSchemes();

        ComboBox<String> schemeToSelect = new ComboBox<>();
        for (String scheme: localSchemes.keySet()){
            schemeToSelect.getItems().add(scheme);
        }

        RadioButton primary = new RadioButton();
        primary.setUserData(container.getRowCount());
        primary.setToggleGroup(primaryKeyGroup);
        if (container.getRowCount() == 1) {
            primary.setSelected(true);
        }

        //TODO no mostrar el primary cuando se edita un esquema
        if (!modifyScheme) {
            ImageView delete = new ImageView(loadImg("res/images/delete.png"));
            delete.setFitWidth(25);
            delete.setFitHeight(25);
            delete.setUserData(container.getRowCount());
            delete.setOnMouseClicked(mouseEvent -> {
                primary.setToggleGroup(null);
                container.getChildren().removeAll(attrName, attrType, schemeToSelect, primary, delete);
                refreshGrid(container);
            });

            GridPane.setHalignment(delete, HPos.CENTER);
            container.addRow(container.getRowCount(), attrName, attrType, schemeToSelect, primary, delete);

        } else {
            container.addRow(container.getRowCount(), attrName, attrType, schemeToSelect, primary);
        }
    }

    /**
     * Método encargado de generar los elementos de la interfaz necesarios para agregar un atributo de los
     * tipos convencionales.
     *
     * @param container Gridpane principal que contiene los atributos.
     */
    private void addAttribute(GridPane container) {

        TextField attrName = new TextField();
        attrName.setUserData("normal");
        ComboBox<String> attrType = new ComboBox<>(
                FXCollections.observableArrayList("int", "string", "float", "long", "double"));
        attrType.getSelectionModel().select(0);

        Spinner<Integer> attrSize = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5000, 1);
        attrSize.setValueFactory(valueFactory);
        attrSize.setEditable(true);

        GridPane.setHalignment(attrName, HPos.CENTER);
        GridPane.setHalignment(attrType, HPos.CENTER);
        GridPane.setHalignment(attrSize, HPos.CENTER);

        if (!modifyScheme) {
            System.out.println("Full control");
            RadioButton primary = new RadioButton();
            primary.setUserData(container.getRowCount());
            primary.setToggleGroup(primaryKeyGroup);
            if (container.getRowCount() == 1) {
                primary.setSelected(true);

            }

            ImageView delete = new ImageView(loadImg("res/images/delete.png"));
            delete.setFitHeight(25);
            delete.setFitWidth(25);

            delete.setUserData(container.getRowCount());
            delete.setOnMouseClicked(mouseEvent -> {
                if (primary.isSelected()) {
                    if (primaryKeyGroup.getToggles().size() > 1) {
                        primaryKeyGroup.selectToggle(primaryKeyGroup.getToggles().get(1));
                    }
                }
                primary.setToggleGroup(null);
                container.getChildren().removeAll(attrName, attrType, attrSize, primary, delete);
                refreshGrid(container);
            });

            GridPane.setHalignment(primary, HPos.CENTER);
            GridPane.setHalignment(delete, HPos.CENTER);
            container.addRow(container.getRowCount(), attrName, attrType, attrSize, primary, delete);

        } else {
            System.out.println("Limited control");
            container.addRow(container.getRowCount(), attrName, attrType, attrSize);
        }
    }

    /**
     * Método encargado de refrescar el contenedor de los atributos.
     *
     * @param container Gridpane principal que contiene los atributos.
     */
    private void refreshGrid(GridPane container) {
        int columnCount = container.getColumnCount();
        for (int row=0; row<container.getRowCount()-1; row++) {
            //TODO column count -> 5
            for (int column=0; column<columnCount; column++) {
                int index = ((row*columnCount)+column);

                Node item = container.getChildren().get(index);
                GridPane.setColumnIndex(item, column);
                GridPane.setRowIndex(item, row);
            }
        }
    }

    /**
     * Éste método se encarga de generar el json, el cual será enviado al servidor.
     * @param container Gridpane principal que contiene los atributos.
     * @return true si el json se generó correctamente, de lo contrario devuelve false.
     */
    private boolean createJson(GridPane container) {

        generatedJson = new JSONObject();
        generatedJson.put("action", "createScheme");

        JSONObject scheme = new JSONObject();
        scheme.put("name", schemeNameField.getText());

        JSONArray nameArray = new JSONArray();
        JSONArray typeArray = new JSONArray();
        JSONArray sizeArray = new JSONArray();

        boolean hasJoin = false;

        int columnCount = container.getColumnCount();

        for (int row=1; row<container.getRowCount(); row++) {
            //TODO column count -> 5
            TextField nameWidget = (TextField) container.getChildren().get(row*columnCount);
            String typeFlag = (String) nameWidget.getUserData();

            if (!nameWidget.getText().isBlank()) {
                nameArray.put(nameWidget.getText());
            } else {
                return false;
            }

            if (typeFlag.equals("normal")) {
                @SuppressWarnings("unchecked")
                ComboBox<String> attTypeC = (ComboBox<String>) container.getChildren().get(row*columnCount+1);
                typeArray.put(attTypeC.getSelectionModel().getSelectedItem());

                @SuppressWarnings("unchecked")
                Spinner<Integer> attSize = (Spinner<Integer>) container.getChildren().get(row*columnCount+2);
                sizeArray.put(attSize.getValue());

            } else {
                typeArray.put("join");
                hasJoin = true;
                @SuppressWarnings("unchecked")
                ComboBox<String> schemeToJoin = (ComboBox<String>) container.getChildren().get(row*columnCount+2);
                sizeArray.put(schemeToJoin.getSelectionModel().getSelectedItem());
            }
        }

        boolean coincidences = false;

        for (int i=0; i<nameArray.length(); i++) {
            for (int j=i+1; j<nameArray.length(); j++) {
                if (nameArray.getString(i).equals(nameArray.getString(j))) {
                    coincidences = true;
                }
            }
        }

        scheme.put("hasJoin", hasJoin);
        scheme.put("attrName", nameArray);
        scheme.put("attrType", typeArray);
        scheme.put("attrSize", sizeArray);
        scheme.put("primaryKey", nameArray.getString(((Integer) primaryKeyGroup.getSelectedToggle().getUserData())-1));
        scheme.put("attrCount", container.getRowCount()-1);

        generatedJson.put("scheme", scheme);

        return !coincidences;
    }

    /**
     * Método encargado de cargar una imagen desde la ruta especificada.
     * @param relativePath Ruta relativa de la imagen.
     * @return Instancia de la imagen.
     */

    private Image loadImg(String relativePath) {
        String cwd = System.getProperty("user.dir");
        return new Image("file://" + cwd + "/" + relativePath);
    }

    /**
     * Éste método se encarga de configurar el gridpane de los atributos.
     * @param pane Gridpane principal que contiene los atributos.
     */

    private void setupGridpane(GridPane pane) {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(25);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPercentWidth(20);

        if (!modifyScheme) {
            ColumnConstraints column5 = new ColumnConstraints();
            column5.setPercentWidth(5);
            pane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);
        } else {
            pane.getColumnConstraints().addAll(column1, column2, column3, column4);
        }

        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(50);
        pane.getRowConstraints().add(row1);

        pane.setVgap(10);
        pane.setPadding(new Insets(10));
        GridPane.setHgrow(pane, Priority.ALWAYS);
        GridPane.setVgrow(pane, Priority.ALWAYS);
    }

    /**
     * Método que muestra la ventana de creación de nuevo esquema.
     * @return JSONObject conteniendo el esquema a crear.
     */
    public static void newScheme() {
        new NewScheme().start(new Stage());
    }

    public JSONObject updateScheme(JSONObject actualScheme) {
        modifyScheme = true;
        editableJson = actualScheme;
        launch(NewScheme.class);
        return generatedJson;
    }
}

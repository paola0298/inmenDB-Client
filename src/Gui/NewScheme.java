package Gui;

import Logic.Controller;
import javafx.application.Application;
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

public class NewScheme extends Application {

    private Controller controller;
    private static JSONObject generatedJson;
    private ToggleGroup primaryKeyGroup;
    private ImageView addButton;

    @Override
    public void start(Stage stage) {
        //this.controller = Controller.getInstance();
        generatedJson = new JSONObject();
        this.primaryKeyGroup = new ToggleGroup();

        BorderPane mainLayout = new BorderPane();

        VBox upperContainer = new VBox();

        HBox namePanel = new HBox();
        namePanel.setSpacing(15);
        namePanel.setAlignment(Pos.CENTER);
        namePanel.setPadding(new Insets(10));
        Label schemeNameLabel = new Label("Nombre del esquema: ");
        TextField schemeNameField = new TextField();
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
        normalItem.setOnAction(actionEvent -> {
            attrGrid.getChildren().remove(addButton);
            addAttribute(attrGrid, addButton);
            attrGrid.add(addButton, 4, attrGrid.getRowCount());
            GridPane.setHalignment(addButton, HPos.CENTER);
        });
        MenuItem joinItem = new MenuItem("Join");
        joinItem.setOnAction(actionEvent -> {

            //TODO verificar si hay esquemas
            if (!controller.getSchemesTable().isEmpty()) {
                attrGrid.getChildren().remove(addButton);
                addJoinAttribute(attrGrid, addButton);
                attrGrid.add(addButton, 4, attrGrid.getRowCount());
                GridPane.setHalignment(addButton, HPos.CENTER);
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

        attrGrid.add(addButton, 4, attrGrid.getRowCount());

        upperContainer.getChildren().addAll(namePanel, scrollPane);

        HBox options = new HBox();
        options.setSpacing(15);
        options.setPadding(new Insets(5));
        options.setAlignment(Pos.CENTER_RIGHT);
        Button cancel = new Button("Cancelar");
        cancel.setOnAction(actionEvent -> {
            generatedJson.put("status", "CANCELLED");
            stage.close();
        });
        Button accept = new Button("Aceptar");
        accept.setOnAction(actionEvent -> {
            if (attrGrid.getRowCount() > 2) {
                if (!schemeNameField.getText().isBlank()) {
                    attrGrid.getChildren().remove(addButton);
                    if (createJson(attrGrid, schemeNameField.getText())) {
                        attrGrid.add(addButton, 4, attrGrid.getRowCount());
                        stage.close();
                    } else {
                        attrGrid.add(addButton, 4, attrGrid.getRowCount());
                        showAlert("Atributos con valores inválidos", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Debes escribir un nombre para el esquema", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("El esquema debe tener al menos un atributo", Alert.AlertType.ERROR);
            }
        });
        options.getChildren().addAll(cancel, accept);

        mainLayout.setCenter(upperContainer);
        mainLayout.setBottom(options);
        Scene scene = new Scene(mainLayout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Crear nuevo esquema");
        stage.show();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.show();
    }

    private void addJoinAttribute(GridPane container, ImageView addButton) {
        TextField attrName = new TextField();
        attrName.setUserData("join");
        Label attrType = new Label("Join");

        //TODO cargar los esquemas disponibles desde el servidor o localmente
        ComboBox<String> schemeToSelect = new ComboBox<>();

        RadioButton primary = new RadioButton();
        primary.setUserData(container.getRowCount());
        primary.setToggleGroup(primaryKeyGroup);
        if (container.getRowCount() == 1) {
            primary.setSelected(true);
        }

//        ImageView delete = new ImageView(loadImg("res/images/delete.png", 24, 24));
        ImageView delete = new ImageView(loadImg("res/images/delete.png"));
        delete.setFitWidth(25);
        delete.setFitHeight(25);

        delete.setUserData(container.getRowCount());
        delete.setOnMouseClicked(mouseEvent -> {
            primary.setToggleGroup(null);
            container.getChildren().removeAll(attrName, attrType, schemeToSelect, primary, delete, addButton);
            reallocate(container, (Integer) delete.getUserData());
            container.add(addButton, 4, container.getRowCount());
            refreshGrid(container, addButton);
        });

        GridPane.setHalignment(attrName, HPos.CENTER);
        GridPane.setHalignment(attrType, HPos.CENTER);
        GridPane.setHalignment(schemeToSelect, HPos.CENTER);
        GridPane.setHalignment(primary, HPos.CENTER);
        GridPane.setHalignment(delete, HPos.CENTER);

        container.addRow(container.getRowCount(), attrName, attrType, schemeToSelect, primary, delete);
    }

    private void addAttribute(GridPane container, ImageView addImage) {
        TextField attrName = new TextField();
        attrName.setUserData("normal");
        ComboBox<String> attrType = new ComboBox<>(
                FXCollections.observableArrayList("int", "string", "float", "long", "double"));
        attrType.getSelectionModel().select(0);
        Spinner<Integer> attrSize = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 512, 1);
        attrSize.setValueFactory(valueFactory);

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
            container.getChildren().removeAll(attrName, attrType, attrSize, primary, delete, addImage);
            reallocate(container, (Integer) delete.getUserData());
            container.add(addImage, 4, container.getRowCount());

            refreshGrid(container, addImage);
        });

        GridPane.setHalignment(attrName, HPos.CENTER);
        GridPane.setHalignment(attrType, HPos.CENTER);
        GridPane.setHalignment(attrSize, HPos.CENTER);
        GridPane.setHalignment(primary, HPos.CENTER);
        GridPane.setHalignment(delete, HPos.CENTER);

        container.addRow(container.getRowCount(), attrName, attrType, attrSize, primary, delete);
    }

    private void reallocate(GridPane container, int rowDeleted) {

        int rowCount = container.getRowCount() - 1;
        for (; rowDeleted < rowCount; rowDeleted++) {
            for (int column = 0; column < 5; column++) {
                int actualElement = rowDeleted * 5 + column;
                Node item = container.getChildren().get(actualElement);
                GridPane.setRowIndex(item, rowDeleted);
                GridPane.setColumnIndex(item, column);
            }
        }
    }

    private void refreshGrid(GridPane container, ImageView addButton) {
        container.getChildren().remove(addButton);

        for (int row = 0; row < container.getRowCount() - 1; row++) {
            for (int column = 0; column < 5; column++) {
                int index = ((row * 5) + column);
                Node item = container.getChildren().get(index);
                GridPane.setColumnIndex(item, column);
                GridPane.setRowIndex(item, row);
            }
        }

        container.add(addButton, 4, container.getRowCount());

    }

    private boolean createJson(GridPane container, String nameField) {
//        System.out.println("Generating json..");

        generatedJson = new JSONObject();
        generatedJson.put("action", "createScheme");

        JSONObject scheme = new JSONObject();
        scheme.put("name", nameField);

        JSONArray nameArray = new JSONArray();
        JSONArray typeArray = new JSONArray();
        JSONArray sizeArray = new JSONArray();

        for (int row = 1; row < container.getRowCount(); row++) {
            TextField nameWidget = (TextField) container.getChildren().get(row * 5);
            String typeFlag = (String) nameWidget.getUserData();

            if (!nameWidget.getText().isBlank()) {
                nameArray.put(nameWidget.getText()); //Nombre del atributo
            } else {
                return false;
            }

            if (typeFlag.equals("normal")) {
                @SuppressWarnings("unchecked")
                ComboBox<String> attTypeC = (ComboBox<String>) container.getChildren().get(row * 5 + 1);
                typeArray.put(attTypeC.getSelectionModel().getSelectedItem()); //Tipo del atributo

                @SuppressWarnings("unchecked")
                Spinner<Integer> attSize = (Spinner<Integer>) container.getChildren().get(row * 5 + 2);
                sizeArray.put(attSize.getValue()); //Tamaño del atributo

            } else {
                typeArray.put("join");

                @SuppressWarnings("unchecked")
                ComboBox<String> schemeToJoin = (ComboBox<String>) container.getChildren().get(row * 5 + 2);
                sizeArray.put(schemeToJoin.getSelectionModel().getSelectedItem()); // Esquema con el cual hacer join

            }
        }

        boolean coincidences = false;

        for (int i = 0; i < nameArray.length(); i++) {
            for (int j = i + 1; j < nameArray.length(); j++) {
                if (nameArray.getString(i).equals(nameArray.getString(j))) {
                    coincidences = true;
                }
            }
        }

        scheme.put("name", nameField);
        scheme.put("attrName", nameArray);
        scheme.put("attrType", typeArray);
        scheme.put("attrSize", sizeArray);
        scheme.put("primaryKey", nameArray.getString(((Integer) primaryKeyGroup.getSelectedToggle().getUserData()) - 1));

        generatedJson.put("scheme", scheme);
//        System.out.println("Json generated succesfully");

        return !coincidences;
    }

    private Image loadImg(String relativePath) {
        String cwd = System.getProperty("user.dir");
        return new Image("file://" + cwd + "/" + relativePath);
    }

    private void setupGridpane(GridPane pane) {
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(20);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(25);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPercentWidth(20);
        ColumnConstraints column5 = new ColumnConstraints();
        column5.setPercentWidth(5);
        pane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(50);
        pane.getRowConstraints().add(row1);

        pane.setVgap(10);
        pane.setPadding(new Insets(10));
        GridPane.setHgrow(pane, Priority.ALWAYS);
        GridPane.setVgrow(pane, Priority.ALWAYS);
    }

    public JSONObject show() {
        launch(NewScheme.class);
        return generatedJson;

    }

}

package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Set;

public class GUI extends Application{
    private Controller controller;

    private BorderPane mainLayout;
    private VBox schemesList;
    private VBox indexList;
    private TableView<JSONArray> schemeDataTable;
    private Label actualSchemeName;
    private VBox schemeDataContainer;

    private Thread messageThread;

    public void start(Stage stage) {
        this.controller = Controller.getInstance();
        this.controller.setMainGui(this);

        //Contenedor principal de la interfaz
        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(5));

        //Contenedor lateral (Esquemas e índices)
        VBox sidePanel = new VBox();
        sidePanel.setMinWidth(250);
        sidePanel.setPadding(new Insets(5));
        sidePanel.setSpacing(10);
        sidePanel.setAlignment(Pos.TOP_CENTER);

        //Contenedor de los esquemas
        VBox schemesContainer = new VBox();
        schemesContainer.setSpacing(5);
        schemesContainer.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(schemesContainer, Priority.ALWAYS);
        //Header de la lista de esquemas
        HBox schemeHeader = new HBox();
        Label schemesTitle = new Label("Esquemas");
        schemesTitle.setStyle("-fx-font-size: 16px;");
        HBox addSchemeContainer = new HBox();
        addSchemeContainer.setPadding(new Insets(0, 5, 0, 0));
        addSchemeContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(addSchemeContainer, Priority.ALWAYS);
        ImageView addScheme = new ImageView(loadImg("res/images/plus.png"));
        addScheme.setFitHeight(25);
        addScheme.setFitWidth(25);
        addScheme.setOnMouseClicked(mouseEvent -> {
            System.out.println("Add scheme..");
            this.controller.generateScheme();
        });
        addSchemeContainer.getChildren().add(addScheme);
        schemeHeader.getChildren().addAll(schemesTitle, addSchemeContainer);
        //Lista de esquemas
        ScrollPane schemesWrapper = new ScrollPane();
        schemesWrapper.setFitToHeight(true);
        schemesWrapper.setFitToWidth(true);
        VBox.setVgrow(schemesWrapper, Priority.ALWAYS);
        schemesList = new VBox();
        schemesList.setPadding(new Insets(5));
        schemesWrapper.setContent(schemesList);
        VBox.setVgrow(schemesList, Priority.ALWAYS);
        schemesContainer.getChildren().addAll(schemeHeader, schemesWrapper);

        //Contenedor de índices
        VBox indexContainer = new VBox();
        indexContainer.setSpacing(5);
        indexContainer.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(indexContainer, Priority.ALWAYS);
        //Header de la lista de índices
        HBox indexHeader = new HBox();
        Label indexTitle = new Label("Índices");
        indexTitle.setStyle("-fx-font-size: 16px;");
        HBox addIndexContainer = new HBox();
        addIndexContainer.setPadding(new Insets(0, 5, 0, 0));
        addIndexContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(addIndexContainer, Priority.ALWAYS);
        ImageView addIndex = new ImageView(loadImg("res/images/plus.png"));
        addIndex.setFitHeight(25);
        addIndex.setFitWidth(25);
        addIndex.setOnMouseClicked(mouseEvent -> {
            System.out.println("Add index..");
            if (!getSelectedSchemeName().equals("Selecciona un esquema")) {
                controller.newIndex();
            } else {
                showAlert("Por favor seleccione un esquema", Alert.AlertType.INFORMATION);
            }
        });
        addIndexContainer.getChildren().add(addIndex);
        indexHeader.getChildren().addAll(indexTitle, addIndexContainer);
        //Lista de índices
        ScrollPane indexWrapper = new ScrollPane();
        indexWrapper.setFitToWidth(true);
        indexWrapper.setFitToHeight(true);
        VBox.setVgrow(indexWrapper, Priority.ALWAYS);
        indexList = new VBox();
        indexList.setPadding(new Insets(5));
        VBox.setVgrow(indexList, Priority.ALWAYS);
        indexWrapper.setContent(indexList);
        indexContainer.getChildren().addAll(indexHeader, indexWrapper);

        sidePanel.getChildren().addAll(schemesContainer, indexContainer);

        //Contenedor principal de contenido
        ScrollPane mainWrapper = new ScrollPane();
        mainWrapper.setFitToHeight(true);
        mainWrapper.setFitToWidth(true);
        VBox.setVgrow(mainWrapper, Priority.ALWAYS);
        HBox.setHgrow(mainWrapper, Priority.ALWAYS);
        //Stack del contenido
        StackPane mainStack = new StackPane();
        VBox.setVgrow(mainStack, Priority.ALWAYS);
        HBox.setHgrow(mainStack, Priority.ALWAYS);
        mainWrapper.setContent(mainStack);

        //Contendor del contenido de esquemas
        schemeDataContainer = new VBox();
        VBox.setVgrow(schemeDataContainer, Priority.ALWAYS);
        HBox.setHgrow(schemeDataContainer, Priority.ALWAYS);
        //Header de los esquemas
        HBox schemeDataHeader = new HBox();
        schemeDataHeader.setMinHeight(40);
        schemeDataHeader.setPadding(new Insets(10));
        actualSchemeName = new Label("Selecciona un esquema");
        actualSchemeName.setStyle("-fx-font-size: 18px;");
        //Contenedor de los botones de buscar y añadir registros
        HBox headerActions = new HBox();
        headerActions.setAlignment(Pos.CENTER_RIGHT);
        headerActions.setSpacing(10);
        HBox.setHgrow(headerActions, Priority.ALWAYS);
        ImageView deleteRecords = new ImageView(loadImg("res/images/delete2.png"));
        deleteRecords.setFitWidth(26);
        deleteRecords.setFitHeight(26);
        deleteRecords.setOnMouseClicked(mouseEvent -> {
            System.out.println("Delete selected records of " + actualSchemeName.getText());
            deleteSelectedRecords();

        });
        ImageView addRegisterButton = new ImageView(loadImg("res/images/plus.png"));
        addRegisterButton.setFitWidth(28);
        addRegisterButton.setFitHeight(28);
        addRegisterButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("Add register in " + actualSchemeName.getText());
            controller.insertData();

        });
        ImageView searchButton =  new ImageView(loadImg("res/images/search.png"));
        searchButton.setFitWidth(28);
        searchButton.setFitHeight(28);
        searchButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("Search in scheme " + actualSchemeName.getText());
            controller.querySchemeCollection();
        });
        headerActions.getChildren().addAll(deleteRecords, addRegisterButton, searchButton);
        schemeDataHeader.getChildren().addAll(actualSchemeName, headerActions);
        //Tabla de datos del esquema
        schemeDataTable = new TableView<>();
        schemeDataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        schemeDataTable.setPlaceholder(new Label("No hay datos en la tabla"));
        VBox.setVgrow(schemeDataTable, Priority.ALWAYS);
        HBox.setHgrow(schemeDataTable, Priority.ALWAYS);
        schemeDataContainer.getChildren().addAll(schemeDataHeader, schemeDataTable);

        VBox defaultContentContainer = new VBox();
        defaultContentContainer.setAlignment(Pos.CENTER);
        defaultContentContainer.setStyle("-fx-background-color: white;");
        HBox.setHgrow(defaultContentContainer, Priority.ALWAYS);
        VBox.setVgrow(defaultContentContainer, Priority.ALWAYS);
        Label defaultContentText = new Label("Selecciona un esquema para ver sus datos");
        defaultContentText.setStyle("-fx-font-size: 18px;");
        defaultContentContainer.getChildren().add(defaultContentText);

        mainStack.getChildren().addAll(schemeDataContainer, defaultContentContainer);


        mainLayout.setLeft(sidePanel);
        mainLayout.setCenter(mainWrapper);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setTitle("In Memory DataBase");
        stage.setScene(scene);
        stage.show();

        controller.getUpdatedData();
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

    private void deleteSelectedRecords() {
        ObservableList<JSONArray> recordsToDelete = schemeDataTable.getSelectionModel().getSelectedItems();
        if (recordsToDelete.size() > 0) {

            JSONArray primaryKeys = new JSONArray();

            JSONObject scheme = this.controller.getSelectedScheme();
            JSONArray attrName = scheme.getJSONArray("attrName");

            String pk = scheme.getString("primaryKey");
            int pkIndex = 0;

            for (int i=0; i<attrName.length(); i++) {
                if (attrName.getString(i).equals(pk)) {
                    pkIndex = i;
                }
            }

            for (JSONArray registry: recordsToDelete) {
                primaryKeys.put(registry.getString(pkIndex));
            }

            this.controller.deleteRecords(primaryKeys);

        } else {
            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION, "Primero selecciona los registros a eliminar", ButtonType.OK);
            alert.setHeaderText(null);
            alert.show();
        }
    }

    /**
     * Éste método recibe el hashtable con los esquemas desde el servidor y los muestra en la
     * lista de la interfaz
     * @param schemes HashTable de esquemas.
     */
    public void loadSchemesList(Hashtable<String, String> schemes) {
        schemesList.getChildren().clear();

        for (String schemeName: schemes.keySet()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);

            ContextMenu menu = new ContextMenu();
            MenuItem itemDelete = new MenuItem("Eliminar");
            //todo actualizar la ventana principal para que no aparezca la informacion del esquema
            itemDelete.setOnAction(actionEvent -> deleteScheme(schemeName));
            menu.getItems().addAll(itemDelete);

            row.setOnMouseEntered(mouseEvent -> {
                row.setStyle("-fx-background-color: #dbdbdb;");
            });
            row.setOnMouseExited(mouseEvent -> {
                row.setStyle("-fx-background-color: transparent;");
            });
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    if (!menu.isShowing()) {
                        menu.show(row, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    } else {
                        menu.hide();
                    }
                } else {
                    schemeDataContainer.toFront();
                    System.out.println("Consultando datos del esquema");
                    querySchemeData(schemeName);
                    //todo mostrar indices relacionados

                    System.out.println(controller.getListOfIndex());

                    if (controller.getListOfIndex().length()>0)
                        loadIndexList(controller.getListOfIndex(), schemeName);
                }
            });

            Label schemeLabel = new Label(schemeName);
            row.getChildren().add(schemeLabel);

            Platform.runLater(() -> schemesList.getChildren().addAll(new Separator(), row));

            }
    }


    private void loadIndexList(JSONObject indexArray, String schemeName) {
        indexList.getChildren().clear();
        Set<String> keySet = indexArray.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                System.out.println("key " + key + " schemeName " + schemeName);
                System.out.println("key.equals(schemeName) " + key.equals(schemeName));
                if (key.equals(schemeName)){
                    JSONArray actualIndexList = indexArray.getJSONArray(key);

                    for (int i=0; i<actualIndexList.length(); i++){

                        JSONArray innerList = actualIndexList.getJSONArray(i);

                        String indexName = innerList.getString(0);
                        System.out.println("adding " + indexName);
                        HBox row = new HBox();
                        row.setAlignment(Pos.CENTER_LEFT);

                        ContextMenu menu = new ContextMenu();
                        MenuItem itemDelete = new MenuItem("Eliminar");
                        itemDelete.setOnAction(actionEvent -> {
                            System.out.println("Eliminando indice");
                            deleteIndex(indexName, schemeName);
                            loadIndexList(controller.getListOfIndex(), schemeName);
                        });
                        menu.getItems().addAll(itemDelete);

                        row.setOnMouseClicked(mouseEvent -> {
                            if (!menu.isShowing()) {
                                menu.show(row, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                            } else {
                                menu.hide();
                            }
                        });

                        row.setOnMouseEntered(mouseEvent -> {
                            row.setStyle("-fx-background-color: #dbdbdb;");
                        });
                        row.setOnMouseExited(mouseEvent -> {
                            row.setStyle("-fx-background-color: transparent;");
                        });

                        Label indexLabel = new Label(indexName);
                        row.getChildren().add(indexLabel);

                        Platform.runLater(() -> indexList.getChildren().addAll(new Separator(), row));


                    }
                    break;
                }
            }
        }






    }

    private void deleteIndex(String indexName, String schemeName) {
        System.out.println("Eliminando indice " + indexName + " en esquema " + schemeName);
        controller.deleteIndex(indexName, schemeName);
    }

    private void deleteScheme(String schemeName) {
        System.out.println("Eliminar esquema " + schemeName);
        controller.deleteScheme(schemeName);
    }

    private void querySchemeData(String schemeName) {
        controller.querySchemeData(schemeName);

        actualSchemeName.setText(schemeName);
    }

    public void loadSchemeTableData(JSONObject scheme, Hashtable<String, JSONArray> collection) {

        System.out.println("Cargando columnas");

        schemeDataTable.getColumns().clear();

        if (collection == null) {
            collection = new Hashtable<>();
        }

        // [cedula, nombre, edad]
        JSONArray attributes = scheme.getJSONArray("attrName");

        // [[402390083, Paola, 20], [122200589521, Marlon, 20]]
        ObservableList<JSONArray> items = FXCollections.observableArrayList(collection.values());

        for (int i=0; i<attributes.length(); i++) {
            TableColumn<JSONArray, String> column = new TableColumn(attributes.getString(i));
            int finalI = i;
            column.setCellValueFactory(p -> {
                return new SimpleStringProperty(p.getValue().getString(finalI));
            });
            column.setPrefWidth(150);
            schemeDataTable.getColumns().add(column);
        }

        schemeDataTable.setItems(items);
    }

    public void showQueryData(JSONArray queryData) {
        schemeDataTable.getItems().clear();

        ObservableList<JSONArray> dataItems = FXCollections.observableArrayList();

        for (int i=0; i<queryData.length(); i++) {
            schemeDataTable.getItems().add(new JSONArray(queryData.getString(i)));
        }
    }

    public String getSelectedSchemeName() {
        return actualSchemeName.getText();
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

    public void showMessage(String message) {

        HBox messageContainer = new HBox();
        messageContainer.setPrefHeight(0);
        messageContainer.setStyle("-fx-background-color: #3C3C3C;");
        messageContainer.setAlignment(Pos.CENTER_LEFT);

        Label messageLabel = new Label(message);
        messageLabel.setTextFill(Color.WHITE);
//        messageLabel.setStyle("-fx-font-weight: bold;");
        messageLabel.setPadding(new Insets(0, 10, 0, 10));

        mainLayout.setBottom(messageContainer);

        messageThread = new Thread(() -> {
            try {
                //Expandir el mensaje
                double size = 0;
                for (double i = 0; i < 25; i++) {
                    Thread.sleep(25);
                    size += 1;
                    double finalSize = size;
                    Platform.runLater(() -> messageContainer.setPrefHeight(finalSize));

                }

                Platform.runLater(() -> messageContainer.getChildren().add(messageLabel));
                Thread.sleep(5000);
                Platform.runLater(() -> messageContainer.getChildren().clear());

                //Contraer el mensaje
                for (double i = 25; i > 0; i--) {
                    Thread.sleep(25);
                    size -= 1;
                    double finalSize = size;
                    Platform.runLater(() -> messageContainer.setPrefHeight(finalSize));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> mainLayout.setBottom(null));
        });
        messageThread.setDaemon(true);
        messageThread.start();
    }

    public void show() {
        launch(GUI.class);
    }
}

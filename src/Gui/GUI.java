package Gui;

import Logic.Controller;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;

public class GUI extends Application{
    private Controller controller;

    private BorderPane mainLayout;
    private VBox schemesList;
    private VBox indexList;
    private TableView schemeDataTable;
    private Label actualSchemeName;

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
        VBox schemeDataContainer = new VBox();
        VBox.setVgrow(schemeDataContainer, Priority.ALWAYS);
        HBox.setHgrow(schemeDataContainer, Priority.ALWAYS);
        //Header de los esquemas
        HBox schemeDataHeader = new HBox();
        schemeDataHeader.setMinHeight(40);
        schemeDataHeader.setPadding(new Insets(10));
        actualSchemeName = new Label("Selecciona un esquema");
        actualSchemeName.setStyle("-fx-font-size: 18px;");
        //TODO agregar botón para agregar y buscar datos.
        schemeDataHeader.getChildren().add(actualSchemeName);
        //Tabla de datos del esquema
        schemeDataTable = new TableView();
//        schemeDataTable.getColumns().add(new TableColumn<>("Columna 1"));
        VBox.setVgrow(schemeDataTable, Priority.ALWAYS);
        HBox.setHgrow(schemeDataTable, Priority.ALWAYS);
        schemeDataContainer.getChildren().addAll(schemeDataHeader, schemeDataTable);

        mainStack.getChildren().addAll(schemeDataContainer);


        mainLayout.setLeft(sidePanel);
        mainLayout.setCenter(mainWrapper);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setTitle("In Memory DataBase");
        stage.setScene(scene);
        stage.show();

        controller.querySchemes();
    }

    /**
     * Éste método recibe el hashtable con los esquemas desde el servidor y los muestra en la
     * lista de la interfaz
     * @param schemes HashTable de esquemas.
     */
    public void loadSchemesList(Hashtable<String, String> schemes) {
        schemesList.getChildren().clear();
        boolean sep = false;

        for (String schemeName: schemes.keySet()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);

            ContextMenu menu = new ContextMenu();
            MenuItem itemDelete = new MenuItem("Eliminar");
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
                    querySchemeData(schemeName);
                }
            });

            Label schemeLabel = new Label(schemeName);
            row.getChildren().add(schemeLabel);

            if (!sep) {
                Platform.runLater(() -> schemesList.getChildren().add(row));
                sep = true;
            } else {
                Platform.runLater(() -> schemesList.getChildren().addAll(new Separator(), row));
                sep = false;
            }
        }
    }

    private void deleteScheme(String schemeName) {
        //TODO mandar solicitud al servidor para eliminar esquema
        System.out.println("Eliminar esquema " + schemeName);
    }

    private void querySchemeData(String schemeName) {
        controller.querySchemeData(schemeName);

        actualSchemeName.setText(schemeName);
    }

    public void loadSchemeTableColumns(JSONObject scheme) {

        System.out.println("Cargando columnas");

        schemeDataTable.getColumns().clear();

        JSONArray attributes = scheme.getJSONArray("attrName");

        for (int i=0; i<attributes.length(); i++) {
            TableColumn column = new TableColumn(attributes.getString(i));
            column.setMinWidth(50);
            schemeDataTable.getColumns().add(column);

        }

    }

    public void loadDataToTable(Hashtable<String, JSONObject> collection) {
        //TODO meter los datos a las columnas de la tabla
        System.out.println("Añadiendo datos");

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
        messageContainer.setStyle("-fx-background-color: #dbdbdb;");
        messageContainer.setAlignment(Pos.CENTER_LEFT);

        Label messageLabel = new Label(message);
        messageLabel.setPadding(new Insets(0, 10, 0, 10));

        mainLayout.setBottom(messageContainer);

        Thread messageThread = new Thread(() -> {
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
                for (double i=25; i>0; i--) {
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

    public void addIndexTitle(VBox index, String name) {
        HBox indextitle = new HBox();
        indextitle.setSpacing(30);
        Label title = new Label(name);
        ContextMenu indexMenu = new ContextMenu();
        Button edit = new Button("Editar");
        edit.setContextMenu(indexMenu);
        MenuItem editIndex = new MenuItem("Editar Índice...");
        editIndex.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Editar índice");
            }
        });
        MenuItem deleteIndex = new MenuItem("Eliminar Índice");
        deleteIndex.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Se elimina el índice");
            }
        });


        indexMenu.getItems().addAll(editIndex, deleteIndex);
        indextitle.getChildren().addAll(title, edit);
        index.getChildren().addAll(indextitle);

    }

    /**
     * En este método es donde se va a crear la Tabla, se llama al método de las configuraciones y se llama al método que inserta los datos
     *
     * @return
     */
    private TableView createTable() {
        //Se crea la tabla para visualizar los datos
        TableView data = new TableView();
        setTableappearance(data);
        addData(data);
        return data;

    }

    private GridPane setGridData() {
        GridPane data = new GridPane();
        data.setHgap(6);
        data.setVgap(8);
        data.setPadding(new Insets(10, 10, 10, 10));
        data.setGridLinesVisible(true);
        return data;

    }

    private GridPane addToGridData(GridPane data) {//, JSONObject json){
//        int column = json.getJSONArray("attr").length();
//        Se añaden los títulos de las columnas
//        for (int i = 0; i <= column -1; i++){
//            TextField title = new TextField(json.getJSONArray("atrr").getString(i));
//            data.addColumn(i, title);
//        }
//        TextField buttontitle = new TextField("");
//        data.addColumn(column, buttontitle);
//        Button b = new Button("ve");
//        data.add(b,1,0);
        return data;
    }

    /**
     * Este método se encarga de las configuraciones de la tabla para visualizar los datos
     *
     * @param data data
     */
    private void setTableappearance(TableView data) {
        data.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        data.setPrefWidth(600);
        data.setPrefHeight(600);

    }

    /**
     * Este método se va a encargar de llenar con los datos necesarios la tabla
     *
     * @param data data
     * @return
     */
    public TableView addData(TableView data) {
        TableColumn<Button, String> column1 = new TableColumn("Boton");
//        TableColumn<Scheme, String> column2 = new TableColumn("Nombre");
//
//        column2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
//
//        data.getColumns().addAll(column1, column2);
//        data.getItems().add(new Scheme());
        return data;
    }

    public void show() {
        launch(GUI.class);
    }
}

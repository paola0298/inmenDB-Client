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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.Hashtable;

public class GUI extends Application{
    private Controller controller;

    private VBox scheme;
    private StackPane mainWindowLayout;
    private GridPane grid;
    private Button newscheme;
    private HBox titlebutton;
    private HBox newSchemeContainer;
    private VBox mainSpace;
    public ScrollPane schemeArea;
    private ScrollPane dataArea;
    private ScrollPane indexArea;

    private BorderPane mainLayout;
    private VBox schemesList;
    private VBox indexList;

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
        mainWrapper.setContent(mainStack);

        //Contendor del contenido de esquemas
        VBox schemeDataContainer = new VBox();
        HBox schemeDataHeader = new HBox();



//        scheme = new VBox();
//        mainWindowLayout = new StackPane();
//        grid = new GridPane();
//        newscheme = new Button();
//        titlebutton = new HBox();
//        mainSpace = new VBox();
//        schemeArea = new ScrollPane();
//        dataArea = new ScrollPane();
//        indexArea = new ScrollPane();

//        Panel de la pantalla principal, aquí se van a agregar los componentes de esta pantalla
//        addtoMaingrid();

//        Scene scene = new Scene(mainWindowLayout, 1280, 900);

        mainLayout.setLeft(sidePanel);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setTitle("In Memory DataBase");
        stage.setScene(scene);

        stage.show();
    }

    /**
     * Éste método recibe el hashtable con los esquemas desde el servidor y los muestra en la
     * lista de la interfaz
     * @param schemes HashTable de esquemas.
     */
    public void loadSchemesList(Hashtable<String, JSONObject> schemes) {
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
                    queryScheme(schemeName);
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

    private void queryScheme(String schemeName) {
        //TODO obtener datos del esquema desde el servidor
        System.out.println("Mostrar datos de esquema " + schemeName);
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
                    Thread.sleep(50);
                    size += 1;
                    double finalSize = size;
                    Platform.runLater(() -> messageContainer.setPrefHeight(finalSize));

                }

                Platform.runLater(() -> messageContainer.getChildren().add(messageLabel));
                Thread.sleep(3000);
                Platform.runLater(() -> messageContainer.getChildren().clear());

                //Contraer el mensaje
                for (double i=25; i>0; i--) {
                    Thread.sleep(50);
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

    /**
     * En este método se van a agregar los paneles correspondientes
     * al gridpane correspondiente a la pantalla principal
     * @author Brayan Rodríguez
     */
    public void addtoMaingrid(){
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(10,10,10,10));
        addtoSchemeVBox(grid);

        addIndexSpace(grid);

        mainWindowLayout.getChildren().addAll(grid);

    }

    /**
     * En este método se maneja el cuadro donde se muestran los esquemas actuales,
     * se puede accesar a la pantalla para construir un nuevo esquema
     *
     * @param grid
     * @author Brayan Rodríguez
     */
    public void addtoSchemeVBox(GridPane grid){
        //Este VBox es donde se van a agregar las partes necesarias para el área destinada a los esquemas

        scheme.setPadding(new Insets(10,10,10,10));
        scheme.setSpacing(30);
        scheme.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(142,255,185,0.5);";
        scheme.setStyle(style);

        // Se crea un HBox para poner el título y el botón horizontalmente
        Label schemetitle = new Label("Esquemas");
        newscheme.setText("Nuevo Esquema"); //Botón para agregar un nuevo esquema
        newscheme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Aquí se van a agregar las funcionalidades del botón para crear un nuevo esquema
                //Se debe accesar a la pantalla de crear un nuevo esquema
                System.out.println("Nuevo esquema");
            }
        });


        titlebutton.setSpacing(15);
        titlebutton.getChildren().addAll(schemetitle,newscheme);
        //Aquí se agregan los componentes al VBox
        scheme.getChildren().addAll(titlebutton);
        //Ejemplo de como agregar un nuevo esquema al área destinada a mostrar los esquemas actuales
        addSchemeTitle(scheme, "hola");
        addSchemeTitle(scheme, "holaw");
        schemeArea.setContent(scheme);
        schemeArea.setPannable(true);
        //Se agrega el ScrollPane al GridPane
        grid.add(schemeArea, 0, 0);
    }

    /**
     * Este método se llama cuando se desea agregar un nuevo esquema, se va a llamar como <addSchemeTitle(scheme, "nombre del esquema");>"
     * @param scheme
     * @param name
     */
    public void addSchemeTitle(VBox scheme, String name) {
        Label schemetitle = new Label(name);
        Button edition = new Button("Editar");
        ContextMenu edit = new ContextMenu();
        MenuItem showScheme = new MenuItem("Mostrar datos...");
        showScheme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addVisualizationDataSpace(grid, name);
            }
        });

        MenuItem editScheme = new MenuItem("Editar Esquema...");
        editScheme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Aquí se debe llamar a la pantalla para editar los esquemas
                System.out.println("Editar un esquema");
            }
        });
        MenuItem deleteScheme = new MenuItem("Eliminar Esquema");
        deleteScheme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Aquí se agrega al método para eliminar esquemas
                System.out.println("Eliminar un esquema");
            }
        });
        edit.getItems().addAll(showScheme, editScheme, deleteScheme);
        edition.setContextMenu(edit);


        newSchemeContainer = new HBox(schemetitle, edition);

        newSchemeContainer.setSpacing(60);

        scheme.getChildren().addAll(newSchemeContainer);
    }

    public void addIndexSpace(GridPane grid) {
        VBox index = new VBox();
        index.setPadding(new Insets(10, 10, 10, 10));
        index.setSpacing(30);
        index.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(89,122,255,0.5);";
        index.setStyle(style);

        HBox title = new HBox();
        title.setSpacing(30);
        Label indextitle = new Label("Índices");
        Button addIndex = new Button("Nuevo Índice...");
        addIndex.setOnAction(actionEvent -> {
            System.out.println("Añadir índice...");
            //Se debe hacer este llamado cuando se termine de la pantalla para crear el índice
            addIndexTitle(index, "arbol");
        });
        title.getChildren().addAll(indextitle, addIndex);
        index.getChildren().addAll(title);
        indexArea.setContent(index);
        grid.add(indexArea, 0, 1);
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

    /**
     * FALTAN MODIFICACIONES
     * Este método funciona para visualizar los datos de un esquema específico
     *
     * @param grid
     * @param schemeName
     */
    public void addVisualizationDataSpace(GridPane grid, String schemeName) {
        //************Falta añadir que reciba un objeto esquema para que pueda tomarse el nombre y
        // actualizar valores de ese esquema

        //En este VBox se agrega el título del esquema y la tabla para visualizar los datos
        VBox mainSpace = new VBox();
        mainSpace.setPadding(new Insets(10, 10, 10, 10));
        mainSpace.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(255,233,105,0.54);";
        mainSpace.setStyle(style);


        //Titulo del esquema y botón para agregar datos
        Label title = new Label(schemeName);
        Button addData = new Button("Añadir Dato...");

        addData.setOnAction(actionEvent -> {
            System.out.println("Añadir dato");
            mainSpace.getChildren().add(setGridData());

        });


        HBox titleData = new HBox();
        titleData.setSpacing(800);
        titleData.getChildren().addAll(title, addData);

        mainSpace.getChildren().add(0, titleData);//, addToGridData(grid));//, addToGridData(grid));
        mainSpace.getChildren().add(1, setGridData());

        dataArea.setContent(mainSpace);
        dataArea.setPannable(true);
        //Prueba de como insertar con el formato gridName.add(widget,column,row,columnspan,rowspan)
        grid.add(dataArea, 1, 0, 40, 20);


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

package Gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;

public class GUI extends Application{
    private VBox scheme = new VBox();
    private StackPane mainWindowLayout = new StackPane();
    private GridPane grid = new GridPane();
    private Button newscheme = new Button();
    private HBox titlebutton = new HBox();
    private HBox newSchemeContainer;
    private VBox mainSpace = new VBox();
    public ScrollPane schemeArea = new ScrollPane();
    private ScrollPane dataArea = new ScrollPane();
    private ScrollPane indexArea = new ScrollPane();


    public void start(Stage stage) {
        stage.setTitle("In Memory DataBase");

        /**Panel de la pantalla principal, aquí se van a
         * agregar los componentes de esta pantalla
         * @author Brayan Rodríguez
         */

        addtoMaingrid(mainWindowLayout);

        Scene scene = new Scene(mainWindowLayout, 1280, 900);
        stage.setMinWidth(640);
        stage.setMinHeight(480);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * En este método se van a agregar los paneles correspondientes
     * al gridpane correspondiente a la pantalla principal
     * @author Brayan Rodríguez
     */
    public void addtoMaingrid(StackPane mainWindowLayout){
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
        addIndex.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Añadir índice...");
                //Se debe hacer este llamado cuando se termine de la pantalla para crear el índice
                addIndexTitle(index, "arbol");


            }
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
     * FALTAN MODIFICACIONES
     * Este método funciona para visualizar los datos de un esquema específico
     *
     * @param grid
     * @param schemeName
     */
    public void addVisualizationDataSpace(GridPane grid, String schemeName) { //************Falta añadir que reciba un objeto esquema para que pueda tomarse el nombre y actualizar valores de ese esquema


        //En este VBox se agrega el título del esquema y la tabla para visualizar los datos
        VBox mainSpace = new VBox();
        mainSpace.setPadding(new Insets(10, 10, 10, 10));
        mainSpace.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(255,233,105,0.54);";
        mainSpace.setStyle(style);


        //Titulo del esquema y botón para agregar datos
        Label title = new Label(schemeName);
        Button addData = new Button("Añadir Dato...");


        addData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Añadir dato");
                mainSpace.getChildren().add(setGridData());

            }
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
}

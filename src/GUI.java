import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application{
    private VBox scheme = new VBox();
    private StackPane mainWindowLayout = new StackPane();
    private GridPane grid = new GridPane();
    private Button newscheme = new Button();
    private HBox titlebutton = new HBox();
    private HBox newSchemeContainer;




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
        mainWindowLayout.getChildren().addAll(grid);

    }
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

        //Se agrega el VBox al GridPane
        grid.add(scheme,0,0);
    }

    public void addSchemeTitle(VBox scheme, String name) {
        Label schemetitle = new Label(name);
        Button edition = new Button("Editar");
        ContextMenu edit = new ContextMenu();
        MenuItem editScheme = new MenuItem("Editar Esquema...");
        editScheme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Editar un esquema");
            }
        });
        MenuItem deleteScheme = new MenuItem("Eliminar Esquema");
        deleteScheme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Eliminar un esquema");
            }
        });
        edit.getItems().addAll(editScheme, deleteScheme);
        edition.setContextMenu(edit);


        //edit.setText("Edit");
        newSchemeContainer = new HBox(schemetitle, edition);

        newSchemeContainer.setSpacing(60);
        //newSchemeContainer.getChildren().addAll(schemetitle,edit);
        scheme.getChildren().addAll(newSchemeContainer);



    }

//    @Override
//    /**
//     * En esta clase se manejarán las acciones por hacer de los botones
//     * @author Brayan Rodríguez Villalobos
//     */
//    public void handle(ActionEvent actionEvent) {
//        if (actionEvent.getSource()==newscheme){
//            //Abrir la ventana para crear un nuevo esquema
//            //Actualizar el espacio donde se muestran los esquemas ya hechos
//            System.out.println("Nueva pantalla");
//        }//else if (actionEvent.getSource()){
////
////
////        }
//    }
}

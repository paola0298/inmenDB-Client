import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GUI extends Application{
    VBox scheme = new VBox();





    public void start(Stage stage){


        stage.setTitle("In Memory Data Base");

        /**Panel de la pantalla principal, aquí se van a
         * agregar los componentes de esta pantalla
         * @author Brayan Rodríguez
         */
        StackPane mainWindowLayout = new StackPane();
        addtoMaingrid(mainWindowLayout);






        Scene scene = new Scene(mainWindowLayout,1280,900);
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
        GridPane grid = new GridPane();
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
        Button newscheme = new Button("New scheme");
        HBox titlebutton = new HBox();
        titlebutton.setSpacing(15);
        titlebutton.getChildren().addAll(schemetitle,newscheme);



        //Aquí se agregan los componentes al VBox
        scheme.getChildren().addAll(titlebutton);


        //Ejemplo de como agregar un nuevo esquema al área destinada a mostrar los esquemas actuales
        addNewSchemeTitle(scheme, "hola");
        addNewSchemeTitle(scheme, "hola");



        //Se agrega el VBox al GridPane
        grid.add(scheme,0,0);







    }
    public void addNewSchemeTitle(VBox scheme, String name){
        Label schemetitle = new Label(name);
        Button edit = new Button("Edit");
        HBox newSchemeContainer = new HBox();
        newSchemeContainer.setSpacing(60);
        newSchemeContainer.getChildren().addAll(schemetitle,edit);
        scheme.getChildren().addAll(newSchemeContainer);



    }

}

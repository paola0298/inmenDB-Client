import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GUI extends Application{





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
        VBox scheme = new VBox();

        scheme.setPadding(new Insets(10,10,10,10));
        scheme.setSpacing(30);
        scheme.setBackground(Background.EMPTY);
        String style = "-fx-background-color: rgba(142,255,185,0.5);";
        scheme.setStyle(style);


        Label schemetitle = new Label("Esquemas");
        Button newscheme = new Button("New scheme");
        HBox titlebutton = new HBox();
        titlebutton.setSpacing(15);
        titlebutton.getChildren().addAll(schemetitle,newscheme);


        scheme.getChildren().addAll(titlebutton);



        grid.add(scheme,0,0);







    }


}

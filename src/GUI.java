import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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






        Scene scene = new Scene(mainWindowLayout,800,600);
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
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(0, 10, 0, 10));


        addtoSchemeVBox(grid);
        mainWindowLayout.getChildren().addAll(grid);

    }
    public void addtoSchemeVBox(GridPane grid){
        VBox newscheme = new VBox();
        newscheme.setPadding(new Insets(10));
        newscheme.setSpacing(8);
        newscheme.setBackground(Background.EMPTY);
        //String style = "-fx-background-color: rgba(255, 0, 255, 0.5);";
        //newscheme.setStyle(style);


        //newscheme.getChildren().addAll(new Button("Nuevo Esquema"));







        grid.add(newscheme,1,0);


    }


}

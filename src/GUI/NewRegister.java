package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
public class NewRegister {

    @FXML
    private Button buttonEnter;

    @FXML
    private ScrollPane scrollPaneRegister;

    @FXML
    private AnchorPane anchorPaneRegister;

    @FXML
    private SplitPane splitPaneRegister;

    @FXML
    private TextField nameTextFIeld;

    @FXML
    private TextField sizeTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private TextField primaryKeyTextFIeld;

    private Label labelTest;

    public void createLabel(String setText){
        Label labelCreated = new Label("setText");
        labelCreated.setMaxSize(80,50);
        labelCreated.setAlignment(Pos.TOP_CENTER);
        labelCreated.setText(setText);
        labelCreated.setFont(Font.font("Verdana",20));



    }

    @FXML
    void boton(ActionEvent event) {
        System.out.println("Button enter was pressed");

    }


    @FXML
    void initialize() {

    }
}
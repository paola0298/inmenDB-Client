package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
public class NewRegister  extends Pane {

    private String []infoEnviar;

    @FXML
    private Label labelAtribute;

    @FXML
    private Button buttonEnter;

    @FXML
    private ScrollPane scrollPaneRegister;

    @FXML
    private AnchorPane anchorPaneRegister;

    @FXML
    private SplitPane splitPaneRegister;

    @FXML
    private Pane dynamicLabelPane;

    @FXML
    private Pane textLabelPane;

    @FXML
    private TextField nameTextFIeld;

    @FXML
    private TextField sizeTextField;

    @FXML
    private TextField typeTextField;

    @FXML
    private TextField primaryKeyTextFIeld;

    private int actualValueArray;



    public Button getButtonEnter() {
        return buttonEnter;
    }

    public void setButtonEnter(Button buttonEnter) {
        this.buttonEnter = buttonEnter;
    }

    public ScrollPane getScrollPaneRegister() {
        return scrollPaneRegister;
    }

    public void setScrollPaneRegister(ScrollPane scrollPaneRegister) {
        this.scrollPaneRegister = scrollPaneRegister;
    }

    public AnchorPane getAnchorPaneRegister() {
        return anchorPaneRegister;
    }

    public void setAnchorPaneRegister(AnchorPane anchorPaneRegister) {
        this.anchorPaneRegister = anchorPaneRegister;
    }

    public SplitPane getSplitPaneRegister() {
        return splitPaneRegister;
    }

    public void setSplitPaneRegister(SplitPane splitPaneRegister) {
        this.splitPaneRegister = splitPaneRegister;
    }

    public Pane getDynamicLabelPane() {
        return dynamicLabelPane;
    }

    public void setDynamicLabelPane(Pane dynamicLabelPane) {
        this.dynamicLabelPane = dynamicLabelPane;
    }

    public Pane getTextLabelPane() {
        return textLabelPane;
    }

    public void setTextLabelPane(Pane textLabelPane) {
        this.textLabelPane = textLabelPane;
    }

    public TextField getNameTextFIeld() {
        return nameTextFIeld;
    }

    public void setNameTextFIeld(TextField nameTextFIeld) {
        this.nameTextFIeld = nameTextFIeld;
    }

    public TextField getSizeTextField() {
        return sizeTextField;
    }

    public void setSizeTextField(TextField sizeTextField) {
        this.sizeTextField = sizeTextField;
    }

    public TextField getTypeTextField() {
        return typeTextField;
    }

    public void setTypeTextField(TextField typeTextField) {
        this.typeTextField = typeTextField;
    }

    public TextField getPrimaryKeyTextFIeld() {
        return primaryKeyTextFIeld;
    }

    public void setPrimaryKeyTextFIeld(TextField primaryKeyTextFIeld) {
        this.primaryKeyTextFIeld = primaryKeyTextFIeld;
    }
//    @FXML
//    public void createLabel(String setText){
//        Label labelCreated = new Label("setText");
//        labelCreated.setMaxSize(80,50);
//        labelCreated.setAlignment(Pos.TOP_CENTER);
//        labelCreated.setText(setText);
//        labelCreated.setFont(Font.font("Verdana",20));
//        dynamicLabelPane.getChildren().add(labelCreated);
//        System.out.println(dynamicLabelPane.getChildren().add(labelCreated));
//
//    }

    public String[] getInfoEnviar() {
        return infoEnviar;
    }

    public void setInfoEnviar(String[] infoEnviar) {
        this.infoEnviar = infoEnviar;

        System.out.println(this.infoEnviar[0]);
    }

    @FXML
    void boton(ActionEvent event) {
        System.out.println("Button enter was pressed");
        //String[] test1=this.infoEnviar;
        //System.out.println(test1[1]);
        labelAtribute.setText(infoEnviar[actualValueArray]);
        //actualiceNewRegister();
        actualValueArray++;

    }

    public void actualiceNewRegister(){//pos del array a actualizar el label
        System.out.println("DEBUG 1");
        System.out.println(infoEnviar[0]);
        labelAtribute = new Label();
        //labelAtribute.setText();
        actualValueArray++;

    }

    @FXML
    void initialize() {

    }
}
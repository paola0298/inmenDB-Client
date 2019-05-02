package Gui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONObject;

public class CreateScheme extends Application {
    private JSObject newScheme;
    private ImageView addImage;

    @Override
    public void start(Stage stage) {
        VBox box = new VBox();
        box.setSpacing(15);
        box.setPadding(new Insets(15));

        VBox upperContainer = new VBox();
        VBox.setVgrow(upperContainer, Priority.ALWAYS);
        upperContainer.setSpacing(5);

        Label schemeNameL = new Label("Nombre: ");
        TextField schemeName = new TextField();
        HBox nameContainer = new HBox(schemeNameL, schemeName);
        nameContainer.setAlignment(Pos.CENTER);

        VBox attributes = new VBox();

        MenuItem normal = new MenuItem("Normal");
        normal.setOnAction(actionEvent -> addAttribute(attributes));
        MenuItem join = new MenuItem("Join");
        join.setOnAction(actionEvent -> addJoinAttribute(attributes));
        ContextMenu cm = new ContextMenu(normal, join);
        addImage = new ImageView(loadImg("res/images/add-list.png"));
        addImage.setFitWidth(40);
        addImage.setFitHeight(40);
        addImage.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                cm.show(addImage, mouseEvent.getScreenX(), mouseEvent.getScreenY());

            } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (!cm.isShowing()) {
                    addAttribute(attributes);
                } else {
                    cm.hide();
                }
            }
        });
        VBox addContainer = new VBox(addImage);
        addContainer.setAlignment(Pos.CENTER_RIGHT);

        HBox options = new HBox();
        options.setAlignment(Pos.CENTER_RIGHT);
        options.setSpacing(15);
        Button accept = new Button("Aceptar");
        Button cancel = new Button("Cancelar");
        options.getChildren().addAll(cancel, accept);

        upperContainer.getChildren().addAll(nameContainer, attributes, addContainer);
        box.getChildren().addAll(upperContainer, options);

        Scene scene = new Scene(box, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Crear nuevo esquema");
        stage.show();
    }

    private void addAttribute(VBox container) {
        System.out.println("Atributo normal");
    }
    private void addJoinAttribute(VBox container) {
        System.out.println("Atributo tipo Join");
    }

    private Image loadImg(String relativePath) {
        String cwd = System.getProperty("user.dir");
        System.out.println(cwd + "/" + relativePath);
        return new Image("file://" + cwd + "/" + relativePath);

    }

    public JSONObject show() {
        launch(CreateScheme.class);

        return new JSONObject();
    }
}

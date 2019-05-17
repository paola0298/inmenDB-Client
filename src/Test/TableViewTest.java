package Test;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TableViewTest extends Application {

    @Override
    public void start(Stage stage) {

        JSONArray registry = new JSONArray();
        registry.put("402390083");
        registry.put("Paola");
        registry.put("20");
        JSONArray registry2 = new JSONArray();
        registry.put("122200589521");
        registry.put("Marlon");
        registry.put("20");



        // sample data

        JSONArray attrName = new JSONArray();
        attrName.put("Cédula");
        attrName.put("Nombre");
        attrName.put("Edad");

        Hashtable<String, String> registers = new Hashtable<>();
        registers.put(registry.getString(0), registry.toString());
        registers.put(registry2.getString(0), registry2.toString());

        TableView<Hashtable<String,String>> table = new TableView<>();



        for (int i=0; i<attrName.length(); i++) {

            TableColumn<Hashtable<String, String>, String> column = new TableColumn<>(attrName.getString(i));
            column.setCellValueFactory(p -> {
//                String reg = p.getValue().get();
                return new SimpleStringProperty();
            });
            table.getColumns().add(column);
        }

//        table.setItems();

//        int c = 0;
//        for (String key: registers.keySet()) {
//            TableColumn<Hashtable<String, String>, String> column = table.getColumns().get(c);
//
//            column.setCellValueFactory(p -> {
//                JSONArray reg = new JSONArray(p.getValue().get(key));
//
//                return new SimpleStringProperty(reg.getString(c));
//            });
//        }

        // use fully detailed type for Map.Entry<String, String>
//        TableColumn<HashMap<String, String>, String> column1 = new TableColumn<>("Key");
//        column1.setCellValueFactory(new Callback<>() {
//
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<HashMap<String, String>, String> p) {
//                // this callback returns property for just one cell, you can't use a loop here
//                // for first column we use key
//                return new SimpleStringProperty(p.getValue().getKey());
//            }
//        });
//
//        TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Value");
//        column2.setCellValueFactory(p -> {
//            // for second column we use value
//            return new SimpleStringProperty(p.getValue().getValue());
//        });

//        //Set<Entry<String, String>>
//
//        ObservableList<Hashtable<String, String>> items = FXCollections.observableArrayList(registers.entrySet());
//        FXCollections.observable



//        table.getColumns().setAll(column1, column2);

        Scene scene = new Scene(table, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

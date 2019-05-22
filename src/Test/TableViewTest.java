package Test;

import com.fasterxml.jackson.annotation.JsonAlias;
import javafx.application.Application;
import javafx.beans.Observable;
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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TableViewTest extends Application {

    @Override
    public void start(Stage stage) {

//        [Nombre, cédula, edad]

        JSONObject scheme = new JSONObject();
        scheme.put("name", "Persona");
        JSONArray attrName = new JSONArray();
        attrName.put("Nombre");
        attrName.put("Cédula");
        attrName.put("Curso");
        JSONArray attrType = new JSONArray();
        attrType.put("string");
        attrType.put("int");
        attrType.put("join");
        JSONArray attrSize = new JSONArray();
        attrSize.put("40");
        attrSize.put("8");
        attrSize.put("Curso");
        scheme.put("attrName", attrName);
        scheme.put("attrType", attrType);
        scheme.put("attrSize", attrSize);
        scheme.put("primaryKey", attrName.getString(1));


        Hashtable<String, JSONArray> collection = new Hashtable<>();

        JSONArray reg1 = new JSONArray();
        reg1.put("Paola");
        reg1.put("402390083");
        reg1.put("curso1");

        JSONArray reg2 = new JSONArray();
        reg2.put("Marlon");
        reg2.put("122200589521");
        reg2.put("curso2");

        JSONArray reg3 = new JSONArray();
        reg3.put("Villegas");
        reg3.put("123456789");
        reg3.put("curso3");

        JSONArray joinAttr = new JSONArray();
        joinAttr.put("Código");
        joinAttr.put("Capacidad");
        joinAttr.put("Aula");
        JSONArray joinValues = new JSONArray();
        joinValues.put("CE1102");
        joinValues.put("40");
        joinValues.put("D3-09");

        collection.put(reg1.getString(1), reg1);
        collection.put(reg2.getString(1), reg2);
        collection.put(reg3.getString(1), reg3);


        TableView<JSONArray> table = new TableView<>();

        int joinIndex = getJoinIndex(scheme);

        for (int i=0; i<attrName.length(); i++) {
            int finalI = i;
            TableColumn<JSONArray, String> column = new TableColumn<>(attrName.getString(i));

            if (i == joinIndex) {
                System.out.println("Join column, setting subcolumns");
                for (int j=0; j<joinAttr.length(); j++) {
                    int finalJ = j;

                    TableColumn<JSONArray, String> joinColumn = new TableColumn<>(joinAttr.getString(j));

                    joinColumn.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getString(finalJ + finalI)));

                    column.getColumns().add(joinColumn);
                }
            } else {

                column.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getString(finalI)));
            }

            table.getColumns().add(column);
        }

        ObservableList<JSONArray> items = FXCollections.observableArrayList(reg1, reg2, reg3);

        table.getItems().addAll(items);

        Scene scene = new Scene(table, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private int getJoinIndex(JSONObject scheme) {
        JSONArray attrType = scheme.getJSONArray("attrType");
        for (int i=0; i< attrType.length(); i++) {
            if (attrType.getString(i).equals("join")) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        launch();
    }
}

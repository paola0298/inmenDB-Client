import GUI.JSON;
import GUI.NewData;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

public class Main{

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        json.put("name", "Persona");
        JSONArray attrName = new JSONArray();
        attrName.put("Nombre");
        JSONArray attrSize = new JSONArray();
        attrName.put("10");
        JSONArray attrType = new JSONArray();
        attrName.put("string");
        json.put("attrName", attrName);
        json.put("attrSize", attrSize);
        json.put("attrType", attrType);
        NewData ventana = new NewData();
        ventana.show();
    }

//    @Override
//    public void start(Stage primaryStage)throws Exception {
//
//        String stringPrueba;
//        stringPrueba= JOptionPane.showInputDialog(null,"inserte json");
//        JSON prueba = new JSON();
//        NewData window= new NewData();
//        //window.start(primaryStage, prueba.recive_data(stringPrueba).getAttr_name());
//
//
//
//
//    }
}



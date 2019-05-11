import Connection.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;

public class ServerClientTest {

    /*
    {
  "scheme": {
    "attrSize": [
      20,
      100,
      12
    ],
    "name": "Persona",
    "attrName": [
      "Nombre",
      "Edad",
      "Cédula"
    ],
    "attrType": [
      "string",
      "int",
      "int"
    ],
    "primaryKey": "Cédula"
  },
  "action": "createScheme"
}
     */

    private JSONObject createScheme(String name){
        JSONObject response = new JSONObject();
        JSONObject newScheme = new JSONObject();
        JSONArray attrName = new JSONArray();
        attrName.put("Nombre");
        attrName.put("Cedula");
        attrName.put("Edad");
        JSONArray attrType = new JSONArray();
        attrType.put("String");
        attrType.put("int");
        attrType.put("int");
        JSONArray attrSize = new JSONArray();
        attrSize.put(20);
        attrSize.put(100);
        attrSize.put(12);

        newScheme.put("name", name);
        newScheme.put("attrName", attrName);
        newScheme.put("attrType", attrType);
        newScheme.put("attrSize", attrSize);
        newScheme.put("primaryKey", "Cedula");

        response.put("action", "createScheme");
        response.put("scheme", newScheme.toString());

        return response;
    }

    private JSONObject createScheme(String name, String join){
        JSONObject response = new JSONObject();
        JSONObject newScheme = new JSONObject();
        JSONArray attrName = new JSONArray();
        attrName.put("Nombre");
        attrName.put("Cedula");
        attrName.put("Edad");
        attrName.put("Persona");
        JSONArray attrType = new JSONArray();
        attrType.put("String");
        attrType.put("int");
        attrType.put("int");
        attrType.put(join);
        JSONArray attrSize = new JSONArray();
        attrSize.put(20);
        attrSize.put(100);
        attrSize.put(12);
        attrSize.put("join");

        newScheme.put("name", name);
        newScheme.put("attrName", attrName);
        newScheme.put("attrType", attrType);
        newScheme.put("attrSize", attrSize);
        newScheme.put("primaryKey", "Cedula");

        response.put("action", "createScheme");
        response.put("scheme", newScheme.toString());

        return response;
    }

    private JSONObject deleteScheme(String scheme){
        JSONObject response = new JSONObject();
        response.put("action", "deleteScheme");
        response.put("scheme", scheme);

        return response;
    }

    private JSONObject modifyScheme(){ return new JSONObject();}

    private JSONObject queryScheme() { return new JSONObject();}

    private JSONObject insertData(String scheme) {
        JSONObject response = new JSONObject();
        JSONArray attr = new JSONArray();
        attr.put("Paola");
        attr.put("402390083");
        attr.put("20");

        response.put("action", "insertData");
        response.put("type", scheme);
        response.put("attr", attr);

        return response;
    }

    private JSONObject insertData(String scheme, String join) {
        JSONObject response = new JSONObject();
        JSONArray attr = new JSONArray();
        attr.put("Paola");
        attr.put("402390083");
        attr.put("20");
        attr.put(join);

        response.put("action", "insertData");
        response.put("type", scheme);
        response.put("attr", attr);

        return response;
    }

    private JSONObject deleteData(String scheme) {
        JSONObject response = new JSONObject();

        response.put("action", "deleteData");
        response.put("scheme", scheme);
        response.put("primaryKey", "402390083");

        return response;
    }

    private Hashtable<String, JSONObject> deserializeSchemes(JSONObject scheme){
        ObjectMapper objectMapper = new ObjectMapper();
        String stringSchemes = scheme.getString("schemes");
        Hashtable<String, JSONObject> actualSchemes = new Hashtable<>();

        try {
            actualSchemes = objectMapper.readValue(stringSchemes, Hashtable.class);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error getting hashtable object");
        }

        return actualSchemes;

    }

    private Hashtable<String, Hashtable<String, JSONArray>> deserializeCollections(JSONObject collec){
        if (collec.get("status") == "success") {
            ObjectMapper objectMapper = new ObjectMapper();
            String stringCollect = collec.getString("collections");
            Hashtable<String, Hashtable<String, JSONArray>> actualCollections = new Hashtable<>();

            try {
                actualCollections = objectMapper.readValue(stringCollect, Hashtable.class);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error getting hashtable object");
            }

            return actualCollections;
        }

        return new Hashtable<>();

    }


    public static void main(String[] args) {
        ServerClientTest test = new ServerClientTest();
        Client client = new Client("localhost", 6307);
        System.out.println("Creando un nuevo esquema");
        System.out.println("Persona");
        JSONObject newScheme = test.createScheme("Persona");

        JSONObject response1 = client.connect(newScheme);
//
//        Hashtable<String, JSONObject> actualSchemes = test.deserializeSchemes(response);
////        actualSchemes.get()
//        System.out.println(actualSchemes.toString());
//        System.out.println(actualSchemes.get("name"));

        System.out.println("Estudiante");
        JSONObject newJoinScheme = test.createScheme("Estudiante", "Persona");
        JSONObject response = client.connect(newJoinScheme);
        Hashtable<String, JSONObject> actualSchemes = test.deserializeSchemes(response);

//        actualSchemes.get()
        System.out.println(actualSchemes.toString());


        JSONObject insert = client.connect(test.insertData("Persona"));
        JSONObject insert2 = client.connect(test.insertData("Estudiante", "Persona"));

        Hashtable<String, Hashtable<String, JSONArray>> actualCollections1 = test.deserializeCollections(insert);
        System.out.println("Actual Collections1");
        System.out.println(actualCollections1.toString());
        System.out.println(insert);
        System.out.println("\n");

        Hashtable<String, Hashtable<String, JSONArray>> actualCollections = test.deserializeCollections(insert2);
        System.out.println("Actual Collections");
        System.out.println(actualCollections.toString());
        System.out.println(insert2);
        System.out.println("\n");


        JSONObject deleteScheme = test.deleteScheme("Estudiante");
        JSONObject response2 = client.connect(deleteScheme);
        System.out.println("Deleting scheme Estudiante");
        System.out.println(response2.toString());

    }
}

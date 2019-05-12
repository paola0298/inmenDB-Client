import Logic.Controller;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {

        Controller controller = Controller.getInstance();

//        controller.createScheme();

        JSONObject scheme = new JSONObject();
        scheme.put("name", "Persona");
        JSONArray arrayName = new JSONArray();
        arrayName.put("Nombre");
        arrayName.put("Cédula");
        arrayName.put("Edad");

        JSONArray arrayType = new JSONArray();
        arrayType.put("string");
        arrayType.put("int");
        arrayType.put("int");

        JSONArray arraySize = new JSONArray();
        arraySize.put("20");
        arraySize.put("12");
        arraySize.put("100");

        scheme.put("attrName", arrayName);
        scheme.put("attrSize", arraySize);
        scheme.put("attrType", arrayType);
        scheme.put("primaryKey", "Cédula");

        scheme.put("attrCount", 3);
        scheme.put("hasJoin", false);

        controller.updateScheme(scheme);

    }
}

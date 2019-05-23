package Logic;

import Connection.Client;
import Gui.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Esta clase se encarga de manejar la lógica y comunicación con el servidor, así como realizar las
 * operaciones necesarias en la interfaz.
 *
 * @version 1.0
 */
public class Controller {

    private static Controller instance;
    private GUI mainGui;
    private Client client;

    private Hashtable<String, String> localSchemes;
    private Hashtable<String, Hashtable<String, JSONArray>> localCollections;

    private ObjectMapper mapper;

    private TypeReference<Hashtable<String, String>> schemeTypeRef = new TypeReference<>() {};
    private TypeReference<Hashtable<String, JSONArray>> collectionTypeRef = new TypeReference<>() {};
    private TypeReference<Hashtable<String, Hashtable<String, JSONArray>>> collectionsTypeRef = new TypeReference<>() {};

    /**
     * Constructor por defecto de Controller.
     */
    private Controller() {
        this.localSchemes = new Hashtable<>();
        this.localCollections = new Hashtable<>();
        this.mapper = new ObjectMapper();
        initialize();
    }

    public void setMainGui(GUI mainGui) {
        this.mainGui = mainGui;
    }

    /**
     * Método que inicializa la configuración del cliente desde el archivo de propiedades, necesario para
     * conectarse con el servidor.
     */
    private void initialize() {
        Properties props = new Properties();
        try {
            FileInputStream stream = new FileInputStream(System.getProperty("user.dir") + "/res/settings.properties");
            props.load(stream);
            String host = props.get("host_ip").toString();
            int port = Integer.parseInt(props.get("host_port").toString());
            this.client = new Client(host, port);
        } catch (IOException e) {
            System.out.println("[Error] Could not read settings");
        }
    }



    /**
     * Éste método sirve para obtener la instancia del controlador.
     *
     * @return Instancia de Controller.
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Método que calcula el tiempo total que tarda una operación.
     * @param startTime Tiempo inicial antes de realizar la operación.
     * @return Tiempo final que duró la operación en realizarse, incluyendo la unidad de tiempo respectiva
     * dependiendo de tiempo durado.
     */
    private String getFinalTime(double startTime) {
        //TODO devolver la unidad de tiempo respectiva, dependiendo cuanto tarda cada operación.
        return (System.currentTimeMillis() - startTime) / 1000 + " s";
    }

    /**
     * Método para obtener el HashTable local de esquemas.
     *
     * @return HashTable de esquemas.
     */
    public Hashtable<String, String> getLocalSchemes() {
        return localSchemes;
    }

    /**
     * Método para obtener el HashTable de las colecciones locales.
     * @return HashTable de las colecciones locales.
     */
    public Hashtable<String, Hashtable<String, JSONArray>> getLocalCollections() {
//        System.out.println("local collections in controller " + localCollections);
        return localCollections;
    }

    /**
     * Método que se encarga de recuperar los esquemas, índices y colecciones de datos del servidor.
     */
    public void getUpdatedData() {
        double startTime = System.currentTimeMillis();

        JSONObject action = new JSONObject();
        action.put("action", "getUpdatedData");

        JSONObject response = client.connect(action);

        if (response.getString("status").equals("success")) {
            try {
                localSchemes = mapper.readValue(
                        response.getString("schemes"), schemeTypeRef);
                localCollections = mapper.readValue(
                        response.getString("collections"), collectionsTypeRef);
                mainGui.loadSchemesList(localSchemes);
                mainGui.showMessage("Esquemas y colecciones cargados correctamente - " + getFinalTime(startTime));
            } catch (IOException e) {
                mainGui.showMessage("Error al recibir los datos del servidor - " + getFinalTime(startTime));
            }
        } else {
            mainGui.showMessage("No se pudieron recuperar los datos del servidor - " + getFinalTime(startTime));
        }
    }

    /**
     * Éste método se encarga de mostrar la ventana para crear un nuevo esquema, así como enviar la solicitud
     * al servidor, y actualizar la interfaz.
     */
    public void createScheme() {
        NewScheme.newScheme();
    }

    public void getSchemeRecords(String schemeName) {

        double startTime = System.currentTimeMillis();

        JSONObject action = new JSONObject();
        action.put("action", "getSchemeData");
        action.put("schemeName", schemeName);

        JSONObject response = client.connect(action);

        if (response.getString("status").equals("success")) {
            try {
                /*
                SE NECESITA:
                    -Esquema actual: para generar columnas normales - JSONObject
                    -Registros del esquema actual: para popular la tabla - JSONArray
                    -Esquema(s) de join si hay: para generar columnas del join - JSONObject
                    -Registros del join: para popular las columnas del join - JSONArray

                CÓMO SE HACE:
                    -Primero conseguir el esquema actual - JSONObject
                    -Revisar si el esquema contiene un join - Método()

                SI NO HAY JOIN:
                    -Generar las columnas del esquema actual.
                    -Insertar los registros del esquema actual a la tabla.

                SI HAY JOIN:
                    -Generar las columnas del esquema actual.
                    -Generar las columnos del join.
                    -Insertar los registros normales y del join a la tabla.
                */

                JSONObject data = new JSONObject();

                Hashtable<String, JSONArray> collection =
                        mapper.readValue(response.getString("collection"), collectionTypeRef); //Registros normales

                JSONObject actualScheme = getSelectedScheme();
                JSONObject joinSchemes = getJoinSchemes(actualScheme);

                JSONArray tableItems = generateTableItems(collection, joinSchemes, actualScheme);

                data.put("actualScheme", actualScheme);
                data.put("joinSchemes", joinSchemes);
                data.put("tableItems", tableItems);

                mainGui.showDataTable(data);

                mainGui.showMessage("Datos recuperados correctamente - " + getFinalTime(startTime));
            } catch (IOException e) {
                mainGui.showMessage("No se pudo mostrar la información - " + getFinalTime(startTime));
            }
        } else {
            mainGui.showMessage("No se pudieron recuperar los datos del servidor - " + getFinalTime(startTime));
        }

    }

    //######################
    private JSONObject getJoinSchemes(JSONObject scheme) {
        JSONObject joinSchemes = new JSONObject();

        JSONArray attrTypes = scheme.getJSONArray("attrType");
        JSONArray attrSize = scheme.getJSONArray("attrSize");

        for (int i=0; i<attrTypes.length(); i++) {
            if (attrTypes.getString(i).equals("join")) {
                joinSchemes.put(attrSize.getString(i), new JSONObject(localSchemes.get(attrSize.getString(i))));
            }
        }
        return joinSchemes;
    }

    private JSONArray generateTableItems(Hashtable<String, JSONArray> collection, JSONObject joinSchemes, JSONObject scheme) {
        JSONArray tableItems = new JSONArray();

        if (collection != null) {
            for (JSONArray value : collection.values()) {
                JSONObject item = new JSONObject();

                item.put("normal", value);

                if (joinSchemes.length() > 0) {
                    JSONArray joinItems = getAssociatedRecords(value, scheme.getJSONArray("attrType"), scheme.getJSONArray("attrSize"));
                    item.put("join", joinItems);
                }

                tableItems.put(item);
            }
        }
        return tableItems;
    }

    private JSONArray getAssociatedRecords(JSONArray schemeRecord, JSONArray attrType, JSONArray attrSize) {
        JSONArray joinRecords = new JSONArray();

        for (int i=0; i<schemeRecord.length(); i++) {
            if (attrType.getString(i).equals("join")) {
                Hashtable<String, JSONArray> collection = localCollections.get(attrSize.getString(i));
                JSONArray record = collection.get(schemeRecord.getString(i));
                joinRecords.put(record);
            }
        }
        return joinRecords;
    }

    //######################

    /**
     * Método que le envía al servidor el nuevo esquema a añadir.
     * @param schemeToSend Nuevo esquema a añadir.
     */
    public void sendNewScheme(JSONObject schemeToSend) {
//        System.out.println(schemeToSend.toString());

        double startTime = System.currentTimeMillis();

        JSONObject response = client.connect(schemeToSend);

        if (response.getString("status").equals("success")) {
            try {
                localSchemes = mapper.readValue(response.getString("schemes"), schemeTypeRef);
                mainGui.loadSchemesList(localSchemes);
                mainGui.showMessage("Esquema añadido correctamente - " + getFinalTime(startTime));
            } catch (IOException e) {
                mainGui.showMessage(e.getMessage() + " - " + getFinalTime(startTime));
            }
        } else {
            if (response.getString("error").equals("Already exists")) {
                mainGui.loadSchemesList(localSchemes);
                mainGui.showMessage("El esquema ya existe - " + getFinalTime(startTime));
            } else {
                mainGui.showMessage("No se pudo crear el esquema - " + getFinalTime(startTime));
            }
        }
    }

    /**
     * Método que le indica al servidor que elimine el esquema dado.
     * @param schemeName Nombre del esquema a eliminar.
     */
    public void deleteScheme(String schemeName) {
        JSONObject toSend = new JSONObject();
        toSend.put("action", "deleteScheme");
        toSend.put("scheme", schemeName);

        double startTime = System.currentTimeMillis();

        JSONObject response = client.connect(toSend);

        if (response.getString("status").equals("success")) {
            try {
                localSchemes = mapper.readValue(response.getString("schemes"), schemeTypeRef);
                localCollections = mapper.readValue(response.getString("collections"), collectionsTypeRef);
                mainGui.loadSchemesList(localSchemes);
                mainGui.showMessage("Esquema eliminado correctamente - " + getFinalTime(startTime));
            } catch (IOException e) {
                mainGui.showMessage(e.getMessage() + " - " + getFinalTime(startTime));
            }
        } else {
            if (response.getString("error").equals("join")) {
                mainGui.showMessage("El esquema no se puede eliminar ya que hay un join - " + getFinalTime(startTime));
            } else {
                mainGui.showMessage("No se pudo eliminar el esquema - " + getFinalTime(startTime));
            }
        }
    }

    /**
     * Método que invoca la ventana para la creación de nuevo registros.
     */
    public void createRecord() { NewData.newData(); }

    /**
     * Método que se encargar de enviarle en nuevo registro al servidor, para luego actualizar las colecciones locales.
     * @param dataToInsert Nuevo registro a insertar.
     */
    public void sendNewRecord(JSONObject dataToInsert) {

        double startTime = System.currentTimeMillis();

        JSONObject response = client.connect(dataToInsert);

//        System.out.println("response inserting data " + response.toString(2));

        if (response.getString("status").equals("success")) {
            String deleted = response.getString("deleted");
            try {

                switch (deleted){
                    case "all":
                        mainGui.showMessage("Los registros se eliminaron correctamente - " + getFinalTime(startTime));
                        break;
                    case "some":
                        mainGui.showMessage("Algunos registros no se puedieron eliminar - " + getFinalTime(startTime));
                        break;
                    case "none":
                        mainGui.showMessage("No se pudieron eliminar los registros - " + getFinalTime(startTime));
                        break;
                }

                localCollections = mapper.readValue(response.getString("collections"), collectionsTypeRef);

                JSONObject data = new JSONObject();
                Hashtable<String, JSONArray> collection = localCollections.get(getActualSchemeName());

                JSONObject actualScheme = getSelectedScheme();
                JSONObject joinSchemes = getJoinSchemes(actualScheme);

                JSONArray tableItems = generateTableItems(collection, joinSchemes, actualScheme);

                data.put("actualScheme", actualScheme);
                data.put("joinSchemes", joinSchemes);
                data.put("tableItems", tableItems);

                mainGui.showDataTable(data);

                mainGui.showMessage("Registro agregado correctamente - " + getFinalTime(startTime));
            } catch (IOException e) {
                mainGui.showMessage(e.getMessage() + " - " + getFinalTime(startTime));
            }
        } else {
            if (response.getString("error").equals("No exists")) {
                mainGui.showMessage("El esquema no existe - " + getFinalTime(startTime));
            } else {
                mainGui.showMessage("No se pudo crear la coleccion de datos - " + getFinalTime(startTime));
            }
        }
    }

    public void deleteRecords(JSONArray records) {

        double startTime = System.currentTimeMillis();

        JSONObject action = new JSONObject();
        action.put("action", "deleteData");
        action.put("scheme", getActualSchemeName());
        action.put("records", records);

        JSONObject response = client.connect(action);

        if (response.getString("status").equals("success")) {
            try {
                localCollections = mapper.readValue(
                        response.getString("collections"), collectionsTypeRef);

                JSONObject data = new JSONObject();

                Hashtable<String, JSONArray> collection = localCollections.get(getActualSchemeName());

                JSONObject actualScheme = getSelectedScheme();
                JSONObject joinSchemes = getJoinSchemes(actualScheme);

                JSONArray tableItems = generateTableItems(collection, joinSchemes, actualScheme);

                data.put("actualScheme", actualScheme);
                data.put("joinSchemes", joinSchemes);
                data.put("tableItems", tableItems);

                mainGui.showDataTable(data);

                mainGui.showMessage("Los registros se eliminaron correctamente - " + getFinalTime(startTime));

            } catch (IOException e) {
                mainGui.showMessage("Error al recibir los datos del servidor - " + getFinalTime(startTime));
            }
        } else {
            mainGui.showMessage("Ocurrió un error al eliminar los registros - " + getFinalTime(startTime));
        }
    }

    /**
     * Método encargado de invocar a la ventana para realizar una búsqueda.
     */
    public void querySchemeCollection() {
        querySchemeCollection.queryScheme();
    }

    public void sendQuery(JSONObject queryToSend) {

        double startTime = System.currentTimeMillis();

        JSONObject response = client.connect(queryToSend);

        if (response.getString("status").equals("success")) {

            /*
                SE NECESITA:
                    -Esquema actual: para generar columnas normales - JSONObject
                    -Registros del esquema actual: para popular la tabla - JSONArray
                    -Esquema(s) de join si hay: para generar columnas del join - JSONObject
                    -Registros del join: para popular las columnas del join - JSONArray

                CÓMO SE HACE:
                    -Primero conseguir el esquema actual - JSONObject
                    -Revisar si el esquema contiene un join - Método()

                SI NO HAY JOIN:
                    -Generar las columnas del esquema actual.
                    -Insertar los registros del esquema actual a la tabla.

                SI HAY JOIN:
                    -Generar las columnas del esquema actual.
                    -Generar las columnos del join.
                    -Insertar los registros normales y del join a la tabla.
                */

            JSONObject schemeData = new JSONObject(response.getString("scheme"));
            JSONObject joinsData = new JSONObject(response.getString("join"));

            System.out.println("Scheme data:\n" + schemeData);
            System.out.println("Join data:\n" + joinsData);

//            JSONObject queryData = new JSONObject();
//
//            queryData.put("actualScheme", getScheme(
//                    new JSONObject(queryToSend.getString("parameters")).getString("scheme")));
//            JSONArray temp = joinsData.getJSONArray("joinName");
//            queryData.put("joinScheme", getScheme(temp.getString(0)));
//
//            JSONArray tableItems = processQueryData(
//                    new JSONArray(schemeData.getString("attributes")),
//                    joinsData.getJSONArray("attributesJoin"));
//
//            queryData.put("tableItems", tableItems);

            JSONObject data = new JSONObject();


            Hashtable<String, JSONArray> collection = new Hashtable<>();
//                    mapper.readValue(response.getString("collection"), collectionTypeRef); //Registros normales


            JSONObject actualScheme = getSelectedScheme();
            JSONObject joinSchemes = getJoinSchemes(actualScheme);

            JSONArray tableItems = generateTableItems(collection, joinSchemes, actualScheme);

            data.put("actualScheme", actualScheme);
            data.put("joinSchemes", joinSchemes);
            data.put("tableItems", tableItems);

            mainGui.showDataTable(data);


            mainGui.showMessage("Datos recuperados correctamente - " + getFinalTime(startTime));

        } else {
            System.out.println(response);
            mainGui.showMessage("Ocurrió un error al recuperar los datos - " + getFinalTime(startTime));
        }
//TODO

    }

    public void newIndex() {
        NewIndex.newIndex();
    }

    public void createIndex(JSONObject generatedJson) {
        double startTime = System.currentTimeMillis();

        JSONObject response = client.connect(generatedJson);
        if (response.getString("status").equals("success")) {

            mainGui.showMessage("Indice creado exitosamente - " + getFinalTime(startTime));
        }

        System.out.println(response);

    }

    public JSONObject getListOfIndex(){
        JSONObject query = new JSONObject();
        query.put("action", "getIndexList");
        JSONObject response = client.connect(query);
        if (response.getString("status").equals("success")){
            return response.getJSONObject("list");
        }
        return null;
    }

    /**
     * Método que devuelve el nombre del esquema seleccionado actualmente.
     * @return Nombre del esquema seleccionado.
     */
    public String getActualSchemeName() {
        return mainGui.getSelectedSchemeName();
    }

    /**
     * Método encargado de crear y devolver el JSONObject del esquema actual.
     * @return JSONObject del esquema actual.
     */
    public JSONObject getSelectedScheme() {
        System.out.println("Esquemas locales \n" + localSchemes);
        System.out.println("Esquema seleccionado: " + getActualSchemeName());

        return new JSONObject(localSchemes.get(getActualSchemeName()));
    }

    public JSONArray getSelectedSchemeAttr() {
        String actualSchemeName = getActualSchemeName();
        String actualScheme = localSchemes.get(actualSchemeName);
        JSONObject actualSchemeObject = new JSONObject(actualScheme);
        JSONArray attrName = actualSchemeObject.getJSONArray("attrName");

        System.out.println("Attr name " + attrName);
        return attrName;

    }

}

package Logic;

import Connection.Client;

import Gui.GUI;
import Gui.NewScheme;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
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
    private ObjectMapper mapper;

    /**
     * Constructor por defecto de Controller.
     */
    private Controller() {
        this.localSchemes = new Hashtable<>();
        this.mapper = new ObjectMapper();
        initialize();
    }

    public void setMainGui(GUI mainGui) {
        this.mainGui = mainGui;
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
     * Éste método se encarga de mostrar la ventana para crear un nuevo esquema, así como enviar la solicitud
     * al servidor, y actualizar la interfaz.
     */
    public void generateScheme() {
        NewScheme.newScheme();
    }

    public void sendScheme(JSONObject schemeToSend) {
        System.out.println(schemeToSend.toString());

        double startTime = System.currentTimeMillis();

        JSONObject response = client.connect(schemeToSend);

        if (response.getString("status").equals("success")) {
            try {
                @SuppressWarnings("unchecked")
                Hashtable<String, String> updatedSchemes = mapper.readValue(response.getString("schemes"), Hashtable.class);
                mainGui.loadSchemesList(updatedSchemes);
                mainGui.showMessage("Esquema añadido correctamente - " + getFinalTime(startTime));
                localSchemes = updatedSchemes;
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

    private String getFinalTime(double startTime) {
        return (System.currentTimeMillis() - startTime) / 1000 + " s";
    }

    public void querySchemes() {
        double startTime = System.currentTimeMillis();

        JSONObject action = new JSONObject();
        action.put("action", "querySchemes");

        JSONObject response = client.connect(action);

        if (response.getString("status").equals("success")) {
            try {
                @SuppressWarnings("unchecked")
                Hashtable<String, String> updatedSchemes = mapper.readValue(response.getString("schemes"), Hashtable.class);
                mainGui.loadSchemesList(updatedSchemes);
                mainGui.showMessage("Esquemas cargados correctamente - " + getFinalTime(startTime));
                localSchemes = updatedSchemes;

            } catch (IOException e) {
                mainGui.showMessage("Error al recibir los esquemas - " + getFinalTime(startTime));
            }
        } else {
            mainGui.showMessage("No se pudieron recuperar los esquemas - " + getFinalTime(startTime));
        }
    }

    public void querySchemeData(String schemeName) {

        double time = System.currentTimeMillis();

        JSONObject action = new JSONObject();
        action.put("action", "getSchemeData");
        action.put("schemeName", schemeName);

        JSONObject response = client.connect(action);

        if (response.getString("status").equals("success")) {
            try {
                Hashtable<String, JSONObject> registers = mapper.readValue(response.getString("collection"), Hashtable.class);
                mainGui.loadSchemeTableColumns(new JSONObject(response.getString("scheme")));
                mainGui.loadDataToTable(registers);

                mainGui.showMessage("Datos recuperados correctamente - " + getFinalTime(time));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }

    }

    public String getActualSchemeName() {
        return mainGui.getSelectedSchemeName();
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

}

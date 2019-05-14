package Logic;

import Connection.Client;

import Gui.GUI;
import Gui.NewScheme;
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

    private Hashtable<String, JSONObject> localSchemes;

    /**
     * Constructor por defecto de Controller.
     */
    private Controller() {
        this.localSchemes = new Hashtable<>();
        this.localSchemes.put("Persona", new JSONObject());
        this.localSchemes.put("Carro", new JSONObject());
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

//        JSONObject response = client.connect(schemeToSend);
        JSONObject response = new JSONObject();
        response.put("status", "SUCCESS");

        //TODO recibir el hashtable con los esquemas y deserializarlo

        if (response.getString("status").equals("SUCCESS")) {
            //Test
            localSchemes = new Hashtable<>();
            localSchemes.put("Curso", new JSONObject());
            localSchemes.put("Universidad", new JSONObject());
            //
            mainGui.loadSchemesList(localSchemes);
            mainGui.showMessage("Esquema añadido!");
        } else {

        }
    }

    public void updateScheme(JSONObject scheme) {
        NewScheme win = new NewScheme();
        JSONObject action = win.updateScheme(scheme);

        System.out.println(action.toString(2));
    }

    public void queryScheme(String schemeName) {

    }

    /**
     * Método para obtener el HashTable local de esquemas.
     *
     * @return HashTable de esquemas.
     */
    public Hashtable<String, JSONObject> getLocalSchemes() {
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

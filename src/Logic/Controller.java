package Logic;

import Connection.Client;
import Gui.GUI;
import Gui.NewScheme;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class Controller {
    private static Controller instance;
    private Client client;
    private Hashtable<String, JSONObject> schemesTable;

    private GUI mainGui;

    private Controller() {
        this.schemesTable = new Hashtable<>();
        initialize();
//        this.mainGui = mainGui;

    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void createScheme() {
        NewScheme win = new NewScheme();

        JSONObject action = win.show();

        System.out.println(action.toString(2));
    }

    public Hashtable<String, JSONObject> getSchemesTable() {
        return schemesTable;
    }

    private void initialize() {
        Properties props = new Properties();
        try {
            FileInputStream stream = new FileInputStream(System.getProperty("user.dir") + "/res/settings.properties");
            props.load(stream);
            String host = props.get("host_ip").toString();
            int port = Integer.parseInt(props.get("host_port").toString());
            this.client = new Client(host, port);
            System.out.println("[Info] Settings initialized successfully");
        } catch (IOException e) {
            System.out.println("[Error] Could not read settings");
        }
    }

}

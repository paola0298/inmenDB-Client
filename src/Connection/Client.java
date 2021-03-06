package Connection;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * La clase Client es la que se conecta con el servidor y envía y recibe mensajes.
 * @author marlon
 * @version 1.0
 */
public class Client {
    private int port;
    private String host;
    private Socket clientSocket;

    /**
     * @param host Dirección IP del servidor.
     * @param port Puerto en el cual el servidor está escuchando.
     */
    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    /**
     * @param message Mensaje a enviar al servidor.
     * @return Mensaje recibido del servidor.
     */
    public JSONObject connect(JSONObject message) {
        try {
            this.clientSocket = new Socket(this.host, this.port);
        } catch (IOException e) {
            System.out.println("Error opening socket: " + e.getMessage());
        }
        if (this.clientSocket != null) {
            sendData(message.toString());

            return getData();
        } else {
            JSONObject obj = new JSONObject();
            obj.put("status", "CONNECTION_REFUSED");
            obj.put("error", "no_connection");
            return obj;
        }
    }

    /**
     * Este método se usa para enviar información al servidor.
     * @param message Mensaje a enviar al servidor.
     */
    private void sendData(String message) {
        try {
            DataOutputStream os = new DataOutputStream(this.clientSocket.getOutputStream());
            os.writeUTF(EncodeDecode.cifrarBase64(message));
        } catch (IOException e) {
            System.out.println("Error sending data: " + e.getMessage());
        }
    }

    /**
     * Este método recibe la información enviada del servidor.
     * @return Información enviada del servidor.
     */
    private JSONObject getData() {
        try {
            DataInputStream is = new DataInputStream(this.clientSocket.getInputStream());
            String data = EncodeDecode.descifrarBase64(is.readUTF());
//            System.out.println("Data from server: \n" + data);
            return new JSONObject(data);
        } catch (IOException e) {
            System.out.println("Error getting data: " + e.getMessage());
            JSONObject err = new JSONObject();
            err.put("status", "ERROR");
            return err;
        }
    }

    public static void main(String[] args) {

    }

}

package GUI;
import com.google.gson.Gson;
import javafx.geometry.Pos;

import java.awt.*;


public class JSON {
    public void recive_data(String data){
        Gson gson = new Gson();
        JSONNewRegister parseData = gson.fromJson(data, JSONNewRegister.class);
        String a[]=parseData.getAttr_name();
//        NewRegister ventana = new NewRegister();
//        ventana.setInfoEnviar(a);
//        System.out.println(ventana.getInfoEnviar()[0]);
//        ventana.actualiceNewRegister();
//        for (int i=0;i<a.length;i++){
//            System.out.println(a[i]);
////            ventana.createLabel(a[i]);
//        }

    }
}

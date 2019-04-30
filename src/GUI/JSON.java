package GUI;
import com.google.gson.Gson;
import javafx.geometry.Pos;

import java.awt.*;


public class JSON {
    public void recive_data(String data){
        Gson gson = new Gson();
        JSONNewRegister parseData = gson.fromJson(data, JSONNewRegister.class);
        String a[]=parseData.getAttr_name();
        int count = 0;
        NewRegister ventana = new NewRegister();

        for (int i=0;i<a.length;i++){
            System.out.println(a[i]);
            count++;
            ventana.createLabel(a[i]);
        }

    }
}

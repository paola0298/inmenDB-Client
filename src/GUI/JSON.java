package GUI;
import com.google.gson.Gson;
import javafx.geometry.Pos;

import java.awt.*;


public class JSON {
    public JSONNewRegister recive_data(String data){
        Gson gson = new Gson();
        JSONNewRegister parseData = gson.fromJson(data, JSONNewRegister.class);
        String a[]=parseData.getAttr_name();
        return parseData;

    }
}

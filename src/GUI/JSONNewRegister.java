package GUI;

import com.google.gson.Gson;

public class JSONNewRegister {
    private String name;
    private String []attr_name;
    private int []attr_size;
    private String []attr_type;
    private boolean []primary_key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String[] attr_name) {
        this.attr_name = attr_name;
    }

    public int[] getAttr_size() {
        return attr_size;
    }

    public void setAttr_size(int[] attr_size) {
        this.attr_size = attr_size;
    }

    public String[] getAttr_type() {
        return attr_type;
    }

    public void setAttr_type(String[] attr_type) {
        this.attr_type = attr_type;
    }

    public boolean[] getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(boolean[] primary_key) {
        this.primary_key = primary_key;
    }


}

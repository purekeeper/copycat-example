package com.anjuke.mss;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by root on 16-8-3.
 */
public class WriteData implements Serializable{
    private String dictionary;
    private String id;
    private String type;
    private Map<String,String> keyword;
    private Map<String,Object> value;

    public void setType(String type) {
        this.type=type;
    }
    public String getType() {
        return type;
    }
    public void setDictionary(String dic) {

        this.dictionary=dic;
    }
    public String getDictionary() {
        return dictionary;
    }
    public void setId(String id) {
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public void setKeyWord(Map<String,String> key) {
        this.keyword=key;
    }
    public Map<String,String> getKeyWord() {
        return keyword;
    }

    public void setValue(Map<String,Object> value) {
        this.value=value;
    }
    public Map<String,Object> getValue() {
        return value;
    }

}

package com.anjuke.mss;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by root on 16-8-3.
 */
//set data structure
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

    public void setKeyword(Map<String,String> keyword) {
        this.keyword=keyword;
    }
    public Map<String,String> getKeyword() {
        return keyword;
    }

    public void setValue(Map<String,Object> value) {
        this.value=value;
    }
    public Map<String,Object> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "WriteData{" +
                "dictionary='" + dictionary + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", keyword=" + keyword +
                ", value=" + value +
                '}';
    }
}

package com.anjuke.mss;

import java.io.Serializable;
import java.util.Map;
/**
 * Created by root on 16-8-1.
 */
//search return data structure
public class ResponseData implements Serializable{
    private String id;
    private String type;
    private String keyWord;
    private String keyWorldType;
    Map<String,Object> value;
    public String getId()
    {return this.id;}
    public void setId(String id)
    {this.id = id;}

    public String getType()
    {return this.type;}
    public void setType(String type)
    {this.type = type;}

    public String getKeyWord()
    {return this.keyWord;}
    public void setKeyWord(String keyWord)
    {this.keyWord = keyWord;}

    public String getKeyWordType()
    {return this.keyWorldType;}
    public void setKeyWordType(String keyWorldType)
    {this.keyWorldType = keyWorldType;}

    public Map<String, Object> getValue()
    {return this.value;}
    public void setValue(Map<String,Object> value)
    {this.value = value;}

    @Override
    public String toString() {
        return "ResponseData{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", keyWorldType='" + keyWorldType + '\'' +
                ", value=" + value +
                '}';
    }
}

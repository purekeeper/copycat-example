package com.anjuke.mss;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by root on 16-7-25.
 */
//map<dic,Queryvalue>
public class QueryValue implements Serializable {
    private String id;
    private String type;
    private Map<String,String> keyWord;
    private Map<String,Object> value;
    public QueryValue(String type, Map setKey, Map setValue)
    {
        this.type = type;
        this.keyWord = setKey;
        this.value = setValue;
    }
    public void setType(String type)
    {this.type = type;}
    public String getType()
    {return this.type;}
    public void setSetKey(Map setKey)
    {this.keyWord = setKey;}
    public Map getSetKey()
    {return this.keyWord;}
    public void setSetValue(Map setValue)
    {this.value = setValue;}
    public Map getSetValue()
    {return this.value;}
}

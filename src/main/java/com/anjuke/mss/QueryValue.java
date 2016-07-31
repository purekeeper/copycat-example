package com.anjuke.mss;
import org.mapdb.Atomic;
import org.mapdb.HTreeMap;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by root on 16-7-25.
 */
public class QueryValue implements Serializable {
    private String type;
    private Map<String,String> setKey;
    private Map<String,String> setValue;
    public QueryValue(String type, Map setKey, Map setValue)
    {
        this.type = type;
        this.setKey = setKey;
        this.setValue = setValue;
    }
    public void setType(String type)
    {this.type = type;}
    public String getType()
    {return this.type;}
    public void setSetKey(Map setKey)
    {this.setKey = setKey;}
    public Map getSetKey()
    {return this.setKey;}
    public void setSetValue(Map setValue)
    {this.setValue = setValue;}
    public Map getSetValue()
    {return this.setValue;}
}

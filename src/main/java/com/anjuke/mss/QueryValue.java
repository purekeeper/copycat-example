package com.anjuke.mss;
import org.mapdb.Atomic;
import org.mapdb.HTreeMap;

/**
 * Created by root on 16-7-25.
 */
public class QueryValue {
    private Atomic.String type;
    private HTreeMap setKey;
    private HTreeMap setValue;
    public QueryValue(Atomic.String type,HTreeMap setKey,HTreeMap setValue)
    {
        this.type = type;
        this.setKey = setKey;
        this.setValue = setValue;
    }
    public void setType(Atomic.String type)
    {this.type = type;}
    public Atomic.String getType()
    {return this.type;}
    public void setSetKey(HTreeMap setKey)
    {this.setKey = setKey;}
    public HTreeMap getSetKey()
    {return this.setKey;}
    public void setSetValue(HTreeMap setValue)
    {this.setValue = setValue;}
    public HTreeMap getSetValue()
    {return this.setValue;}
}

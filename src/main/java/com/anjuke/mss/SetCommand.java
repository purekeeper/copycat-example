package com.anjuke.mss;
import io.atomix.copycat.Command;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by root on 16-7-20.
 */
public class SetCommand implements Command<Void> {
    private String dic;
    private String id;
    private String type;
    private Map key;
    private Map value;
    public SetCommand(){}
    public SetCommand(String dic,String id,String type,Map key,Map value)
    {
        key = new HashMap<String,String>();
        value = new HashMap<String,String>();
        this.dic = dic;
        this.id = id;
        this.type = type;
        this.key = key;
        this.value = value;
    }
    public void setType(String type)
    {
        this.type=type;
    }
    public String getType()
    {
        return type;
    }
    public void setDic(String dic)
    {
        this.dic=dic;
    }
    public String getDic()
    {
        return dic;
    }
    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return id;
    }
    public void setKey(Map Key)
    {
        this.key=key;
    }
    public Map getKey()
    {
        return key;
    }
    public void setValue(Map value)
    {
        this.value=value;
    }
    public Map getValue()
    {
        return value;
    }
   // @Override
    public CompactionMode compaction() {
        return CompactionMode.QUORUM;
    }
}

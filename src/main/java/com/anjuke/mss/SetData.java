package com.anjuke.mss;


/**
 * Created by 123 on 2016/7/21.
 */
public class SetData {
    private String dic;
    private String id;
    private String key;
    //private List<String> key;
    private String type;
   // private String key;
    //private List<String> value;
    private String value;
    /*public SetData()
    {
        key = new ArrayList<String>();
        value = new ArrayList<String>();
    }*/
    public void SetType(String type)
    {
        this.type=type;
    }
    public String GetType()
    {
        return type;
    }
    public void SetDic(String dic)
    {
        this.dic=dic;
    }
    public String GetDic()
    {
        return dic;
    }
    public void SetId(String id)
    {
        this.id=id;
    }
    public String GetId()
    {
        return id;
    }

    public void SetKey(String Key)
    {
        this.key=key;
    }
    public String GetKey()
    {
        return key;
    }
    public void SetValue(String value)
    {
        this.value=value;
    }
    public String GetValue()
    {
        return value;
    }
}

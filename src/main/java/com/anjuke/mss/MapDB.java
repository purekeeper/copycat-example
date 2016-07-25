package com.anjuke.mss;

import org.mapdb.*;

import java.util.Map;

/**
 * Created by yangjian on 16-7-25.
 */
public class MapDB {
    private DB db;
    private Atomic.String type;
    private HTreeMap<String,Object[]> tableStore;
    private HTreeMap<String, String>  setKey;
    private HTreeMap<String, String>  setValue;
    public void createDB(String tableName)
    {
        try {
            db = DBMaker.fileDB(tableName).make();
            tableStore = db.hashMap("tableStore")
                    // use array serializer for unknown objects
                    // TODO db.getDefaultSerializer()
                    // or use serializer for specific objects such as String
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.JAVA)
                    .create();
            type = db.atomicString("type").create();
            setKey = db.hashMap("setKey")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
            setValue = db
                    .hashMap("setValue", Serializer.STRING, Serializer.STRING)
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
    public void openDB(String tableName)
    {
        try {
            db = DBMaker.fileDB(tableName).make();
            type = db.atomicString("type").open();
            tableStore = db.hashMap("tableStore")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.JAVA)
                    .open();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }
    public  void setDB(String id, String type, Map<String,String> setKey,Map<String,String> setValue)
    {
        Object[] value = new Object[3];
        for (Map.Entry<String, String> entry : setKey.entrySet()) {

            this.setKey.put(entry.getKey(),entry.getValue());
        }
        for (Map.Entry<String, String> entry : setKey.entrySet()) {
            this.setValue.put(entry.getKey(),entry.getValue());
        }
        value[0] = type;
        value[1] = setKey;
        value[2] = setValue;
        tableStore.put(id,value);
    }
    public Object[] getDataForId(String id)
    {

        Object[] data= tableStore.get(id);
        //type = (Atomic.String)data[0];
        //setKey = (HTreeMap)data[1];
        //setValue = (HTreeMap)data[2];
        return data;
    }

    public void close() {
        try {
            db.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

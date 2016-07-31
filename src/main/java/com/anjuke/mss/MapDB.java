package com.anjuke.mss;
import org.mapdb.*;

import java.util.Map;
/**
 * Created by yangjian on 16-7-25.
 */
public  class MapDB {
    private DB db;
    private HTreeMap<String,QueryValue> tableStore;
    public  void createDB(String tableName)
    {

        try {
            db = DBMaker.fileDB(tableName).make();
            tableStore = db.hashMap("tableStore")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.JAVA)
                    .createOrOpen();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public boolean cheackkeyExist()
    {

    }
    public void openDB(String tableName)
    {
        try {
            db = DBMaker.fileDB(tableName).make();
            tableStore = db.hashMap("tableStore")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.JAVA)
                    .open();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public String getOldKey()
    {

    }
    public  void setDB(String id,QueryValue value)
    {
        tableStore.put(id, value);
    }
    public QueryValue getDataForId(String id)
    {
        if (tableStore.get(id) == null)
            System.out.println("aaaa");
        QueryValue data = (QueryValue) tableStore.get(id);
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

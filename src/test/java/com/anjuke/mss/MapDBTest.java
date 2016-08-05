package com.anjuke.mss;
/**
 * Created by yangjian on 2016/7/24.
 */

import org.mapdb.*;

import java.util.HashMap;
import java.util.Map;


public class MapDBTest {
    private DB db;
    private HTreeMap<String,QueryValue> tableStore;
    public void createDB(String tableName) {
        try {
            db = DBMaker.fileDB(tableName).make();
            tableStore = db.hashMap("tableStore")
                    // use array serializer for unknown objects
                    // TODO db.getDefaultSerializer()
                    // or use serializer for specific objects such as String
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.JAVA)
                    .create();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public void openDB(String tableName) {
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

    public void setDB(String id, QueryValue value) {
        System.out.println("111'");

        tableStore.put(id, value);
        System.out.println("vvv");
    }

    public QueryValue getDataForId(String id) {

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
    public void cun()
    {
        String _id;
        System.out.println("3333");
        System.out.println("2222");
        _id="test";
        Map<String, String> key = new HashMap<String,String>();
        Map<String, String> value = new HashMap<String,String>();
        key.put("name", "yangjian");
        value.put("address", "shanghai");
        QueryValue testValue = new QueryValue(_id, key, value);
        //System.out.println(testValue.getType().get());
        try {
            setDB("comu|111", testValue);
        }
       catch (Exception e)
       {
           e.printStackTrace();
           System.out.println(e.toString());
       }
    }
    public void op()
    {
       // Atomic.String _id = db.atomicString("_id").create();
        //_id.set("comu|111");
        String _id = "comu|111";
        QueryValue res =getDataForId(_id);
        if(res == null)
            System.out.println("wawa");
        System.out.println(res.getType());
       // System.out.println(res.getType().get());
    }
    public  static void main(String[] args)
    {
        MapDBTest test = new MapDBTest();
        try {
           //  test.createDB("filedb1");

           test.openDB("filedb1");
           // test.cun();
            test.op();
        }
        catch (Exception e)
        {//System.out.println(e.toString());
            }
        finally {
            test.close();
        }
    }
}

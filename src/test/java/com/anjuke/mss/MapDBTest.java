package com.anjuke.mss;
/**
 * Created by yangjian on 2016/7/24.
 */

import org.mapdb.*;

public class MapDBTest {
    private DB db;
    private Atomic.String type;
    private HTreeMap<String, QueryValue> tableStore;

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
    public void cun() {
        Atomic.String _id;

   _id = db.atomicString("_id").create();




        System.out.println("2222");
        _id.set("test");
        HTreeMap<String, String> key = db.hashMap("key")
                // use array serializer for unknown objects
                // TODO db.getDefaultSerializer()
                // or use serializer for specific objects such as String
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .create();

        HTreeMap<String, String> value = db.hashMap("value")
                // use array serializer for unknown objects
                // TODO db.getDefaultSerializer()
                // or use serializer for specific objects such as String
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .create();

        key.put("name", "yangjian");
        value.put("address", "shanghai");
        QueryValue testValue = new QueryValue(_id, key, value);

        System.out.println(testValue.getType().get().toString());
        try {
            setDB("comu|111", testValue);
        }
       catch (Exception e)
       {System.out.println(e.toString());}
    }
    public void op()
    {
       // Atomic.String _id = db.atomicString("_id").create();
        //_id.set("comu|111");
        String _id = "comu|111";
        QueryValue res =getDataForId(_id);
        if(res == null)
            System.out.println("wawa");
        System.out.println(res.getType().get());
    }
    public  static void main(String[] args)
    {
        MapDBTest test = new MapDBTest();
        try {
             //test.createDB("filedb1");

            test.openDB("filedb1");
            test.cun();
            //test.op();
        }
        catch (Exception e)
        {//System.out.println(e.toString());
            }
        finally {
            test.close();
        }


    }
}

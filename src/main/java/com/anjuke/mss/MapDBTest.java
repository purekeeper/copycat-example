package com.anjuke.mss;

/**
 * Created by 123 on 2016/7/24.
 */
import org.mapdb.*;
import java.io.*;
public class MapDBTest {
    DB db;
    Atomic.String id;
    Atomic.String type;
    HTreeMap<String, String> setKey;
    //or shorter form
    HTreeMap<String, String> setValue;
    public void opendb()
    {
        try {

            System.out.println("222");

            db = DBMaker.fileDB("testfile1.db").make();

            System.out.println("1111");
            id = db.atomicString("").create();
            type = db.atomicString("").create();
            setKey = db.hashMap("name_of_map")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
            setValue = db
                    .hashMap("some_other_map", Serializer.STRING, Serializer.STRING)
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        }
      catch (Exception e)
      {
          System.out.println(e.toString());
      }
    }
    public void init()
    {
        id.set("testId");
        type.set("testType");
        setKey.put("age","26");
        setKey.put("name","yangjian");
        setValue.put("sex","nan");
        setValue.put("address","luan");
        db.commit();
    }
    public void close()
    {
        try {
            db.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public  static void main(String[] args)
    {
        MapDBTest test = new MapDBTest();
      test.opendb();
        test.init();
        test.close();
    }
}

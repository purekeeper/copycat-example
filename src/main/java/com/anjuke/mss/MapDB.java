package com.anjuke.mss;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Map;
import java.util.concurrent.ExecutionException;
/**
 * Created by yangjian on 16-7-25.
 */
public  class MapDB {
    private String DBName;
    private DB db=null;
    public MapDB(){
    }
    public MapDB(String DBName) {
        this.DBName = DBName;
    }
    public void init() {
        db = DBMaker.fileDB(DBName).make();
        cache = CacheBuilder
                .newBuilder()
                .build(new CacheLoader<String, Map<String,QueryValue>>() {
                    @Override
                    public Map<String,QueryValue> load(String dic) throws Exception {
                        HTreeMap<String, QueryValue> tableStore = db.hashMap(dic)
                                .keySerializer(Serializer.STRING)
                                  .valueSerializer(Serializer.JAVA)
                                .createOrOpen();
                        return tableStore;
                    }
                });
        if(db!=null)
            System.out.println("db init sucess!");
    }
    public void destroy() {
        if(db != null)
            db.close();
    }
    private  LoadingCache<String,Map<String,QueryValue>> cache ;

    public Map<String, QueryValue> getTable(String tableName) {
        try {
            return cache.get(tableName);
        } catch (ExecutionException e) {
            throw new RuntimeException("cache error!",e);
        }
    }
}

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
    private  DB db = DBMaker.fileDB("DB").make();
    private  LoadingCache<String,Map<String,QueryValue>> cache ;
    public MapDB()
    {
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
    }
    public Map<String, QueryValue> getTable(String tableName) {
        try {
            return cache.get(tableName);
        } catch (ExecutionException e) {
            throw new RuntimeException("cache error!",e);
        }
    }
    public void close() {
            db.close();
    }
}

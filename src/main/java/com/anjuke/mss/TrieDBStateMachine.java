package com.anjuke.mss;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.StateMachineExecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 16-7-20.
 */
public class TrieDBStateMachine extends StateMachine
{

    //value=id+keywordtype
    MapDB db = new MapDB();
    Map<String,ConcurrentInvertedRadixTree> trees = new HashMap<String,ConcurrentInvertedRadixTree>();
    @Override
    protected void configure(StateMachineExecutor executor) {
        executor.register(SetCommand.class, this::Set);
        executor.register(QueryCommand.class, this::Get);
    }
    /**
     * Sets the value.    */
    public void Set(Commit<SetCommand> commitSetData) {
        /*
        key word is the key ,id and key word type is value of the tree.
         */
        System.out.println("Setting!!");
        ConcurrentInvertedRadixTree<List> tree = null;//
        Iterator mapIterator = null;
        Map<String,String> keyWordType  = null;
        List<String> treeValue = null;
        String keyType = null;
        String key=null;
        SetCommand setData;
        try {
            setData = commitSetData.operation();
            if(trees.containsKey(setData.getDic()))
                tree = trees.get(setData.getDic());
            else
                tree = new ConcurrentInvertedRadixTree<List>(new DefaultCharArrayNodeFactory());
            keyWordType= setData.getKey();//key:keyWordType
            mapIterator = keyWordType.keySet().iterator();
            while(mapIterator.hasNext())
            {
                key = mapIterator.next().toString();//
                keyType = keyWordType.get(key);//key word type->name,alias,address....
                if((tree.getValueForExactKey(key))==null)
                {
                    treeValue.add(setData.getId());
                    treeValue.add(keyType);
                    tree.put(key,treeValue);
                }
                else {
                    tree.remove(key);
                    tree.put(key,treeValue);
                }
            }
            trees.put(setData.getDic(),tree);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        finally {
            commitSetData.close();
        }
    }
    /**
     * Gets the value.
     */
    public String Get(Commit<QueryCommand> commitQueryData)
    {
        System.out.println("quering!!!");
        String QueryResult=null;
        Iterator searcher;
        String queryText=null;
        QueryCommand queryData= null;
        ConcurrentInvertedRadixTree<List> tree = null;
        try {
            queryData = commitQueryData.operation();
            if(!trees.containsKey(queryData.getDic()))
            {
                return null;
            }
            queryText = queryData.getText();
            tree = trees.get(queryData.getDic());
            searcher = tree.getValuesForKeysContainedIn(queryText).iterator();
            if(searcher.hasNext())
                QueryResult = searcher.next().toString();
            while(searcher.hasNext())
            {
                QueryResult+="|";
                QueryResult+=searcher.next().toString();
            }
        } finally {
            commitQueryData.close();
        }
        return QueryResult;
    }
}

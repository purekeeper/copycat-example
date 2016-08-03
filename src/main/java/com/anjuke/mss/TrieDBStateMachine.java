package com.anjuke.mss;

import com.googlecode.concurrenttrees.common.KeyValuePair;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.StateMachineExecutor;

import javax.annotation.Resource;
import java.util.Map.Entry;

import java.util.*;

/**
 * Created by root on 16-7-20.
 */
public class TrieDBStateMachine extends StateMachine
{
    //value=id+keywordtype
    @Resource
    MapDB db;

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
            //get request data
            SetCommand setData = commitSetData.operation();
            System.out.println(setData);
            //dic not exist Create,exist open
            Map<String,QueryValue> table = db.getTable(setData.getDic());
            //check key exist?
            System.out.println("2222");
            Map<String,String> oldkeyWord=null;
            if(table.containsKey(setData.getId()))
            {
                //get old key:type
                oldkeyWord = table.get(setData.getId()).getSetKey();
            }
            System.out.println("3333");
            table.put(setData.getId(),new QueryValue(setData.getType(),setData.getKey(),setData.getValue()) );
            System.out.println("4444");
            //update tree
            ConcurrentInvertedRadixTree<List> tree;
            if(trees.containsKey(setData.getDic()))
                tree = trees.get(setData.getDic());
            else
                tree = new ConcurrentInvertedRadixTree<List>(new DefaultCharArrayNodeFactory());
            //first delete all old data
            if(oldkeyWord != null) {
                for(Entry<String,String> entry:oldkeyWord.entrySet()) {
                    String oldkey = entry.getKey();//
                    System.out.println("5555");
                    if (tree.getValueForExactKey(oldkey) != null) {
                        tree.remove(oldkey);
                    }
                }
            }
            System.out.println("666");
            //then add all new data
            Map<String,String> newkeyWord = setData.getKey();
            for(Entry<String,String> entry:newkeyWord.entrySet()) {
                System.out.println("777");
                String temp = entry.getKey();
                System.out.println(temp);
                List<String> treeValue = new ArrayList<String>();
                //tree value contain id and key type
                treeValue.add(setData.getId());
                treeValue.add(entry.getValue());
                tree.put(temp,treeValue);
            }
            System.out.println("8888");
            trees.put(setData.getDic(),tree);
            System.out.println("999");
    }
    /**
     * Gets the value.
     */
    public List<ResponseData> Get(Commit<QueryCommand> commitQueryData)
    {
        System.out.println("quering!!!");
        QueryCommand queryData = commitQueryData.operation();
            if(!trees.containsKey(queryData.getDic()))
            {
                System.out.println("aaaa");
                return null;
            }
            String queryText = queryData.getText();
            ConcurrentInvertedRadixTree<List> tree = trees.get(queryData.getDic());
            Iterator searcher = tree.getKeyValuePairsForKeysContainedIn(queryText).iterator();
            System.out.println("cccc");
            List<ResponseData> returnData = null;
            while(searcher.hasNext())
            {
                returnData = new ArrayList<>();
                System.out.println("bbbb");
                ResponseData result = new ResponseData();
                KeyValuePair keyValue = (KeyValuePair)searcher.next();
                //value[0]==id,value[1]==type
                result.setKeyWord(keyValue.getKey().toString());//key
                List<String> value =(List<String>)keyValue.getValue();
                result.setId(value.get(0));//id
                System.out.println(value.get(0)+"|"+value.get(1));
                result.setKeyWordType(value.get(1));//type
                Map<String,QueryValue>table = db.getTable(queryData.getDic());
                QueryValue queryResult = table.get(value.get(0));
                result.setType(queryResult.getType());
                System.out.println("dddd");
                result.setValue(queryResult.getSetValue());
                returnData.add(result);
                System.out.println("eeee");
            }
        return returnData;
    }
}

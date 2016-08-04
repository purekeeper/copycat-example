package com.anjuke.mss;

import com.googlecode.concurrenttrees.common.KeyValuePair;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.StateMachineExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by root on 16-7-20.
 */
@Service
public class TrieDBStateMachine extends StateMachine
{
    //value=id+keywordtype
    @Resource
    private MapDB db;
    Map<String,ConcurrentInvertedRadixTree> trees = new HashMap<String,ConcurrentInvertedRadixTree>();
    @Override
    protected void configure(StateMachineExecutor executor) {
        executor.register(SetCommand.class, this::set);
        executor.register(QueryCommand.class, this::get);
    }
    /**
     * Sets the value.    */
    public void set(Commit<SetCommand> commitSetData) {
        /*
        key word is the key ,id and key word type is value of the tree.
         */
            //get request data
            SetCommand setData = commitSetData.operation();
            //dic not exist Create,exist open
            Map<String,QueryValue> table = db.getTable(setData.getDic());
            //check key exist?
            Map<String,String> oldkeyWord=null;
            if(table.containsKey(setData.getId()))
            {
                //get old key:type
                oldkeyWord = table.get(setData.getId()).getSetKey();
            }
            table.put(setData.getId(),new QueryValue(setData.getType(),setData.getKey(),setData.getValue()) );
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
                    if (tree.getValueForExactKey(oldkey) != null) {
                        tree.remove(oldkey);
                    }
                }
            }
            //then add all new data
            Map<String,String> newkeyWord = setData.getKey();
            for(Entry<String,String> entry:newkeyWord.entrySet()) {
                String temp = entry.getKey();
                List<String> treeValue = new ArrayList<String>();
                //tree value contain id and key type
                treeValue.add(setData.getId());
                treeValue.add(entry.getValue());
                tree.put(temp,treeValue);
            }
            trees.put(setData.getDic(),tree);
    }
    /**
     * Gets the value.
     */
    public List<ResponseData> get(Commit<QueryCommand> commitQueryData)
    {
        QueryCommand queryData = commitQueryData.operation();
            if(!trees.containsKey(queryData.getDic()))
            {
                return null;
            }
            String queryText = queryData.getText();
            ConcurrentInvertedRadixTree<List> tree = trees.get(queryData.getDic());
            Iterator searcher = tree.getKeyValuePairsForKeysContainedIn(queryText).iterator();
            List<ResponseData> returnData = null;
            while(searcher.hasNext())
            {
                returnData = new ArrayList<>();
                ResponseData result = new ResponseData();
                KeyValuePair keyValue = (KeyValuePair)searcher.next();
                result.setKeyWord(keyValue.getKey().toString());//key
                List<String> value =(List<String>)keyValue.getValue();
                result.setId(value.get(0));//id
                result.setKeyWordType(value.get(1));//type
                Map<String,QueryValue>table = db.getTable(queryData.getDic());
                QueryValue queryResult = table.get(value.get(0));
                result.setType(queryResult.getType());
                result.setValue(queryResult.getSetValue());
                returnData.add(result);
            }
        return returnData;
    }
}

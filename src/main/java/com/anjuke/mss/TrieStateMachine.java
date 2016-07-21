package com.anjuke.mss;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radixinverted.ConcurrentInvertedRadixTree;
import com.googlecode.concurrenttrees.radixreversed.ConcurrentReversedRadixTree;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.StateMachineExecutor;

//import javax.naming.directory.SearchResult;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 16-7-20.
 */
public class TrieStateMachine extends StateMachine
{
  //  private Map<String,ConcurrentInvertedRadixTree> trees = new ConcurrentHashMap<String,ConcurrentInvertedRadixTree>();
  ConcurrentInvertedRadixTree<String> tree = new ConcurrentInvertedRadixTree<String>(new DefaultCharArrayNodeFactory());
    //  private Commit<SetCommand> commitSetData;
    @Override
    protected void configure(StateMachineExecutor executor) {
        executor.register(LoadCommand.class,this::LoadDic);
        executor.register(SetCommand.class, this::Set);
        executor.register(QueryCommand.class, this::Get);
    }
    /**
     * Sets the value.    */
    public void LoadDic(Commit<LoadCommand> commitLoadData)
    {
        String fileName;
    //    System.out.println(commitLoadData.operation().GetFileName());
        System.out.println("Load!!");
       // filName= commitLoadData.operation().GetData();
       // String fileName = commitLoadData.operation().GetFileName();
        //fileName="/home/yangjian/workspace/copycat/src/dic/anjuke_sale_100.txt";
        fileName="F:\\copycat\\src\\dic\\anjuke_sale_100.txt";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            String temp;
            int i = 0;
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
                temp = temp.trim();
                if (temp.equals("")) {
                    continue;
                }
                String[] line = temp.split(":", 2);
                String word = line[0];
                String type = "";
                if (line.length == 1) {
                    type = "ban";
                } else {
                    type = line[1];
                }
                tree.put(word, type);
                i++;
            }
            System.out.println("loaded " + fileName + ", contains " + i	+ " words");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
             commitLoadData.close();
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void Set(Commit<SetCommand> commitSetData) {
        System.out.println("Setting!!");
        try {
            ConcurrentInvertedRadixTree<String> tree=null;
            if (commitSetData != null) {
                String data = commitSetData.operation().GetData();
                String[] line = data.split(":", 2);
                tree.remove(line[0]);
                tree.put(line[0],line[1]);
                commitSetData.close();
            }

        } catch (Exception e) {

            throw e;
        }
        finally {
            commitSetData.close();
        }
    }
    /**
     * Gets the value.
     */
    public void Get(Commit<QueryCommand> commitQueryData)
    {
        System.out.println("quering!!!");
        Iterable searcher;
        try {
         //   ConcurrentInvertedRadixTree<String> tree = null;
           // QueryData data = null;
            String data=null;
            if (commitQueryData != null) {
                data = commitQueryData.operation().GetData();
                //tree = trees.get(data.GetDic());

                //searcher =

                System.out.println(data);
                Iterables.toString(tree.getValuesForKeysContainedIn(data));

            }

        } finally {
            commitQueryData.close();
        }
    }
}

package com.anjuke.mss;

/**
 * Created by root on 16-7-20.
 */
import io.atomix.copycat.Query;

/**
 * Value get query.
 *
 * @author <a href="http://github.com/kuujo>Jordan Halterman</a>
 */


public class QueryCommand implements Query<String> {
    private String dic;
    private String text;
    public QueryCommand(){}
    public QueryCommand(String dic,String text)
    {
        this.dic=dic;
        this.text=text;
    }
    public String getDic()
    {
        return dic;
    }
    public String getText()
    {
        return text;
    }
   // @Override
    public ConsistencyLevel consistency() {
        return ConsistencyLevel.LINEARIZABLE_LEASE;
    }

}
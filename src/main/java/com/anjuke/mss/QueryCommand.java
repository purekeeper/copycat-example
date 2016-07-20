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
public class QueryCommand implements Query<Object> {

   // @Override
    public ConsistencyLevel consistency() {
        return ConsistencyLevel.LINEARIZABLE_LEASE;
    }

}
package com.anjuke.mss;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.StateMachineExecutor;

/**
 * Created by root on 16-7-20.
 */
public class TrieStateMachine extends StateMachine {
    private Commit<SetCommand> value;

    @Override
    protected void configure(StateMachineExecutor executor) {
        executor.register(SetCommand.class, this::set);
        executor.register(QueryCommand.class, this::get);
    }
    /**
     * Sets the value.
     */
    private Object set(Commit<SetCommand> commit) {
        try {
            Commit<SetCommand> previous = value;
            value = commit;
            if (previous != null) {
                Object result = previous.operation().value();
                previous.close();
                return result;
            }
            return null;
        } catch (Exception e) {
            commit.close();
            throw e;
        }
    }

    /**
     * Gets the value.
     */
    private Object get(Commit<QueryCommand> commit) {
        try {
            return value != null ? value.operation().value() : null;
        } finally {
            commit.close();
        }
    }
}

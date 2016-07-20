package com.anjuke.mss;

import io.atomix.copycat.Command;

/**
 * Created by root on 16-7-20.
 */
public class SetCommand implements Command<Object> {
    private final Object value;

    public SetCommand(Object value) {
        this.value = value;
    }

    /**
     * Returns the value.
     */
    public Object value() {
        return value;
    }

   // @Override
    public CompactionMode compaction() {
        return CompactionMode.QUORUM;
    }

}

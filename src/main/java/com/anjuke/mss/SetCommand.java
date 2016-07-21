package com.anjuke.mss;

import io.atomix.copycat.Command;

/**
 * Created by root on 16-7-20.
 */
public class SetCommand implements Command<Void> {
   // private  SetData data;
private String data;
    public SetCommand(String data) {
        this.data = data;
    }

    /**
     * Returns the value.
     */
    public String GetData() {
        return data;
    }

   // @Override
    public CompactionMode compaction() {
        return CompactionMode.QUORUM;
    }

}

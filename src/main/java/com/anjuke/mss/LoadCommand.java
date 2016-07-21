package com.anjuke.mss;

import io.atomix.copycat.Command;

/**
 * Created by root on 16-7-21.
 */
public class LoadCommand implements Command<Void> {
    private final String fileName;

    public LoadCommand(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the value.
     */
    public String GetFileName() {
        return fileName;
    }

    // @Override
    public CompactionMode compaction() {
        return CompactionMode.QUORUM;
    }
}

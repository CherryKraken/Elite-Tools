package com.connorboyle.elitetools;

import java.io.Serializable;

/**
 * Created by Connor Boyle on 2017-06-16.
 */

public class NoteEntry implements Serializable{
    long id;
    String title;
    String text;
    public long modified;

    NoteEntry(long id, String title, String text, long modified) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.modified = modified;
    }

    NoteEntry(String title, String text, long modified) {
        this(-1, title, text, modified);
    }
}

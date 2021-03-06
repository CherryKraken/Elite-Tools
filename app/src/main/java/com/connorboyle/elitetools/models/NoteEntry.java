package com.connorboyle.elitetools.models;

import java.io.Serializable;

/**
 * Created by Connor Boyle on 2017-06-16.
 */

public class NoteEntry implements Serializable{
    long id;
    public String title;
    public String text;
    public long modified;

    public NoteEntry(long id, String title, String text, long modified) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.modified = modified;
    }

    public NoteEntry(String title, String text, long modified) {
        this(-1, title, text, modified);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteEntry noteEntry = (NoteEntry) o;

        return id == noteEntry.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}

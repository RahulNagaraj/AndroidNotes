package com.rahulnagaraj.androidnotes;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class Note implements Serializable {
    private String notesTitle;
    private String lastUpdatedDateTimestamp;
    private String notesDescription;

    public Note() {}

    public Note(String notesTitle, String lastUpdatedDateTimestamp, String notesDescription) {
        this.notesTitle = notesTitle;
        this.lastUpdatedDateTimestamp = lastUpdatedDateTimestamp;
        this.notesDescription = notesDescription;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }

    public String getLastUpdatedDateTimestamp() {
        return lastUpdatedDateTimestamp;
    }

    public void setLastUpdatedDateTimestamp(String lastUpdatedDateTimestamp) {
        this.lastUpdatedDateTimestamp = lastUpdatedDateTimestamp;
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public void setNotesDescription(String notesDescription) {
        this.notesDescription = notesDescription;
    }

    @Override
    public String toString() {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("notesTitle").value(getNotesTitle());
            jsonWriter.name("notesDescription").value(getNotesDescription());
            jsonWriter.name("lastUpdatedDateTimestamp").value(getLastUpdatedDateTimestamp());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

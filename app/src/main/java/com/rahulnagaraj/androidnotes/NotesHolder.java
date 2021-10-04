package com.rahulnagaraj.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesHolder extends RecyclerView.ViewHolder {
    TextView notesTitle;
    TextView lastUpdatedDateTimestamp;
    TextView notesDescription;


    public NotesHolder(@NonNull View itemView) {
        super(itemView);
        notesTitle = itemView.findViewById(R.id.notesTitle);
        lastUpdatedDateTimestamp = itemView.findViewById(R.id.lastUpdatedDateTimestamp);
        notesDescription = itemView.findViewById(R.id.notesDescription);
    }
}

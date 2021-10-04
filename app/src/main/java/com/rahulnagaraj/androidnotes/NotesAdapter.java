package com.rahulnagaraj.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesHolder> {
    private List<Note> notesList;
    private MainActivity mainActivity;

    public NotesAdapter(List<Note> notesList, MainActivity mainActivity) {
        this.notesList = notesList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_card, parent, false);

        viewItem.setOnClickListener((View.OnClickListener) mainActivity);
        viewItem.setOnLongClickListener((View.OnLongClickListener) mainActivity);

        return new NotesHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        Note note = notesList.get(position);

        String lastUpdatedDateTimestamp = note.getLastUpdatedDateTimestamp();
        String notesDescription = note.getNotesDescription();
        String notesTitle = note.getNotesTitle();


        if (notesTitle != null && notesTitle.length() > 80) {
            notesTitle = notesTitle.substring(0, 80) + "...";
        }
        holder.notesTitle.setText(notesTitle);
        if (lastUpdatedDateTimestamp != null) {
            Date date = new Date(lastUpdatedDateTimestamp);
            DateFormat dateFormat = new SimpleDateFormat("E MMM dd, hh:mm a");
            holder.lastUpdatedDateTimestamp.setText(dateFormat.format(date));
        }
        if (notesDescription != null && notesDescription.length() > 80) {
            notesDescription = notesDescription.substring(0, 80) + "...";
        }
        holder.notesDescription.setText(notesDescription);

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}

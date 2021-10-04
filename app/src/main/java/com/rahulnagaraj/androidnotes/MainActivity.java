package com.rahulnagaraj.androidnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private final List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private boolean isEditNote = false;
    private int editIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        notesAdapter = new NotesAdapter(noteList, this);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);

        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteList.addAll(loadFile());


        setAppTitle();
    }

    @Override
    public void onClick(View view) {
        int index = recyclerView.getChildLayoutPosition(view);
        Note note = noteList.get(index);
        Intent intent = new Intent(this, AddNewNote.class);
        intent.putExtra(getString(R.string.intent_note_key), note);
        isEditNote = true;
        editIndex = index;
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        int index = recyclerView.getChildLayoutPosition(view);
        Note note = noteList.get(index);
        displayDeleteDialog(note, index);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoMenu:
                Intent i = new Intent(this, InfoPageActivity.class);
                startActivity(i);
                return true;
            case R.id.addNoteMenu:
                isEditNote = false;
                Intent intent = new Intent(this, AddNewNote.class);
                activityResultLauncher.launch(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Note> loadFile() {
        ArrayList<Note> noteArrayList = new ArrayList<>();

        try {
            InputStream inputStream = getApplicationContext()
                    .openFileInput(getString(R.string.filename));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            Log.d(TAG, "loadFile: " + sb);

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0;  i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String noteTitle = jsonObject.getString("notesTitle");
                String noteDescription = jsonObject.getString("notesDescription");
                String lastUpdatedDateTimestamp = jsonObject.getString("lastUpdatedDateTimestamp");
                Note note = new Note(noteTitle, lastUpdatedDateTimestamp, noteDescription);
                noteArrayList.add(note);
            }
        } catch (IOException | JSONException e) {
            Log.d(TAG, "loadFile Error : " + e.getMessage());
            e.printStackTrace();
        }

        return noteArrayList;
    }

    public void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            Note note = (Note) data.getSerializableExtra(getString(R.string.intent_note_key));

            if (note != null) {
                if (isEditNote) {
                    isEditNote = false;
                    noteList.remove(editIndex);
                    notesAdapter.notifyItemRemoved(editIndex);
                    noteList.add(0, note);
                    notesAdapter.notifyItemInserted(0);
                } else {
                    noteList.add(0, note);
                    notesAdapter.notifyItemInserted(0);
                }

                saveNoteAsJson();
            }
        } else {
            Log.d(TAG, "onActivityResult: result Code: " + result.getResultCode());
        }
    }

    private void saveNoteAsJson() {
        try {
            FileOutputStream fileOutputStream = getApplicationContext()
                    .openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.print(noteList);
            setAppTitle();
            printWriter.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayDeleteDialog(Note note, int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String alertTitle = "Delete Note '" + note.getNotesTitle() + "'?";
        builder.setTitle(alertTitle);

        builder.setPositiveButton(getString(R.string.positive_alert_button), (dialogInterface, i) -> {
            deleteNote(note, index);
        });

        builder.setNegativeButton(getString(R.string.negative_alert_button), (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteNote(Note note, int index) {
        noteList.remove(note);
        notesAdapter.notifyItemRemoved(index);
        saveNoteAsJson();
        Toast.makeText(this, getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
    }

    private void setAppTitle() {
        ActionBar actionBar = getSupportActionBar();
        String appTitle = getString(R.string.app_name);
        if (actionBar != null) {
            actionBar.setTitle(appTitle + " (" + noteList.size() + ")");
        }
    }
}
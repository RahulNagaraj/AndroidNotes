package com.rahulnagaraj.androidnotes;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class AddNewNote extends AppCompatActivity {

    private static final String TAG = "AddNewNote";
    private EditText noteTitleEditText;
    private EditText noteDescriptionEditText;

    private String noteTitle;
    private String noteDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteDescriptionEditText = findViewById(R.id.noteDescriptionEditText);
        noteDescriptionEditText.setMovementMethod(new ScrollingMovementMethod());

        if (getIntent().hasExtra(getString(R.string.intent_note_key))) {
            Note note = (Note) getIntent().getSerializableExtra(getString(R.string.intent_note_key));
            noteTitle = note.getNotesTitle();
            noteDescription = note.getNotesDescription();
            noteTitleEditText.setText(noteTitle);
            noteDescriptionEditText.setText(noteDescription);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                if (getNoteTitleEditTextValue().isEmpty()) {
                    displayNoTitleAlert();
                } else if (getNoteTitleEditTextValue().equals(noteTitle)
                        && getNoteDescriptionEditTextValue().equals(noteDescription)) {
                    super.onBackPressed();
                } else {
                    addNewNote();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getNoteTitleEditTextValue().isEmpty()) {
            displayNoTitleAlert();
        } else {
            checkIsDirty();
        }
    }

    private void addNewNote() {
        String noteTitle = getNoteTitleEditTextValue();
        String noteDescription = getNoteDescriptionEditTextValue();
        Date date = new Date();
        Note note;

        if (noteTitle.isEmpty() && noteDescription.isEmpty()) {
            note = null;
        } else {
            note = new Note(noteTitle.trim(), date.toString(), noteDescription.trim());
        }

        Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra(getString(R.string.intent_note_key), note);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void checkIsDirty() {
        String updatedNoteTitle = getNoteTitleEditTextValue();
        String updatedNoteDescription = getNoteDescriptionEditTextValue();

        if (updatedNoteTitle.isEmpty() && updatedNoteDescription.isEmpty()) {
            super.onBackPressed();
            return;
        }
        if (updatedNoteTitle.isEmpty() && !updatedNoteDescription.isEmpty()) {
            displayNoTitleAlert();
            return;
        }
        if (!updatedNoteTitle.equals(noteTitle) || !updatedNoteDescription.equals(noteDescription)) {
            displayIsDirtyAlert();
            return;
        }

        super.onBackPressed();
    }

    private String getNoteTitleEditTextValue() {
        return noteTitleEditText.getText().toString();
    }

    private String getNoteDescriptionEditTextValue() {
        return noteDescriptionEditText.getText().toString();
    }

    private void displayIsDirtyAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String alertTitle = "Your note is not saved";
        String message = getNoteTitleEditTextValue().length() > 80
                ? getNoteTitleEditTextValue().substring(0,60) + "..."
                : getNoteTitleEditTextValue();
        String alertMessage = "Save note '" + message + "'?";

        builder.setTitle(alertTitle);
        builder.setMessage(alertMessage);

        builder.setPositiveButton(getString(R.string.positive_alert_button), (dialogInterface, i) -> {
            addNewNote();
        });

        builder.setNegativeButton(getString(R.string.negative_alert_button), (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayNoTitleAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String alertTitle = "Your note has no title";
        String alertMessage = "Note will not be saved without title";

        builder.setTitle(alertTitle);
        builder.setMessage(alertMessage);

        builder.setPositiveButton(getString(R.string.positive_save_note_alert_button), (dialogInterface, i) -> {
            super.onBackPressed();
        });

        builder.setNegativeButton(getString(R.string.negative_save_note_alert_button), (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

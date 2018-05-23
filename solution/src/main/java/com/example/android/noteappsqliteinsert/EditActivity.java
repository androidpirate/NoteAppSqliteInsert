package com.example.android.noteappsqliteinsert;

import android.content.Intent;
import com.example.android.noteappsqliteinsert.db.NoteDbHelper;
import com.example.android.noteappsqliteinsert.model.Note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra-note";
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Get the note from intent extra
        if(getIntent() != null) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
        }

        EditText title = findViewById(R.id.et_title);
        if(note.getTitle() != null) {
            title.setText(note.getTitle());
        }
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setTitle(s.toString());
            }
        });

        EditText description = findViewById(R.id.et_description);
        if(note.getDescription() != null) {
            description.setText(note.getDescription());
        }
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setDescription(s.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note_action:
                NoteDbHelper noteDbHelper = NoteDbHelper.getInstance(this);
                noteDbHelper.insertNote(note);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

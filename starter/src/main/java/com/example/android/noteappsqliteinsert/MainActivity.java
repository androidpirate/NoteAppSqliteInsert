package com.example.android.noteappsqliteinsert;

import android.content.Intent;
import com.example.android.noteappsqliteinsert.adapter.NoteAdapter;
import com.example.android.noteappsqliteinsert.db.NoteDbHelper;
import com.example.android.noteappsqliteinsert.model.Note;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NoteAdapter.NoteClickListener {
    private TextView emptyListMessage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoteDbHelper noteDbHelper = NoteDbHelper.getInstance(this);
        // Get all notes from database
        List<Note> notes = noteDbHelper.getAllNotes();
        recyclerView = findViewById(R.id.rv_note_list);
        emptyListMessage = findViewById(R.id.tv_empty_list);
        if(notes.size() == 0) {
            displayEmptyListMessage();
        } else {
            displayRecyclerView();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            // Set adapter
            NoteAdapter adapter = new NoteAdapter(notes, this);
            recyclerView.setAdapter(adapter);
        }
    }

    private void displayRecyclerView() {
        emptyListMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void displayEmptyListMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        emptyListMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Note note) {
        // Create an intent to start DetailActivity
        Intent intent = new Intent(this, DetailActivity.class);
        // Add note as an intent extra
        intent.putExtra(DetailActivity.EXTRA_NOTE, note);
        startActivity(intent);
    }
}

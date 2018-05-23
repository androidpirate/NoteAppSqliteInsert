package com.example.android.noteappsqliteinsert;

import com.example.android.noteappsqliteinsert.model.Note;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra-note";
    private Note note;
    private TextView title;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Enable up button in ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get the note data from intent extra
        if(getIntent().getExtras() != null) {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
        }
        // Subscribe ui
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        if(note.getTitle() != null && note.getDescription() != null) {
            title.setText(note.getTitle());
            description.setText(note.getDescription());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Returns back to MainActivity
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

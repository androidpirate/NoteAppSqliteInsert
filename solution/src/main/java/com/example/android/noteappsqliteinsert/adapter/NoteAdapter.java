package com.example.android.noteappsqliteinsert.adapter;

import com.example.android.noteappsqliteinsert.R;
import com.example.android.noteappsqliteinsert.model.Note;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter class to bind data to RecyclerView.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> notes;
    private NoteClickListener listener;

    public interface NoteClickListener {
        // Add a note argument for onClick() method
        void onClick(Note note);
    }

    public NoteAdapter(List<Note> notes, NoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.bindNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        if(notes.size() == 0) {
            return 0;
        } else {
            return notes.size();
        }
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;

        public NoteHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
            // Call NoteClickListener's onClick when an item is clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(notes.get(getAdapterPosition()));
                }
            });
        }

        public void bindNote(Note note) {
            if(note.getTitle() != null && note.getDescription() != null) {
                title.setText(note.getTitle());
                description.setText(note.getDescription());
            } else {
                throw new NullPointerException("Note fields are null.");
            }
        }
    }
}

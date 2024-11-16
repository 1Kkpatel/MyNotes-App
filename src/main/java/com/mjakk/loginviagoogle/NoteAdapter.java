package com.mjakk.loginviagoogle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final ArrayList<Note> notes;
    private final NoteClickListener clickListener;
    private final LongClickListener longClickListener;
    private List<Note> noteList = new ArrayList<>(); // Always initialize as a new ArrayList
    private List<Note> noteListFull = new ArrayList<>(); // Copy of the full list


    public NoteAdapter(ArrayList<Note> notes, NoteClickListener clickListener, LongClickListener longClickListener,List<Note> noteList) {
        this.notes = notes;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        if (noteList != null) {
            this.noteList = noteList;
            this.noteListFull = new ArrayList<>(noteList); // Initialize the backup list
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
//        Note note = notes.get(position);
//        holder.tvNote.setText(note.getText());
//
//        holder.itemView.setOnClickListener(v -> clickListener.onNoteClick(note));
//        holder.itemView.setOnLongClickListener(v -> {
//            longClickListener.onLongClick(position);
//            return true;
//        });
//    }
@Override
public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
    Note note = notes.get(position);
    holder.tvTitle.setText(note.getTitle());
    holder.tvNote.setText(note.getText());

    holder.itemView.setOnClickListener(v -> clickListener.onNoteClick(note));
    holder.itemView.setOnLongClickListener(v -> {
        longClickListener.onLongClick(position);
        return true;
    });
}


    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Filter method to search notes
    public void filter(String query) {
        if (noteListFull == null) return; // Safety check
        noteList.clear();
        if (query.isEmpty()) {
            noteList.addAll(noteListFull); // Restore the full list if the query is empty
        } else {
            for (Note note : noteListFull) {
                if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        note.getText().toLowerCase().contains(query.toLowerCase())) {
                    noteList.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

//    static class NoteViewHolder extends RecyclerView.ViewHolder {
//        TextView tvNote;
//
//        public NoteViewHolder(View itemView) {
//            super(itemView);
//            tvNote = itemView.findViewById(R.id.tv_note);
//        }
//    }
static class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvNote;

    public NoteViewHolder(View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_note_title);
        tvNote = itemView.findViewById(R.id.tv_note_text);
    }
}

    interface NoteClickListener {
        void onNoteClick(Note note);
    }

    interface LongClickListener {
        void onLongClick(int position);
    }
}

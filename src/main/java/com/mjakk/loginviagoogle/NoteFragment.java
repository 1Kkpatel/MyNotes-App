package com.mjakk.loginviagoogle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> noteList = new ArrayList<>();
    private NoteDatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        db = new NoteDatabaseHelper(requireContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        View btnCreateNote = view.findViewById(R.id.btnOpenDialog);

        // Set up the Logout button
//        Button btnLogout = view.findViewById(R.id.btnLogout);
//        btnLogout.setOnClickListener(v -> logout());

        btnCreateNote.setOnClickListener(v -> showNoteDialog(null));
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();

        loadNotes();

        // Handle search filtering
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteAdapter.filter(query); // Filter notes when the search is submitted
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.filter(newText); // Filter notes dynamically as the text changes
                return false;
            }
        });


        return view;
    }



    private void loadNotes() {
        // Fetch all notes from the database and set up RecyclerView
        noteList = db.getAllNotes();
        noteAdapter = new NoteAdapter(noteList, this::showNoteDialog, this::deleteNoteDialog, noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noteAdapter);
    }

    private void showNoteDialog(Note note) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_note, null);
        dialogBuilder.setView(dialogView);

        EditText etNoteTitle = dialogView.findViewById(R.id.et_note_title);
        EditText etNoteText = dialogView.findViewById(R.id.et_note_text);

        if (note != null) {
            etNoteTitle.setText(note.getTitle());
            etNoteText.setText(note.getText());
        }

        dialogBuilder.setPositiveButton(note == null ? "Create" : "Update", (dialog, which) -> {
            String noteTitle = etNoteTitle.getText().toString().trim();
            String noteText = etNoteText.getText().toString().trim();

            if (note == null) {
                // Create new note
                db.insertNote(new Note(noteTitle, noteText));
            } else {
                // Update existing note
                note.setTitle(noteTitle);
                note.setText(noteText);
                db.updateNote(note);
            }
            loadNotes();  // Refresh RecyclerView
        });

        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.show();
    }

    private void deleteNoteDialog(int position) {
        // Confirm deletion with a dialog
        new AlertDialog.Builder(requireContext())
                .setMessage("Delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteNoteById(noteList.get(position).getId());
                    loadNotes();  // Refresh notes list after deletion
                    Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
    //for logout
//    private void logout() {
//        // Clear login state
//        requireActivity()
//                .getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//                .edit()
//                .putBoolean("isLoggedIn", false)
//                .apply();
//
//        // Navigate to LoginFragment
//        requireActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, new LoginFragment())
//                .commit();
//    }
}

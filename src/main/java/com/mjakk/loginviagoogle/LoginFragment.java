package com.mjakk.loginviagoogle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class LoginFragment extends Fragment {
    

    private static final int RC_SIGN_IN = 123; // Request code for Google Sign-In
    private GoogleSignInClient googleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button btnGoogleSignIn = view.findViewById(R.id.btn_google_sign_in);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        // Set the button click listener
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());

        return view;
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(); // Successfully signed in
            if (account != null) {
                String displayName = account.getDisplayName();
                Toast.makeText(getContext(), "Welcome, " + displayName, Toast.LENGTH_SHORT).show();

                // Navigate to MainActivity

                navigateToNoteFragment();
            }
        } catch (Exception e) {
            // Sign-in failed
            Toast.makeText(getContext(), "Sign-in failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

//    private void openMainActivity() {
//        Intent intent = new Intent(requireContext(), MainActivity.class);
//        startActivity(intent);
//        requireActivity().finish(); // Close the LoginActivity or hosting activity
//    }
    private void navigateToNoteFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new NoteFragment())
                .commit();
    }

}

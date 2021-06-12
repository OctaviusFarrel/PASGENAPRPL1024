package com.example.pasrpl1024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pasrpl1024.utils.AnimeAdapter;
import com.example.pasrpl1024.utils.UserAnime;
import com.example.pasrpl1024.utils.UserClass;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static SignUp signUp;

    public static SignUp getSignUp() {
        return signUp;
    }

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference users = firebaseDatabase.getReference("users");
    private final DatabaseReference usersAnime = firebaseDatabase.getReference("usersWatchedAnime");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUp = this;
        if (MainActivity.getUserAnime() != null && MainActivity.getUserAnime().getUser() != null) {
            Task<DataSnapshot> reference = users.child(MainActivity.getUserAnime().getUser().getEmail()).get();
            reference.addOnSuccessListener(dataSnapshot -> {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                if (userClass == null) return;
                startActivity(new Intent(signUp, SeasonedAnime.class));
                Toast.makeText(SignUp.getSignUp(), "You're already logged in!", Toast.LENGTH_SHORT).show();
            });
            reference.addOnFailureListener(command -> Toast.makeText(this, command.getMessage(), Toast.LENGTH_LONG).show());
        }
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.sign_up_button).setOnClickListener(this);
        findViewById(R.id.sign_up_login).setOnClickListener(this);
        getSupportActionBar().setTitle("Sign Up");
    }

    @Override
    public void onClick(View v) {
        if (v.getClass() == MaterialButton.class) {
            if (((EditText) findViewById(R.id.sign_up_email)).getText().toString() == null || ((EditText) findViewById(R.id.sign_up_email)).getText().toString().isEmpty()) {
                Toast.makeText(SignUp.getSignUp(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (((EditText) findViewById(R.id.sign_up_username)).getText().toString() == null || ((EditText) findViewById(R.id.sign_up_username)).getText().toString().isEmpty()) {
                Toast.makeText(SignUp.getSignUp(), "Username can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (((EditText) findViewById(R.id.sign_up_password)).getText().toString() == null || ((EditText) findViewById(R.id.sign_up_password)).getText().toString().isEmpty()) {
                Toast.makeText(SignUp.getSignUp(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Task<DataSnapshot> reference = users.child(((EditText) findViewById(R.id.sign_up_email)).getText().toString().replace(".","_")).get();
            reference.addOnSuccessListener(dataSnapshot -> {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                if (userClass != null) {
                    Toast.makeText(SignUp.getSignUp(), "Account has already exist!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MainActivity.getUserAnime() != null && MainActivity.getUserAnime().getUser() != null) {
                    Toast.makeText(SignUp.getSignUp(), "You already logged in!", Toast.LENGTH_SHORT).show();
                    return;
                }
                userClass = new UserClass(((EditText) findViewById(R.id.sign_up_username)).getText().toString(), ((EditText) findViewById(R.id.sign_up_password)).getText().toString(), "https://www.dropbox.com/s/ovhd0gahq5wq2dz/userTest.jpg?dl=1");
                userClass.setEmail(((EditText) findViewById(R.id.sign_up_email)).getText().toString().replace(".","_"));
                users.child(((EditText) findViewById(R.id.sign_up_email)).getText().toString().replace(".","_")).setValue(userClass);
                usersAnime.child(((EditText) findViewById(R.id.sign_up_email)).getText().toString().replace(".","_")).setValue("Test");
                MainActivity.setUserAnime(new UserAnime(userClass, new ArrayList<>()));
                startActivity(new Intent(signUp, SeasonedAnime.class));
                Toast.makeText(SignUp.getSignUp(), "You're logged in!", Toast.LENGTH_SHORT).show();
                AsyncTask.execute(() -> {
                    AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.WATCHING_ANIME);
                    AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.WATCHED_ANIME);
                    AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.SEASONED_ANIME);
                });
            });
            reference.addOnFailureListener(command -> Toast.makeText(this, command.getMessage(), Toast.LENGTH_LONG).show());
        } else if (v.getClass() == MaterialTextView.class) {
            startActivity(new Intent(this, Login.class));
        } else {
            Toast.makeText(SignUp.getSignUp(), "Invalid!", Toast.LENGTH_LONG).show();
        }
    }
}
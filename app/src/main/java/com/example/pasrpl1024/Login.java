package com.example.pasrpl1024;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pasrpl1024.utils.AnimeAdapter;
import com.example.pasrpl1024.utils.AnimeData;
import com.example.pasrpl1024.utils.AnimeUserData;
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
import java.util.List;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private static Login login;

    public static Login getLogin() {
        return login;
    }

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference users = firebaseDatabase.getReference("users");
    private final DatabaseReference usersAnime = firebaseDatabase.getReference("usersWatchedAnime");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = this;
        if (MainActivity.getUserAnime() != null && MainActivity.getUserAnime().getUser() != null) {
            Task<DataSnapshot> task = users.child(MainActivity.getUserAnime().getUser().getEmail()).get();
            task.addOnSuccessListener(dataSnapshot -> {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                if (userClass == null) return;
                startActivity(new Intent(login, SeasonedAnime.class));
                Toast.makeText(SignUp.getSignUp(), "You're already logged in!", Toast.LENGTH_SHORT).show();
            });
        }
        setContentView(R.layout.activity_log_in);
        findViewById(R.id.log_in_button).setOnClickListener(this);
        findViewById(R.id.log_in_signup).setOnClickListener(this);
        getSupportActionBar().setTitle("Login");
    }

    @Override
    public void onClick(View v) {
        if (v.getClass() == MaterialButton.class) {
            if (((EditText) findViewById(R.id.log_in_email)).getText().toString() == null || ((EditText) findViewById(R.id.log_in_email)).getText().toString().isEmpty()) {
                Toast.makeText(Login.getLogin(), "Email can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (((EditText) findViewById(R.id.log_in_password)).getText().toString() == null || ((EditText) findViewById(R.id.log_in_password)).getText().toString().isEmpty()) {
                Toast.makeText(SignUp.getSignUp(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Task<DataSnapshot> reference = users.child(((EditText) findViewById(R.id.log_in_email)).getText().toString().replace(".","_")).get();
            reference.addOnSuccessListener(dataSnapshot -> {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                if (userClass == null) {
                    Toast.makeText(Login.getLogin(), "Account doesn't exist!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!userClass.getPassword().equals(((EditText) findViewById(R.id.log_in_password)).getText().toString())) {
                    Toast.makeText(Login.getLogin(), "Password doesn't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (MainActivity.getUserAnime() != null && MainActivity.getUserAnime().getUser() != null) {
                    Toast.makeText(Login.getLogin(), "You already logged in!", Toast.LENGTH_SHORT).show();
                    return;
                }
                userClass.setEmail(dataSnapshot.getKey());
                Task<DataSnapshot> getData = usersAnime.child(((EditText) findViewById(R.id.log_in_email)).getText().toString().replace(".","_")).get();
                getData.addOnSuccessListener(dataSnapshot1 -> {
                    if (dataSnapshot1.getValue() == null) {
                        MainActivity.setUserAnime(new UserAnime(userClass, new ArrayList<>()));
                        startActivity(new Intent(login, SeasonedAnime.class));
                    } else if (dataSnapshot1.getValue() instanceof String) {
                        MainActivity.setUserAnime(new UserAnime(userClass, new ArrayList<>()));
                        startActivity(new Intent(login, SeasonedAnime.class));
                    } else {
                        List<AnimeUserData> animeDataList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                            AnimeUserData animeData = snapshot.getValue(AnimeUserData.class);
                            assert animeData != null;
                            animeData.setId(snapshot.getKey());
                            animeDataList.add(animeData);
                        }
                        MainActivity.setUserAnime(new UserAnime(userClass, animeDataList));
                        startActivity(new Intent(login, SeasonedAnime.class));
                    }
                    Toast.makeText(Login.getLogin(), "You're logged in!", Toast.LENGTH_SHORT).show();
                    AsyncTask.execute(() -> {
                        AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.WATCHING_ANIME);
                        AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.WATCHED_ANIME);
                        AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.SEASONED_ANIME);
                    });
                });
                getData.addOnFailureListener(command -> Toast.makeText(this, command.getMessage(), Toast.LENGTH_LONG).show());
            });
            reference.addOnFailureListener(command -> Toast.makeText(this, command.getMessage(), Toast.LENGTH_LONG).show());
        } else if (v.getClass() == MaterialTextView.class) {
            startActivity(new Intent(this, SignUp.class));
        } else {
            Toast.makeText(Login.getLogin(), "Invalid!", Toast.LENGTH_LONG).show();
        }
    }
}
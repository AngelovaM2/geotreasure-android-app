package com.example.geotreasure.SidePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geotreasure.Connectives;
import com.example.geotreasure.R;
import com.example.geotreasure.User.SplashPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    ImageButton mBack;
    TextView mEditProfile, mLogout, mDelete;
    FirebaseAuth mAuth;
    //Profile page functionality
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mBack = findViewById(R.id.go_back_profile);
        mEditProfile = findViewById(R.id.edit_prof);
        mLogout = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        mDelete = findViewById(R.id.delete_account);

        mBack.setOnClickListener(view -> finish());

        mEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Edit_Profile.class);
            startActivity(intent);
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), SplashPage.class);
                startActivity(intent);
                finish();
                Toast.makeText(Profile.this, "Logout successful", Toast.LENGTH_SHORT).show();
            }
        });
        //Internet connection
        Connectives connectionDetector = new Connectives(this);
        if (!connectionDetector.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet connection");
            builder.setMessage("Please check your internet connection and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AlertDialog.Builder alert = new AlertDialog.Builder(Profile.this);

                alert.setTitle("Delete Account");
                alert.setMessage("Provide password to delete account");
                // Set an EditText view to get user input
                final EditText input = new EditText(alert.getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), input.getText().toString());

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Intent intent = new Intent(getApplicationContext(), SplashPage.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), "Account Deletion cancelled", Toast.LENGTH_SHORT).show();

                    }
                });

                alert.show();

            }
        });

    }
}
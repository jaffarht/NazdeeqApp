package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nazdeeqapp.DeleteLater.DriverMapActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText mEmailField;
    private EditText mPasswordField;
    private FloatingActionButton mSignInButton;
    private Button createAccountButton;
    private FirebaseAuth fAuth;
    //private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fAuth = FirebaseAuth.getInstance();

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mSignInButton = (FloatingActionButton) findViewById(R.id.button_sign_in);
        createAccountButton = (Button)findViewById(R.id.signup2);
       // progressBar = findViewById(R.id.progressBar2);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    mEmailField.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    mPasswordField.setError("Password is Required");
                    return;
                }

                if (password.length() < 6)
                {
                    mPasswordField.setError("Password must be greater than 6 Characters");
                    return;
                }

               // progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignInActivity.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        else
                        {
                            Toast.makeText(SignInActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           // progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }




        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }

            public void openNewActivity()
            {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

}

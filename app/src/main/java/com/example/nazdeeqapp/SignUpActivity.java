package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {



    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private EditText FirstEditText;
    private EditText EmailEditText, PasswordEditText;
    private EditText phoneEditText;
    private FloatingActionButton mSignUpButton;
    private Button SignInBtn;
    //private ProgressBar progressBar;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirstEditText = (EditText) findViewById(R.id.first_name);
        EmailEditText = (EditText) findViewById(R.id.create_email);
        PasswordEditText = (EditText) findViewById(R.id.create_password);
        phoneEditText = (EditText) findViewById(R.id.create_phone);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        SignInBtn = findViewById(R.id.signInbtn);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();

        mSignUpButton = findViewById(R.id.save_information);


      /*  if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            finish();
        }
*/
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = EmailEditText.getText().toString().trim();
                String password = PasswordEditText.getText().toString().trim();
                final String name = FirstEditText.getText().toString();
                final String phone = phoneEditText.getText().toString();

                if (TextUtils.isEmpty(name))
                {
                    FirstEditText.setError("Name is Required");
                    return;
                }

                if (TextUtils.isEmpty(email))
                {
                    EmailEditText.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password))
                {
                    PasswordEditText.setError("Password is Required");
                    return;
                }

                if (password.length() < 6)
                {
                    PasswordEditText.setError("Password must be greater than 6 Characters");
                    return;
                }

               // progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",name);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("userID",userID);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            }
        });


    }


}

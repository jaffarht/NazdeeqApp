package com.example.nazdeeqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.android.volley.VolleyLog.TAG;

public class CustomerProfile extends AppCompatActivity {

    private TextView FirstEditText;
    private TextView EmailEditText;
    private TextView phoneEditText;
    private ImageView profileImage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    //    String userID = fAuth.getCurrentUser().getUid();
    FirebaseFirestore fStore;
    FirebaseUser user;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        FirstEditText = (TextView) findViewById(R.id.edit_name);
        EmailEditText = (TextView) findViewById(R.id.edit_email);
        phoneEditText = (TextView) findViewById(R.id.edit_phone);
        profileImage = findViewById(R.id.profile_image);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        DocumentReference documentReference = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        String Name = snapshot.getString("fName");
                        String Email = snapshot.getString("email");
                        String Phone = snapshot.getString("phone");
                        FirstEditText.setText(Name);
                        EmailEditText.setText(Email);
                        phoneEditText.setText(Phone);
                    }
                });


        final StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");


        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
                imageURL = uri.toString();
                Log.d(TAG, "onSuccess: " + imageURL);
            }
        });
    }
}

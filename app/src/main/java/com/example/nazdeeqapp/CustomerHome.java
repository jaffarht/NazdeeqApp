package com.example.nazdeeqapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CustomerHome extends AppCompatActivity {


    CardView homeCardView;
    CardView ongoingCardView;
    CardView historyCardView;
    CardView profileCardView;
    CardView contactCardView;
    CardView logoutCardView;

    ImageView backBtn;
    ImageView profilePic;
    TextView NameText;
    String userID;

    //Firestore
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth =FirebaseAuth.getInstance();
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        picAndName();
        cardViewClicks();

        backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this,MainActivity.class));
            }
        });




    }

    private void picAndName()
    {
        if(fAuth.getCurrentUser() != null)
        {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {

                    String name = snapshot.getString("fName");
                    NameText.setText(name);

                }
            });
            storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference profileRef = storageReference.child("users/"+userID+"/profile.jpg");

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profilePic);
                }
            });
        }


    }

    private void cardViewClicks()
    {

        homeCardView = findViewById(R.id.homeCardView);
        ongoingCardView = findViewById(R.id.ogrCardView);
        historyCardView = findViewById(R.id.historyCardView);
        profileCardView = findViewById(R.id.profileCardView);
        contactCardView = findViewById(R.id.contactCardView);
        logoutCardView = findViewById(R.id.logoutCardView);
        profilePic = findViewById(R.id.imageView);
        NameText = findViewById(R.id.textView);

        homeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this,CustomerPost.class));
            }
        });


        ongoingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this,CustomerOngoing.class));
            }
        });


        historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this,CustomerHistoryPost.class));
            }
        });

        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this,CustomerProfile.class));
            }
        });

        contactCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHome.this,CustomerContactUs.class));
            }
        });

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(CustomerHome.this,SignInActivity.class));
            }
        });


    }
}

package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.android.volley.VolleyLog.TAG;

public class DriverProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView FirstEditText;
    private TextView EmailEditText;
    private TextView phoneEditText;
    private TextView CarModelText;
    private TextView CarNameText;
    private ImageView profileImage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    //    String userID = fAuth.getCurrentUser().getUid();
    FirebaseFirestore fStore;
    FirebaseUser user;
    String imageURL;

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // loadNote();
        FirstEditText = (TextView) findViewById(R.id.edit_name);
        EmailEditText = (TextView) findViewById(R.id.edit_email);
        phoneEditText = (TextView) findViewById(R.id.edit_phone);
        CarModelText = (TextView) findViewById(R.id.carmodel);
        CarNameText = (TextView) findViewById(R.id.carname);
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
                        String CarName = snapshot.getString("carModel");
                        String CarModel = snapshot.getString("carNumber");
                        FirstEditText.setText(Name);
                        EmailEditText.setText(Email);
                        phoneEditText.setText(Phone);
                        CarNameText.setText(CarName);
                        CarModelText.setText(CarModel);
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



        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);

            }
        });




        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerdrawer,
                    new DrawerHomeFragment()).commit();

        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_home:
               /* getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerdrawer,
                        new DrawerHomeFragment()).commit();
*/
               /* LayoutInflater inflater = getLayoutInflater();
                RelativeLayout container = (RelativeLayout) findViewById(R.id.show);
                inflater.inflate(R.layout.activity_driver_create_post, container);
*/
                startActivity(new Intent(DriverProfile.this,DriverCreatePostActivity.class));
                break;
            case R.id.drawer_onging:
                startActivity(new Intent(DriverProfile.this,DriverCreatedPost.class));
                break;
            case R.id.drawer_history:
                startActivity(new Intent(DriverProfile.this,DriverHistoryPost.class));
                break;
            case R.id.drawer_profile:

                break;
            case R.id.drawer_contact:
                startActivity(new Intent(DriverProfile.this, DriverContactUsActivity.class));
                break;
            case R.id.drawer_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Nazdeeq App.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
            case R.id.drawer_about:
                startActivity(new Intent(DriverProfile.this, DriverAboutUsActivity.class));
                break;
            case R.id.drawer_logout:
                fAuth.signOut();
                startActivity(new Intent(DriverProfile.this,SignInActivity.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}


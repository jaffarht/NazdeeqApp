package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nazdeeqapp.Adapter.DriverHistoryAdapter;
import com.example.nazdeeqapp.Adapter.DriverPostAdapter;
import com.example.nazdeeqapp.Model.PostsModel;
import com.example.nazdeeqapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DriverHistoryPost extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private static final String TAG = "CustomerPost";
    CustomerPost customerPost;
    private DriverHistoryAdapter adapter;
    private FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_history_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.driverhistory_list);
        String getUser = firebaseAuth.getInstance().getCurrentUser().getUid();
        final Query query = firebaseFirestore.collection( "DriverHistory");


      CollectionReference collectionReference = firebaseFirestore.collection("DriverHistory");
        final Query userQuery = collectionReference.whereEqualTo("userID",getUser);
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "WORKING: " + userQuery);
            }
        });



        FirestoreRecyclerOptions<PostsModel> options = new FirestoreRecyclerOptions.Builder<PostsModel>()
                .setQuery(userQuery,PostsModel.class)
                //.setQuery(userQuery,PostsModel.class)
                .build();


        adapter = new DriverHistoryAdapter(options);

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);


        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Drawer Picture and Name
        final TextView userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.namE);
        final ImageView image = navigationView.getHeaderView(0).findViewById(R.id.displayPic);
        image.setImageResource(R.drawable.carblue);
        if(getUser != null)
        {
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(getUser);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {

                    String name = snapshot.getString("fName");
                    userName.setText(name);

                }
            });
            storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference profileRef = storageReference.child("users/"+getUser+"/profile.jpg");

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(image);
                }
            });
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
                startActivity(new Intent(DriverHistoryPost.this,DriverCreatePostActivity.class));
                break;
            case R.id.drawer_onging:
                startActivity(new Intent(DriverHistoryPost.this,DriverCreatedPost.class));
                break;
            case R.id.drawer_history:
                break;
            case R.id.drawer_profile:
                startActivity(new Intent(DriverHistoryPost.this,DriverProfile.class));
                break;
            case R.id.drawer_contact:
                startActivity(new Intent(DriverHistoryPost.this, DriverContactUsActivity.class));
                break;
            case R.id.drawer_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Nazdeeq App.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
            case R.id.drawer_about:
                startActivity(new Intent(DriverHistoryPost.this, DriverAboutUsActivity.class));
                break;
            case R.id.drawer_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(DriverHistoryPost.this,SignInActivity.class));
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

    @Override
    protected void onStop() {

        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {

        super.onStart();
        adapter.startListening();
    }
}

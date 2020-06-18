package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.nazdeeqapp.Adapter.FirestoreAdapter;
import com.example.nazdeeqapp.Model.PostsModel;
import com.example.nazdeeqapp.Model.User;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CustomerPost extends AppCompatActivity implements FirestoreAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestoreAdapter adapter;
    ImageView backBtn;
    private static final String TAG = "CustomerPost";
    CustomerPost customerPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
         mFirestoreList = findViewById(R.id.firestore_list);

        //Query
        Query query = firebaseFirestore.collection( "Posts");
        //Pagination
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        //Recycler Options
        FirestorePagingOptions<PostsModel> posts = new FirestorePagingOptions.Builder<PostsModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, new SnapshotParser<PostsModel>() {
                    @NonNull
                    @Override
                    public PostsModel parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        PostsModel productsModel = snapshot.toObject(PostsModel.class);
                        String postId = snapshot.getId();
                        productsModel.setPostID(postId );
                        return productsModel;
                    }
                })
                .build();

         adapter = new FirestoreAdapter(posts, this);

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
        //View Holder

        backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerPost.this,CustomerHome.class));
            }
        });


    }




    @Override
    public void OnItemClick(DocumentSnapshot snapshot,int position) {
        PostsModel postsModel = snapshot.toObject(PostsModel.class);
        String id = snapshot.getId();
        //long seats = postsModel.getSeatsAvailable();
        Log.d(TAG,"  Post Model: " + postsModel.getUserID() + " id " + id);
        //Place Intent to go to the new activity here
        Intent intent = new Intent(CustomerPost.this, CustomerPostBooking.class);
        intent.putExtra("Date",postsModel.getDate());
        intent.putExtra("sourceName",postsModel.getSourceName());
        intent.putExtra("destinationName",postsModel.getDestinationName());
        intent.putExtra("sourceAddress",postsModel.getSourceAddress());
        intent.putExtra("destinationAddress",postsModel.getDestinationAddress());
        intent.putExtra("SeatsAvailable", postsModel.getSeatsAvailable());
        intent.putExtra("Time",postsModel.getTime());
        intent.putExtra("UserID",postsModel.getUserID());
        intent.putExtra("PostID",id);
        intent.putExtra("Price",postsModel.getPrice());

        startActivity(intent);
    }
}

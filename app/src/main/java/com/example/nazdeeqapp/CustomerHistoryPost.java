package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.nazdeeqapp.Adapter.CustomerHistoryAdapter;
import com.example.nazdeeqapp.Adapter.DriverHistoryAdapter;
import com.example.nazdeeqapp.Model.CustomerModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class CustomerHistoryPost extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private static final String TAG = "CustomerPost";
    CustomerPost customerPost;
    private CustomerHistoryAdapter adapter;
    private FirebaseAuth firebaseAuth;
    ImageView backBtn;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history_post);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.customerhistory_list);

        final Query query = firebaseFirestore.collection( "CustomerHistory");

        CollectionReference collectionReference = firebaseFirestore.collection("CustomerHistory");
       final Query userQuery = collectionReference.whereArrayContains("customer", firebaseAuth.getInstance().getCurrentUser().getUid());


        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "WORKING: " + userQuery);
            }
        });


        FirestoreRecyclerOptions<CustomerModel> options = new FirestoreRecyclerOptions.Builder<CustomerModel>()
                .setQuery(userQuery,CustomerModel.class)
                //.setQuery(userQuery,PostsModel.class)
                .build();

        adapter = new CustomerHistoryAdapter(options);

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

        backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHistoryPost.this,CustomerHome.class));
            }
        });

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

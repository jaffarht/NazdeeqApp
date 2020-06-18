package com.example.nazdeeqapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.nazdeeqapp.Adapter.CustomerHistoryAdapter;
import com.example.nazdeeqapp.Adapter.CustomerOngoingAdapter;
import com.example.nazdeeqapp.Model.CustomerModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CustomerOngoing extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private static final String TAG = "CustomerOngoing";
    private CustomerOngoingAdapter adapter;
    private FirebaseAuth firebaseAuth;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ongoing);


        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.customerongoing_list);

        CollectionReference collectionReference = firebaseFirestore.collection("CustomerHistory");
        final Query userQuery = collectionReference.whereArrayContains("customer", firebaseAuth.getInstance().getCurrentUser().getUid());


        FirestoreRecyclerOptions<CustomerModel> options = new FirestoreRecyclerOptions.Builder<CustomerModel>()
                .setQuery(userQuery,CustomerModel.class)
                //.setQuery(userQuery,PostsModel.class)
                .build();

        adapter = new CustomerOngoingAdapter(options);

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView mFirestoreList, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder mFirestoreList, int direction) {
                adapter.deleteItem(mFirestoreList.getAdapterPosition());
                adapter.increaseSeat();
            }
        }).attachToRecyclerView(mFirestoreList);

        backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerOngoing.this,CustomerHome.class));
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

package com.example.nazdeeqapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nazdeeqapp.Adapter.CustomerHistoryAdapter;
import com.example.nazdeeqapp.Model.CustomerModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.android.volley.VolleyLog.TAG;

public class CustomerPostBooking extends AppCompatActivity {
    private static final String TAG = "CustomerPostBooking";
    String driverName;
    String carModel;
    String carNumber;
    String date;
    String sourceName;
    String destinationName;
    String sourceAddress;
    String destinationAddress;
    long SeatsAvailable;
    String Time;
    String UserID;
    String PostID;
    String Customer;
    long Price;
    //int Count = 0;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth =FirebaseAuth.getInstance();
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    ImageView profile_image;
    Button bookrideBtn;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_post_booking);


        storageReference = FirebaseStorage.getInstance().getReference();
        t1=(TextView)findViewById(R.id.dpDate);
        t2=(TextView)findViewById(R.id.dpsource);
        t3=(TextView)findViewById(R.id.dpdestination);
        t4=(TextView)findViewById(R.id.price);
        t6=(TextView)findViewById(R.id.dpSeats);
        t7=(TextView)findViewById(R.id.dpTime);
        profile_image=(ImageView)findViewById(R.id.profile_image);
        t5=(TextView)findViewById(R.id.carname);
        t9=(TextView)findViewById(R.id.drivername);
        //t10=(TextView)findViewById(R.id.text10_activity2);
        bookrideBtn = findViewById(R.id.bookRide);



        if (getIntent().hasExtra("Date") && getIntent().hasExtra("sourceName")
                && getIntent().hasExtra("destinationName")&& getIntent().hasExtra("sourceAddress")
                && getIntent().hasExtra("destinationAddress")&& getIntent().hasExtra("SeatsAvailable")
                && getIntent().hasExtra("Time")&& getIntent().hasExtra("UserID")
                && getIntent().hasExtra("PostID"))

        {
            date = getIntent().getStringExtra("Date");
            sourceName = getIntent().getStringExtra("sourceName");
            destinationName = getIntent().getStringExtra("destinationName");
            sourceAddress = getIntent().getStringExtra("sourceAddress");
            destinationAddress = getIntent().getStringExtra("destinationAddress");
            SeatsAvailable = getIntent().getLongExtra("SeatsAvailable",0);
            Price = getIntent().getLongExtra("Price",0);
            Time = getIntent().getStringExtra("Time");
            UserID = getIntent().getStringExtra("UserID");
            PostID = getIntent().getStringExtra("PostID");
            Log.d(TAG, "Date: " + date + " sourceName" + sourceName + "destinationName: " + destinationName
                    + "sourceAddress: " + sourceAddress + "destinationAddress: " + destinationAddress + "SeatsAvailable: " + SeatsAvailable
                    + "Time: " + Time + "UserID: " + UserID + "PostID: " + PostID);

            t1.setText(date);
            t2.setText(sourceName);
            t3.setText(destinationName);
            t4.setText(Price+"");
            //t5.setText(destinationAddress);
            t6.setText(SeatsAvailable + "");
            t7.setText(Time);
           // t8.setText(UserID);
           // t9.setText(PostID);

            //Count = (int) SeatsAvailable;

        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(UserID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                 driverName = snapshot.getString("fName");
                 carModel = snapshot.getString("carModel");
                 carNumber = snapshot.getString("carNumber");
                String Picture = snapshot.getString("imageURL");
                String car = carModel+" ( " + carNumber+ " )";
                t5.setText(car);
                t9.setText("  "+driverName+"  ");
                Picasso.get().load(Picture).into(profile_image);
                Log.d(TAG, "onSuccess: " + Picture);

            }
        });

        final StorageReference profileRef = storageReference.child("users/"+UserID+"/profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_image);
            }
        });


        if(SeatsAvailable == 0)
        {
            Log.d(TAG, "disable button: ");
            bookrideBtn.setEnabled(false);
            bookrideBtn.setText("Booked");

        }

        bookrideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookrideBtn.setEnabled(false);
                bookrideBtn.setText("Booked");
                seatbook();
            }
        });


    }



    private void seatbook()
    {

        SeatsAvailable--;
        updateDescription();
        Log.d(TAG, "seatbook: " + SeatsAvailable);
        if(SeatsAvailable == 0)
        {
            Log.d(TAG, "disable button: ");
            bookrideBtn.setEnabled(false);
            bookrideBtn.setText("All Booked");
        }


    }

    public void updateDescription() {

        DocumentReference documentReference = firebaseFirestore.collection("CustomerHistory").document(PostID);
        DocumentReference documentRef = firebaseFirestore.collection("Posts").document(PostID);
        documentRef.update("seatsAvailable",SeatsAvailable);
        //documentReference.update("customer1", Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        //CustomerModel customerModel = new CustomerModel(date,sourceName,destinationName,Time,PostID,Price,UserID,carModel,carNumber,customer1);
        Customer = fAuth.getCurrentUser().getUid();
        Log.d(TAG, "updateDescription: "+Customer);
        Map<String, Object> data = new HashMap<>();
        data.put("Date", date);
        data.put("sourceName", sourceName);
        data.put("destinationName", destinationName);
        data.put("Time", Time);
        data.put("PostID", PostID);
        data.put("price", Price);
        data.put("driverID", UserID);
        data.put("driverCarModel", carModel);
        data.put("driverCarNumber", carNumber);
        data.put("customer", FieldValue.arrayUnion(Customer));
        data.put("driverName", driverName);
        firebaseFirestore.collection("CustomerHistory").document(PostID)
                .set(data, SetOptions.merge());


    }

    @Override
    public void onBackPressed() {

        // Open your list activity with FLAG_ACTIVITY_CLEAR_TOP flag
        Intent mIntent = new Intent(CustomerPostBooking.this, CustomerPost.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
        super.onBackPressed();
    }


}

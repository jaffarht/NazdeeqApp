package com.example.nazdeeqapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nazdeeqapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverFragment extends Fragment {


    private static final String TAG = "SignUpActivity";
    private EditText CarModel,CarNumber;
    private Button mSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        CarModel = (EditText) view.findViewById(R.id.car_model);
        CarNumber = (EditText) view.findViewById(R.id.car_num);
        mSave = view.findViewById(R.id.save_info);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cm = CarModel.getText().toString();
                final String cn = CarNumber.getText().toString();
                DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
                Map<String,Object> user = new HashMap<>();
                documentReference.update("carModel",cm);
                documentReference.update("carNumber",cn);
                documentReference.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Driver Uploaded " + userID);
                    }
                });
            }
        });


        return view;
    }
}

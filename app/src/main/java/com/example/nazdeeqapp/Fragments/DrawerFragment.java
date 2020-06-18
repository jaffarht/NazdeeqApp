package com.example.nazdeeqapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nazdeeqapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DrawerFragment extends Fragment {

    ImageView profilepic;
    TextView name;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_header, container, false);

        profilepic = view.findViewById(R.id.displayPic);
        name = view.findViewById(R.id.namE);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        String Name = snapshot.getString("fName");
                        String profilePic = snapshot.getString("imageURL");
                        name.setText(Name);
                        Picasso.get().load(profilePic).into(profilepic);
                    }
                });

        return view;
    }
}

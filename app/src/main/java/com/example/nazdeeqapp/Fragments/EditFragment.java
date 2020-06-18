package com.example.nazdeeqapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nazdeeqapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class EditFragment extends Fragment {



    private EditText FirstEditText;
    private EditText EmailEditText, PasswordEditText;
    private EditText phoneEditText;
    private FloatingActionButton mUpdateButton;
    //private ProgressBar progressBar;
    private ImageView profileImage;
    StorageReference storageReference;
    FirebaseAuth fAuth;
//    String userID = fAuth.getCurrentUser().getUid();
    FirebaseFirestore fStore;
    FirebaseUser user;
    String imageURL;
    String userID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        // loadNote();
        FirstEditText = (EditText) view.findViewById(R.id.edit_name);
        EmailEditText = (EditText) view.findViewById(R.id.edit_email);
       // PasswordEditText = (EditText) view.findViewById(R.id.edit_password);
        phoneEditText = (EditText) view.findViewById(R.id.edit_phone);
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        profileImage = view.findViewById(R.id.profile_image);
        //fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userID = fAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "onCreateView: " +  userID);
        DocumentReference documentReference = fStore.collection("Users").document(userID);

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

        final StorageReference profileRef = storageReference.child("users/" + userID + "/profile.jpg");


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
                startActivityForResult(openGalleryIntent, 1000);

            }
        });

        mUpdateButton = view.findViewById(R.id.updatebtn);

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = EmailEditText.getText().toString().trim();
                String password = PasswordEditText.getText().toString().trim();
                final String name = FirstEditText.getText().toString();
                final String phone = phoneEditText.getText().toString();

            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(final Uri imageUri)
    {
        final StorageReference fileRef = storageReference.child("users/"+userID+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });


        DocumentReference documentReference = fStore.collection("Users").document(userID);
        if(imageURL != null)
        {
            documentReference.update("imageURL",imageURL);
        }

    }
}

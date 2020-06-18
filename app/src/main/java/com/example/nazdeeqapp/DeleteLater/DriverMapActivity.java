package com.example.nazdeeqapp.DeleteLater;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nazdeeqapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private FirebaseFirestore mDb;
    private static final String TAG = "DriverMapActivity";
    private static final float DEFAULT_ZOOM = 15f;
    //widgets
    private AutoCompleteTextView mSearchText;
    private AutoCompleteTextView mDestinationText;
    private ImageView mGps;

    String seatsAvailable;
    String time;
    String date;
    LatLng sourcelatlng;
    LatLng destinationlatlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mDestinationText = (AutoCompleteTextView) findViewById(R.id.input_search2);
        //seats   = (EditText) findViewById(R.id.seats);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        mDb = FirebaseFirestore.getInstance();



        findViewById(R.id.create_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });












    }





    //ADD LATER
    private void init()
    {
        Log.d(TAG, "init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    //execute our method for searching
                    geoLocate();

                }
                return false;
            }
        });



        //ADD LATER
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
               // Location();
            }
        });

    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();
        String destinationString = mDestinationText.getText().toString();

        Geocoder geocoder = new Geocoder(DriverMapActivity.this);
        List<Address> list = new ArrayList<>();
        List<Address> list2 = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
            list2 = geocoder.getFromLocationName(destinationString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            double selectedLat = address.getLatitude();
            double selectedLng = address.getLongitude();
            sourcelatlng = new LatLng(selectedLat, selectedLng);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "CHECKING ROAD sourcelatlng" + sourcelatlng.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }

        if(list2.size() > 0){
            Address address = list2.get(0);
            double selectedLat = address.getLatitude();
            double selectedLng = address.getLongitude();
            destinationlatlng = new LatLng(selectedLat, selectedLng);

            Log.d(TAG, "geoLocate: found a location destinationlatlng: " + destinationlatlng.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,destinationlatlng.toString(),Toast.LENGTH_LONG).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));

            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(sourcelatlng,destinationlatlng)
                    .width(5)
                    .color(Color.RED));
            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(selectedLat);
            temp.setLongitude(selectedLng);
            float distance = temp.distanceTo(temp);

            Log.d(TAG, "TESTING HONDI PAI " + distance);
            Log.d(TAG, "KUTEYA" + temp);



        }

    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);

        }

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
    }

    private void createPost()
    {
        DocumentReference documentReference = mDb.collection("Posts").document();
        Map<String,Object> post = new HashMap<>();
       // getSeats();
        String userId= FirebaseAuth.getInstance().getUid();

        GeoPoint geoPointSource = new GeoPoint(sourcelatlng.latitude, sourcelatlng.longitude);
        GeoPoint geoPointDestination = new GeoPoint(destinationlatlng.latitude, destinationlatlng.longitude);
        post.put("Source",geoPointSource);
        post.put("Destination",geoPointDestination);
        post.put("Date",date);
        post.put("Time",time);
        post.put("SeatsAvailable",seatsAvailable);
        post.put("UserID",userId);
        documentReference.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: user Profile is created for ");
            }
        });

    }


}

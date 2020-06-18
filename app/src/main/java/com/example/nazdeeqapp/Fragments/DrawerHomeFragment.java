package com.example.nazdeeqapp.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.nazdeeqapp.DriverCreatePostActivity;
import com.example.nazdeeqapp.Model.PostsModel;
import com.example.nazdeeqapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DrawerHomeFragment extends Fragment implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, RoutingListener {

    private GoogleMap mMap;
    private static final String TAG = "DriverCreatePostActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;

    //Map
    AutocompleteSupportFragment mSource;
    AutocompleteSupportFragment mDestination;
    LatLng sourceLatLng;
    LatLng destinationLatLng;
    String sourceName;
    String destinationName;
    String sourceAddress;
    String destinationAddress;
    int numberofSeats;
    int price;
    EditText seats;
    EditText priceText;
    Marker marker;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.gradient2};

    //Time and Date
    String time;
    String date;

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Posts").document();
    private DocumentReference driverHistory = db.collection("DriverHistory").document();
    private String getUser = FirebaseAuth.getInstance().getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_home_fragment, container, false);

        polylines = new ArrayList<>();
        // NUMBER OF SEATS AVAILABLE
        seats   = (EditText) view.findViewById(R.id.seatsBox);
        priceText = (EditText) view.findViewById(R.id.priceBox);

        //DATE AND TIME
        view.findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogue();
            }
        });

        view.findViewById(R.id.timeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialogue();
            }
        });

        view.findViewById(R.id.createpostButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
        //MAP ACTIVITY
        getLocationPermission();
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mSource = (AutocompleteSupportFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (mSource != null) {
            mSource.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

            mSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "getName: " + place.getName());
                    Log.i(TAG, "getAddress: " + place.getAddress());
                    Log.i(TAG, "getId: " + place.getId());
                    Log.i(TAG, "getLatLng: " + place.getLatLng());
                    LatLng queriedLocation = place.getLatLng();
                    Log.d(TAG, "queriedLocation: " + queriedLocation);

                    sourceName = place.getName();
                    sourceLatLng = place.getLatLng();
                    sourceAddress = place.getAddress();

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sourceLatLng, 15));

                    if (mMap != null) {
                        mMap.clear();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(sourceLatLng).title(place.getName()));


                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }

        /////////////////////////////////////////////////////////////////////////////////////////////
        mDestination = (AutocompleteSupportFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_destination);
        if (mDestination != null) {
            mDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

            mDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "getName2: " + place.getName());
                    Log.i(TAG, "getAddress2: " + place.getAddress());
                    Log.i(TAG, "getId2: " + place.getId());
                    Log.i(TAG, "getLatLng2: " + place.getLatLng());
                    LatLng queriedLocation = place.getLatLng();
                    Log.d(TAG, "queriedLocation2: " + queriedLocation);

                    destinationName = place.getName();
                    destinationLatLng = place.getLatLng();
                    destinationAddress = place.getAddress();


                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));
                    if (mMap != null) {
                        mMap.clear();
                    }

                    marker = mMap.addMarker(new MarkerOptions().position(destinationLatLng).title(place.getName()));
                    getRouteToMarker(destinationLatLng);


                  /*  Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(sourceLatLng,destinationLatLng)
                            .width(5)
                            .color(Color.RED));*/
                    //POLYLINES


                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;



        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //get rid of the location button which centers your default location
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
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

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this.getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this.getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    void showDatePickerDialogue()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity().getApplicationContext(),
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
        Log.d(TAG, "showDatePickerDialogue " + datePickerDialog.toString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date =  dayOfMonth +"/"+ month+ "/"+ year;
        Log.d(TAG, "onDateSet " + date);
    }



    void showTimeDialogue(){
        final Calendar calender = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                calender.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calender.set(Calendar.MINUTE,minute);
                //time = hourOfDay + ":" + minute +" "+ AM_PM;


                time = pad(hourOfDay) + ":" + pad(minute)+ " "+ AM_PM;
                Log.d(TAG, "onTimeSet: "+ time);


            }
        };



        new TimePickerDialog(getActivity(),timeSetListener,calender.get(Calendar.HOUR_OF_DAY),calender.get(Calendar.MINUTE),false).show();
    }

    public String pad(int input)
    {

        String str = "";

        if (input > 10) {

            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);

        }
        return str;
    }

    public void getSeats()
    {
        try
        {
            numberofSeats = Integer.parseInt(seats.getText().toString());
        }
        catch (NumberFormatException ex)
        {
            Log.d(TAG, "int cannot be zero : " +ex);
            Toast.makeText(getActivity(), "Please enter Available seats > 0", Toast.LENGTH_SHORT).show();
        }
    }

    public void getPrice()
    {
        try
        {
            price = Integer.parseInt(priceText.getText().toString());
        }
        catch (NumberFormatException ex)
        {
            Log.d(TAG, "int cannot be zero : " +ex);
            Toast.makeText(getActivity(), "Please enter Price > 0", Toast.LENGTH_SHORT).show();
        }
    }

    public void createPost()
    {
        getSeats();
        getPrice();

        if(sourceName == null)
        {
            Toast.makeText(getActivity(), "Please enter SOURCE", Toast.LENGTH_SHORT).show();
        }

        if(destinationName == null)
        {
            Toast.makeText(getActivity(), "Please enter SOURCE", Toast.LENGTH_SHORT).show();
        }

        if(numberofSeats == 0)
        {
            Toast.makeText(getActivity(), "Please enter Available seats > 0", Toast.LENGTH_SHORT).show();
        }
        if(price == 0)
        {
            Toast.makeText(getActivity(), "Please enter Price > 0", Toast.LENGTH_SHORT).show();
        }

        if(time == null)
        {
            Toast.makeText(getActivity(), "Please enter TIME", Toast.LENGTH_SHORT).show();
        }

        if(date == null)
        {
            Toast.makeText(getActivity(), "Please enter DATE", Toast.LENGTH_SHORT).show();
        }

        PostsModel postsModel = new PostsModel(date,sourceName,destinationName,sourceAddress,destinationAddress,numberofSeats,time,getUser,price);

        noteRef.set(postsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Successfully created a Post",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

        driverHistory.set(postsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private void getRouteToMarker(LatLng destinationLatLng)
    {
        Routing routing = new Routing.Builder()
                .key(getString(R.string.google_maps_key))
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(sourceLatLng,destinationLatLng)
                .build();
        routing.execute();
    }
    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            // Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }


}

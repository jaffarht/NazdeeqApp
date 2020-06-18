package com.example.nazdeeqapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DriverContactUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_contact_us);


        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fAuth = FirebaseAuth.getInstance();


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
                startActivity(new Intent(DriverContactUsActivity.this,DriverCreatePostActivity.class));
                break;
            case R.id.drawer_onging:
                startActivity(new Intent(DriverContactUsActivity.this,DriverCreatedPost.class));
                break;
            case R.id.drawer_history:
                startActivity(new Intent(DriverContactUsActivity.this,DriverHistoryPost.class));
                break;
            case R.id.drawer_profile:
                startActivity(new Intent(DriverContactUsActivity.this, DriverProfile.class));
                break;
            case R.id.drawer_contact:
                break;
            case R.id.drawer_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Sharing Nazdeeq App.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
            case R.id.drawer_about:
                startActivity(new Intent(DriverContactUsActivity.this, DriverAboutUsActivity.class));
                break;
            case R.id.drawer_logout:
                fAuth.signOut();
                startActivity(new Intent(DriverContactUsActivity.this,SignInActivity.class));
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
}




package com.example.suyog.locationtracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReminderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PERMISSION_RESULT_CODE =22 ;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    TextView name;
    TextView email;
    NavigationView navigationView;

    public static final String LOCATION_FENCE_KEY = "LocationFenceKey";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Fence", "not getting permission");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_RESULT_CODE);
        }

        auth=FirebaseAuth.getInstance();
        DatabaseReference userReference= FirebaseDatabase.getInstance().getReference("appusers").child(auth.getCurrentUser().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Users user = dataSnapshot.getValue(Users.class);

                                                    navigationView = (NavigationView) findViewById(R.id.nav_view);
                                                    View headerLayout = navigationView.getHeaderView(0);

                                                    name = (TextView) headerLayout.findViewById(R.id.usernametxt);
                                                    email = (TextView) headerLayout.findViewById(R.id.useremailtxt);
                                                    name.setText(user.getName().toString());
                                                    email.setText(auth.getCurrentUser().getEmail());
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
        getSupportFragmentManager().beginTransaction().add(R.id.content,new AddRemainder(),"Add Reminder").commit();

        progressDialog =new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.remainder, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.setremainder) {
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.content,new AddRemainder())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.viewremainder) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content,new ReminderListFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();

        }
        else if (id == R.id.loggingout) {

            progressDialog.setMessage("Signing Out");
            auth.signOut();
            ReminderSet.cleanReminder();

            finish();
            Intent intent=new Intent(this ,MainActivity.class);
            startActivity(intent);
            //setActionBarTitle("Login");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        moveTaskToBack(true);
        Log.i("Fence","onDestroy");
        super.onDestroy();

    }
}

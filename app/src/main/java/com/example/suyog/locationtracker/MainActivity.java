package com.example.suyog.locationtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            finish();
            Intent intent=new Intent(MainActivity.this,ReminderActivity.class);
            startActivity(intent);

        }
        stopService(new Intent(this, RingtoneService.class));

        getSupportFragmentManager().beginTransaction().add(R.id.container,new Login(),"Log In").commit();


    }


}

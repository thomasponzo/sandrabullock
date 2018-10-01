package com.tijdelijk.sandrabullock;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tijdelijk.sandrabullock.model.Information;

import java.util.Arrays;
import java.util.List;

//<!--
//        * naam: <Thomas Ponzo>
//        * groep: 6c
//        * datum: 28-9-2018
//        -->

public class MainActivity extends AppCompatActivity {

    int RC_SIGN_IN;

    private FirebaseAuth firebaseAuth;
    public LinearLayout linearLayout;

    public DatabaseReference mDatabase;

    public TextView tittle;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //If the user is not yet logged in create login
       if (firebaseAuth.getInstance().getCurrentUser() == null){
           createLogin();
       } else {

           UpdateLogin();
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            } else {
                // Sign in failed.
                Toast.makeText(this, "Error Could not login", Toast.LENGTH_SHORT).show();
                createLogin();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (firebaseAuth.getInstance().getCurrentUser() != null) {
            //if the user press onback when logged in
        } else {
            //if the user press onback when not logged in
            createLogin();
        }
    }

    public void createLogin(){
        RC_SIGN_IN = 101;

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.mipmap.logologin)
                        .build(),
                RC_SIGN_IN);
    }

    public void UpdateLogin(){
        linearLayout = (LinearLayout) findViewById(R.id.mainactivity);
        linearLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        tittle = findViewById(R.id.title);
        mDatabase.child("Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataSnapshot.getValue(Information.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tittle.setText(Information.getTittle());

    }

}


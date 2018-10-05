package com.tijdelijk.sandrabullock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

//<!--
//        * naam: <Thomas Ponzo>
//        * groep: 6c
//        * datum: 28-9-2018
//        -->

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseDatabase.getInstance().getReference().keepSynced(true);


        Completable.timer(2, TimeUnit.SECONDS, Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(() -> {
                    Intent goToMainAcvity = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(goToMainAcvity);
                    finish();
                });
    }
}

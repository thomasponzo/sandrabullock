package com.tijdelijk.sandrabullock;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class account extends AppCompatActivity {

    public Toolbar toolbar;
    public ImageView toolbarBackBtn;

    TextView username;
    TextView useremail;

    private FirebaseAuth firebaseAuth;
    public DatabaseReference mDatabase;

    Button loggoutBtn;
    Button deleteaccountBtn;

    ImageView userpicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.my_toolbar);

        toolbarBackBtn = findViewById(R.id.backbutton);
        loggoutBtn = findViewById(R.id.logout);
        deleteaccountBtn = findViewById(R.id.delete);

        username = findViewById(R.id.profilename);
        useremail= findViewById(R.id.email);

        userpicture= findViewById(R.id.profilepicture);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Variable holding the original String portion of the url that will be replaced
                String originalPieceOfUrl = "s96-c/photo.jpg";

                // Variable holding the new String portion of the url that does the replacing, to improve image quality
                String newPieceOfUrlToAdd = "s400-c/photo.jpg";

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                String photoPath = photoUrl.toString();

                // Replace the original part of the Url with the new part
                String newProfilePictureResolution = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);

                username.setText(name);
                useremail.setText(email);

//                //Convert the link into a image
                Picasso.get()
                        .load(newProfilePictureResolution)
                        .into(userpicture);
            }
            ;
        }


        toolbarBackBtn.setOnClickListener(view -> {
            goBackToMainAcitivty();
        });

        loggoutBtn.setOnClickListener(view -> {
            //Sign the user out of firebase
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();

            goBackToMainAcitivty();
        });

        deleteaccountBtn.setOnClickListener(view -> {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             goBackToMainAcitivty();
                        }
                    });

        });
    }

    public void goBackToMainAcitivty(){
        Intent intent = new Intent(account.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.tijdelijk.sandrabullock;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tijdelijk.sandrabullock.ViewHolder.TableViewHolder;
import com.tijdelijk.sandrabullock.model.Information;
import com.tijdelijk.sandrabullock.model.Table;

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


    public TextView tittlename;
    public TextView discription;

    public ImageView picture;

    public ProgressBar progressBar;

    public Toolbar toolbar;
    public TextView toolbarTittle;

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer youTubePlayer;


    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceTable;

    FirebaseRecyclerOptions<Table> options;
    FirebaseRecyclerAdapter<Table, TableViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        databaseReferenceTable = FirebaseDatabase.getInstance().getReference().child("Table");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        options = new FirebaseRecyclerOptions.Builder<Table>()
                .setQuery(databaseReferenceTable, Table.class)
                .build();

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);
        if (youTubePlayerFragment == null)
            return;

        adapter = new FirebaseRecyclerAdapter<Table, TableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TableViewHolder holder, int position, @NonNull Table model) {
                //Get and set the strings into the view
                holder.movie.setText(model.getMoviename());
                holder.year.setText(model.getYear());
                holder.role.setText(model.getRole());
            }

            @NonNull
            @Override
            public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_item, parent, false);
                return new TableViewHolder(view);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        tittlename = findViewById(R.id.title);
        toolbarTittle = findViewById(R.id.tittletoolbar);
        discription = findViewById(R.id.description);
        picture = findViewById(R.id.image);

        progressBar = findViewById(R.id.progress_loader);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        youTubePlayerFragment.getView().setVisibility(View.GONE);


        //If the user is not yet logged in create login
        if (firebaseAuth.getInstance().getCurrentUser() == null) {
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
                UpdateLogin();
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

    public void createLogin() {
        RC_SIGN_IN = 101;

        progressBar.setVisibility(View.INVISIBLE);

        adapter.startListening();


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
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.AppThemeFirebaseAuth)
                        .build(),
                RC_SIGN_IN);
    }

    public void UpdateLogin() {
        toolbar.setVisibility(View.VISIBLE);
        toolbarTittle.setText("informatie");

        linearLayout = (LinearLayout) findViewById(R.id.mainactivity);
        linearLayout.setBackgroundColor(getResources().getColor(android.R.color.white));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Table");


        mDatabase.child("Information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Fetch the information from firebase
                Information user = dataSnapshot.getValue(Information.class);

                tittlename.setText(user.getTittle());
                discription.setText(user.getDescription());

                //set the views visibility accordantly
                tittlename.setVisibility(View.VISIBLE);
                discription.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                //Convert the link into a image
                Picasso.get()
                        .load(user.getImage())
                        .into(picture);


                youTubePlayerFragment.initialize(YoutubeConfig.getApiKey(), new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                        boolean wasRestored) {
                        youTubePlayerFragment.getView().setVisibility(View.VISIBLE);

                        if (!wasRestored) {
                            youTubePlayer = player;

                            //set the player style default
                            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                            //cue the 1st video by default
                            youTubePlayer.loadVideo(user.getYoutube());
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                        //print or show error if initialization failed

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.accountimage) {
            Intent intent = new Intent(MainActivity.this, account.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (adapter != null) {
//            adapter.startListening();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (adapter != null) {
//            adapter.stopListening();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}




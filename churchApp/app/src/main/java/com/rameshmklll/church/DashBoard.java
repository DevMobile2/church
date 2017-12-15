package com.rameshmklll.church;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    FragmentManager fragmentManager = getSupportFragmentManager();
    android.support.v4.app.Fragment fragment = null;
    private String TAG= "DashBoard";
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    private DashBoard activity;
    private TextView tv_user, tv_email;
    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity= this;
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

         tv_user =  headerView.findViewById(R.id.tv_user);
         tv_email  = headerView.findViewById(R.id.tv_email);
        iv_profile = headerView.findViewById(R.id.iv_profile);
       boolean is_skip = getIntent().getBooleanExtra("is_skip", false);

        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (!is_skip)
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            tv_user.setText(mUsername);
            tv_email.setText(mFirebaseUser.getEmail());
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();

                setProfilePic(mPhotoUrl);

            }
            PreferencesData.putProviderType(activity, mFirebaseUser.getProviders().get(0));

        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    private void setProfilePic(String mPhotoUrl) {

        Picasso.with(activity)
                .load(mPhotoUrl)
//                .resize(60, 50)
//                .centerCrop()
                .into(iv_profile, new Callback() {
                    @Override
                    public void onSuccess() {
                        //code
                    }
                    @Override
                    public void onError() {
                        //code
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreferencesData.getLoggedIn(this)){
                finishAffinity();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.sign_out_menu:

                String b = PreferencesData.getProviderType(activity);

                if ( b.equalsIgnoreCase("google.com")) {
                    mFirebaseAuth.signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    mUsername = ANONYMOUS;

                }else {
                    mFirebaseAuth.signOut();
                    LoginManager.getInstance().logOut();
                }
                PreferencesData.putLoggedIn(this, false);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.worship_timings) {
            // Handle the camera action
            fragment=new WorshipTimingsFrag();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "pendingReports")
                    .addToBackStack("Interviewer").commit();
        } else if (id == R.id.bible) {
            fragment=new BibleFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "pendingReports")
                    .addToBackStack("Interviewer").commit();

        } else if (id == R.id.contact) {

            Intent intent=new Intent(this,XlReaderActivity.class);
            startActivity(intent);
        } else if (id == R.id.gallery) {
            fragment=new Gallery();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "Gallery")
                    .addToBackStack("Gallery").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}

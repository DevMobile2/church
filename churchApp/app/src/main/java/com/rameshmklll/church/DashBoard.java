package com.rameshmklll.church;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;

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
    ArrayList<Integer> titles = new ArrayList<>();
    Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
         toolbar = findViewById(R.id.toolbar);
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
            Toast.makeText(this, " Logged in..", Toast.LENGTH_SHORT).show();
            mUsername = mFirebaseUser.getDisplayName();
            tv_user.setText(mUsername);
            tv_email.setText(mFirebaseUser.getEmail());
            if (mFirebaseUser.getPhotoUrl() != null) {

                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("photo_url",mPhotoUrl);
                setProfilePic(mPhotoUrl);

            }
            PreferencesData.putProviderType(activity, mFirebaseUser.getProviders().get(0));

        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


//        onNavigationItemSelected(null);
        doClick(R.id.first_page);
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


//        FragmentManager manager = getSupportFragmentManager();
//        if (manager.getBackStackEntryCount() > 0) {
//            manager.popBackStack();
//
//            int index = titles.size() - 1;
//            if (index > -1)
//                titles.remove(titles.get(index));
//
//            if (titles.size() == 0)
//                toolbar.setTitle(R.string.dashboard);
//            else
//                toolbar.setTitle(titles.get(titles.size() - 1));
//
//        } else {
//            if (doubleBackToExitPressedOnce) {
////                this.stopService(new Intent(this, ChatService.class));
////                ChatService.unsubscribeToEteki();
//                Intent in = new Intent(this, MainActivity.class);
//                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                in.putExtra("exit", true);
//                startActivity(in);
//            }
//
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
//        }

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
              performLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void performLogout() {
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        doClick(id);
        return true;
    }

    private void doClick(int id) {
        int title = R.string.dashboard;

        if (id == R.id.worship_timings) {
             title = R.string.worship_timings;

            // Handle the camera action
            fragment=new WorshipTimingsFrag();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "worship_timings")
                    .addToBackStack("worship_timings").commit();
        } else if (id == R.id.bible) {
            title = R.string.bible;
            fragment=new BibleFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "bible")
                    .addToBackStack("bible").commit();
        } else if (id == R.id.contact) {
            title = R.string.contact_us;
            fragment  = new ContactUsFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "ContactUs")
                    .addToBackStack("ContactUs").commit();
        } else if (id == R.id.gallery) {
            title = R.string.gallery;
            fragment=new Gallery();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "Gallery")
                    .addToBackStack("Gallery").commit();
        }else if (id == R.id.first_page){
            title = R.string.dashboard;
            fragment = new FirstPage();
            Bundle bundle = new Bundle();
            bundle.putString("userName", mUsername);
            fragment.setArguments( bundle);

            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "FirstPage")
                    .addToBackStack("FirstPage").commit();
        }
        else if (id == R.id.almanac) {
            fragment=new AlmanacFragment();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "Almanac")
                    .addToBackStack("Gallery").commit();
        }
        else if(id==R.id.fb){


            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
     /*   Intent intent=getOpenFacebookIntent(this);
           startActivity(intent);*/

           /* Intent intent = new Intent("android.intent.category.LAUNCHER");
            intent.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
            startActivity(intent);*/

        }
        else if(id==R.id.logout){
            performLogout();
        }
        titles.add(title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public static String FACEBOOK_URL = "https://www.facebook.com/csichristchurcheluru";
    public static String FACEBOOK_PAGE_ID = "csichristchurcheluru";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }


    public static Intent getOpenFacebookIntent(Context context) {

     return    new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/csichristchurcheluru"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }



}

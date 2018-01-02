 package com.rameshmklll.church;

 import android.content.Intent;
 import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
 import com.google.android.gms.tasks.Tasks;
 import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

 public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

     TextView skip;

     // Firebase instance variables
     private FirebaseAuth mFirebaseAuth;
     private FirebaseUser mFirebaseUser;
     public static final String ANONYMOUS = "anonymous";
     private String mUsername;
     private String mPhotoUrl;
     private GoogleApiClient mGoogleApiClient;
     private Button btGoogle;

     private static final String TAG = "MainActivity";
     private static final int RC_SIGN_IN = 9001;
     private CallbackManager mCallbackManager;
     private Button btFacebook;
     LoginButton loginButton;
     private MainActivity activity;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         FacebookSdk.sdkInitialize(getApplicationContext());
         setContentView(R.layout.activity_main);
         Log.d(TAG, "onCreate");

         android.support.v7.app.ActionBar actionBar = getSupportActionBar();
         actionBar.hide();
//         actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED)); // set your desired color

         activity = this;
         skip=findViewById(R.id.tvSkip);
         skip.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent=new Intent(MainActivity.this,DashBoard.class);
            intent.putExtra("is_skip", true);
            startActivity(intent);
          }
         });

         btGoogle = findViewById(R.id.btGoogle);
         btFacebook =  findViewById(R.id.btFb);
         btGoogle.setOnClickListener(this);
         btFacebook.setOnClickListener(this);

         // Configure Google Sign In
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                 .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                 .build();

         // Initialize FirebaseAuth
         mFirebaseAuth = FirebaseAuth.getInstance();

         // Initialize Facebook Login button
         loginButton = findViewById(R.id.login_button);
         mCallbackManager = CallbackManager.Factory.create();

     }

     @Override
     public void onStart() {
         super.onStart();
         Log.d(TAG, "onStart");
         String provider_type;
         // Check if user is signed in (non-null) and update UI accordingly.
          mFirebaseUser = mFirebaseAuth.getCurrentUser();
         if (mFirebaseUser!=null) {
             provider_type = mFirebaseUser.getProviders().get(0);

           if (PreferencesData.getLoggedIn(activity))
                updateUI(mFirebaseUser);
         }
     }

     private void updateUI(FirebaseUser currentUser) {
         Log.d(TAG, "updateUI");
         startActivity(new Intent(this, DashBoard.class));
     }

     @Override
     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
         // An unresolvable error has occurred and Google APIs (including Sign-In) will not
         // be available.
         Log.d(TAG, "onConnectionFailed:" + connectionResult);
         Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
     }


     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.btGoogle:
                 signInGoogle();
                 break;

             case R.id.btFb:
                 signInFacebook();
                 break;
         }
     }

     private void signInFacebook() {
         Log.d(TAG, "signInFacebook:" );
//         loginButton.setFragment();
         loginButton.setReadPermissions("email", "public_profile");
         loginButton.performClick();
         loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
             @Override
             public void onSuccess(LoginResult loginResult) {
                 Log.d(TAG, "facebook:onSuccess:" + loginResult);
                 handleFacebookAccessToken(loginResult.getAccessToken());
             }

             @Override
             public void onCancel() {
                 Log.d(TAG, "facebook:onCancel");
                 // ...
             }

             @Override
             public void onError(FacebookException error) {
                 Log.d(TAG, "facebook:onError", error);
                 // ...
             }
         });
     }

     private void signInGoogle() {
         Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
         startActivityForResult(signInIntent, RC_SIGN_IN);
     }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         Log.d(TAG, "onActivityResult:" );

         // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
         if (requestCode == RC_SIGN_IN) {
             GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
             if (result.isSuccess()) {
                 // Google Sign-In was successful, authenticate with Firebase
                 GoogleSignInAccount account = result.getSignInAccount();
                 firebaseAuthWithGoogle(account);
             } else {
                 // Google Sign-In failed
                 Log.e(TAG, "Google Sign-In failed.");
             }
         }else {
             //         // Pass the activity result back to the Facebook SDK
                 mCallbackManager.onActivityResult(requestCode, resultCode, data);
         }

//         mCallbackManager.onActivityResult(requestCode, resultCode, data);

     }



     private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
         Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
         final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);



        if (mFirebaseUser!=null){
            mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "linkWithCredential:success");
                                FirebaseUser user = task.getResult().getUser();
                                PreferencesData.putLoggedIn(activity, true);
                                updateUI(user);
                                finish();
                            } else {
                                Log.w(TAG, "linkWithCredential:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                FirebaseUser prevUser = mFirebaseUser;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            mFirebaseUser = Tasks.await(mFirebaseAuth.signInWithCredential(credential)).getUser();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }else {
            mFirebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCredential ", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                              startActivity(new Intent(MainActivity.this, DashBoard.class));

                            } else {
                                PreferencesData.putLoggedIn(activity, true);
                                startActivity(new Intent(MainActivity.this, DashBoard.class));
                                finish();
                            }
                        }
                    });
        }

//
//         FirebaseUser prevUser = mFirebaseUser;
//         mFirebaseUser = mFirebaseAuth.signInWithCredential(credential).await().getUser();

     }


     private void handleFacebookAccessToken(AccessToken token) {
         Log.d(TAG, "handleFacebookAccessToken:" + token);

         AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

if (mFirebaseUser!=null)
         mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {


                         if (task.isSuccessful()) {
//                             PreferencesData.putProviderType( activity, mFirebaseUser.getProviders().get(0));

                             // Sign in success, update UI with the signed-in user's information
                             Log.d(TAG, "signInWithCredential:success");
                             FirebaseUser user = mFirebaseAuth.getCurrentUser();
                             PreferencesData.putLoggedIn(activity, true);
                             updateUI(user);

                         } else {
                             // If sign in fails, display a message to the user.
                             Log.w(TAG, "signInWithCredential:failure", task.getException());
                             Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                             updateUI(null);
                         }
                     }
                 });
else


         mFirebaseAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
//                             PreferencesData.putProviderType( activity, mFirebaseUser.getProviders().get(0));

                             // Sign in success, update UI with the signed-in user's information
                             Log.d(TAG, "signInWithCredential:success");
                             FirebaseUser user = mFirebaseAuth.getCurrentUser();
                             PreferencesData.putLoggedIn(activity, true);
                             updateUI(user);

                         } else {
                             // If sign in fails, display a message to the user.
                             Log.w(TAG, "signInWithCredential:failure", task.getException());
                             Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                             updateUI(null);
                         }
                     }
                 });
     }

 }

package edu.oakland.textblock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Hashtable;
import java.util.Map;

import static edu.oakland.textblock.R.id.forget_password_button;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    public final static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x1;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    private SignInButton googleSignInButton;
    private Button signOutButton;
    private TextView statusTextView;
    private GoogleApiClient mGoogleApiClient;
    private EditText emailEdit;
    private EditText passEdit;
    private Button forgetButton, signUpButton, emailSignInButton;
    private FirebaseAuth mAuth;
    //设置一个响应用户的登录状态变化的 AuthStateListener：
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String permission = "ACCESS_FINE_LOCATION";
    private Integer GPS_SETTINGS = 0x7;

    // for "sign up" on our server
//    private String email;
//    private String pwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        askForPermission();
        FirebaseApp.initializeApp(this);

        // [START config_signin]
        // Configure email sign in
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        // specify sign in scope
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        statusTextView = (TextView) findViewById(R.id.status_textview);
        emailEdit = (EditText) findViewById(R.id.editEmail);
        passEdit = (EditText) findViewById(R.id.editPass);
        forgetButton = (Button) findViewById(R.id.forget_password_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        emailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        // create a Google api client
        // when the user click a sign-in button, here we create a sign-in intent and the activity for it.
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        //              .build();


        //googleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        // googleSignInButton.setOnClickListener(this);


        signOutButton.setOnClickListener(this);

        forgetButton.setOnClickListener(this);

        signUpButton.setOnClickListener(this);

        emailSignInButton.setOnClickListener(this);

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);


                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
        startService(new Intent(this, GpsServices.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    System.exit(5);
                }


            }
        }

    }


    // 简单工厂模式
    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
//
//            case R.id.google_sign_in_button:
//                googleSignIn();
//                break;
//
            case R.id.sign_out_button:
                signOut();
                break;
            case forget_password_button:
                forgetPass(emailEdit.getText().toString());
                break;
            case R.id.sign_up_button: {
                String email = emailEdit.getText().toString();
                String pwd = passEdit.getText().toString();
                signUp(email, pwd);
                // to sent the form data to our server
                signUpOnOurServer(email, pwd);
            }
            break;
            case R.id.email_sign_in_button:


                //startService(new Intent(this, GpsServices.class));
                emailSignIn(emailEdit.getText().toString(), passEdit.getText().toString());
                break;

        }

    }

    private void signUpOnOurServer(final String email, final String pwd) {

        final String URL_SIGNUP = "http://52.41.167.226/SignUp.php";
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        // instantiate a StringRequest to get photos' links
        StringRequest getPhotosRequest;
        getPhotosRequest = new StringRequest(Request.Method.POST, URL_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyApp Res", response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyApp ResErr", error.toString());

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                // add IMEI into the request
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String IMEI = telephonyManager.getDeviceId();
                params.put("IMEI", IMEI);
                params.put("email", email);
                params.put("pwd", pwd);
                Log.d("MyApp IMEI", "");
                return params;
            }
        };
        try {
            Log.d("MyApp", getPhotosRequest.getBody().toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        requestQueue.add(getPhotosRequest);


    }


    // [START google signin]
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        // and start an activity for it
    }
    // [END google signin]

    // [START onactivityresult]
    // get the user's data in the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // get signIn object from the data that come back from the intent
            handleSignInResult(result);
        }
    }
    // [END onactivityresult]

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            // sign in successfully, show authenticated UI
            GoogleSignInAccount acct = result.getSignInAccount();
            statusTextView.setText("Hello Google Account User: " + acct.getDisplayName());
            firebaseAuthWithGoogle(acct);
            Intent intent = new Intent(MainActivity.this, GuardianWelcome.class);
            startActivity(intent);
        } else {
            //updateUI(null);
            statusTextView.setText("Google accounts sign in failed, please check the network access.");
        }
    }

    // [START auth_with_google]
    // 登录Google帐户成功后还要用signInWithCredential登录Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_google]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }


    public void signOut() {
        // firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("Signed out.");
                updateUI(null);
            }
        });
    }

    private boolean validateForm(int type) {
        boolean valid = true;

        String email = emailEdit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Required.");
            valid = false;
        } else {
            emailEdit.setError(null);
        }

        if (type == 1) { // sign up or sign in need to validate password
            String password = passEdit.getText().toString();
            if (TextUtils.isEmpty(password)) {
                passEdit.setError("Required.");
                valid = false;
            } else {
                passEdit.setError(null);
            }
        }

        return valid;
    }

    private void forgetPass(String emailAddress) {
        // forget password
        //通过sendPasswordResetEmail 方法向用户发送一封重设密码电子邮件。
        if (!validateForm(0)) { // only validate email address
            return;
        }
        mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(MainActivity.this, "Email sent successful",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Email has not been registered.",
                                    Toast.LENGTH_SHORT).show();
                            statusTextView.setText("Email has not been registered.");
                        }
                    }
                });
    }

    private void signUp(String email, String password) {
        // sign up
        if (!validateForm(1)) { // validate email address and password
            return;
        }
        //通过  mAuth.createUserWithEmailAndPassword(email, password);方法来进行用户注册
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign up fails, display a message to the user. If sign up succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Sign up failed, please check the network access.",
                                    Toast.LENGTH_SHORT).show();
                            // update status textview
                            statusTextView.setText("Sign up failed, please check the network access.");
                        } else {
                            Intent intent = new Intent(MainActivity.this, GuardianWelcome.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void emailSignIn(String email, String password) {
        // sign in with email
        if (!validateForm(1)) { // validate email address and password
            return;
        }
        //通过 signInWithEmailAndPassword(email, password);方法来进行身份认证
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Sign in failed",
                                    Toast.LENGTH_SHORT).show();
                            // update status textview
                            statusTextView.setText("Sign in failed");
                        } else {

                            Intent intent = new Intent(MainActivity.this, GuardianWelcome.class);
                            startActivity(intent);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            statusTextView.setText("Hello Email User: " + user.getEmail());

            findViewById(R.id.editPass).setVisibility(View.GONE);
            findViewById(R.id.editEmail).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
        } else {
            statusTextView.setText("Signed Out.");
            findViewById(R.id.editPass).setVisibility(View.VISIBLE);
            findViewById(R.id.editEmail).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }


}
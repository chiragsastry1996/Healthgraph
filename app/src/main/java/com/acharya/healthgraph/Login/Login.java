package com.acharya.healthgraph.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.acharya.healthgraph.GameList.MainActivity;
import com.acharya.healthgraph.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONObject;

public class Login extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = Login.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    ProgressBar progressBar;

    private GoogleApiClient mGoogleApiClient;

    private SignInButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(Login.this,
                        MainActivity.class);
                intent.putExtra("auth",2);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }});

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        // If already logged in show the home view
        if (accessToken != null) {//<- IMPORTANT
            Intent intent = new Intent(Login.this,
                    MainActivity.class);
            intent.putExtra("auth",2);
            startActivity(intent);
            finish();//<- IMPORTANT
        }


    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        try{ if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String email = acct.getEmail();

            Intent i = new Intent(Login.this, MainActivity.class);
            i.putExtra("auth",1);
            startActivity(i);
            finish();

            Log.e(TAG, "Name: " + personName + ", email: " + email);
        }
        } catch (Exception e){
            // Signed out, show unauthenticated UI.
            Log.d(TAG,"Login Failed"+ e);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            progressBar.setVisibility(View.VISIBLE);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    progressBar.setVisibility(View.GONE);
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

//    protected void getUserDetails(LoginResult loginResult) {
//        GraphRequest data_request = GraphRequest.newMeRequest(
//                loginResult.getAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(
//                            JSONObject json_object,
//                            GraphResponse response) {
//                        Intent intent = new Intent(Login.this,
//                                MainActivity.class);
//                        startActivity(intent);
//                    }
//
//                });
//        Bundle permission_param = new Bundle();
//        permission_param.putString("fields", "id,name,email, picture.width(120).height(120)");
//                data_request.setParameters(permission_param);
//        data_request.executeAsync();
//
//    }

}
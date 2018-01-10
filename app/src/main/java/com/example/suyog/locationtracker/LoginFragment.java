package com.example.suyog.locationtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by ADMIN-PC on 10-01-2018.
 */

public class LoginFragment extends Fragment implements LoginHandler
{
    private static final int RC_SIGN_IN=123;//constant for result code for startActivityForResult in firebase UI
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private String TAG = "Login";
    private Button mButtonSignOut;
    public static LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initAuth();
        initAuthListener();
toast("Hello");
    }
    private void initAuthListener(){
        Log.d(TAG,"authListenerinited");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = mAuthListener==null?new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("LoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    toast("signed in as "+email);

                    //SharedPreferenceHandler.getInstance(MainActivity.this).add(MainActivity.EMAIL,email);
                    //startActivity(HelloWorldActivity.newIntent(MainActivity.this));

                } else {
                    // User is signed out
                    Log.d("LoginActivity", "onAuthStateChanged:signed_out");
                    //SharedPreferenceHandler.getInstance(MainActivity.this).remove(MainActivity.EMAIL);
                    toast("access denied");
                    signIn();
                }
            }
        }:mAuthListener;
        mAuth.addAuthStateListener(mAuthListener);
    }
    private void initAuth()
    {
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                // be available.
                Log.d("LoginActivity", "onConnectionFailed:" + connectionResult);
                toast("Google Play Services error.");
            }

        };
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivity */, connectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.hello, container, false);
        mButtonSignOut = (Button) view.findViewById(R.id.signout);
        mButtonSignOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signOut();
            }
        });
        return view;
    }

    @Override
    public void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void signOut()
    {
        try
        {
            mAuth.signOut();
            //SharedPreferenceHandler.getInstance(this).remove(MainActivity.EMAIL);
            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            toast("Logged Out");
                        }
                    });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"activity result");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                toast("bhai bhai bhai");
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                //SharedPreferenceHandler.getInstance(MainActivity.this).add(MainActivity.EMAIL,account.getEmail());


                //startActivity(FeedPagerActivity.newIntent(MainActivity.this));
                //IpClass.getInstance().setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            } else {
                toast("koi nagar paika ko bulao");
                // Google Sign In failed, update UI appropriately
                Log.i("REQ-CODE",new String(String.valueOf(resultCode)));
                //updateUI(null);


            }




        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("LoginFragment", "firebaseAuthWithGoogle:" + acct.getId());



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LoginActivity", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LoginActivity", "signInWithCredential", task.getException());
                            toast("Firebase Authentication failed.");
                        }


                    }
                });
    }



    @Override
    public void toast(String x)
    {
        Toast.makeText(getContext(),x, Toast.LENGTH_SHORT).show();
    }
}

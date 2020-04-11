package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecoleenligne.views.HomeActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int RC_SIGN_IN = 1;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    public ProgressBar mProgressBar;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton mGoogleSignIn;
    private Button mBtnSignUp;
    private Button mBtnSignIn;
    private String name;
    private FirebaseAuth mAuth;
    /*private FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference refDb = db.getReference("user");

    ArrayList<String> users = new ArrayList<>();
    ListView myListView;
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO - Login Spinner
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setProgressBar(R.id.progressBar);
        // Initialize Firebase Auth

        // Setup Facebook Login method
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.btn_fb_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("MainActivity", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("MainActivity", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("MainActivity", "facebook:onError", error);
                // ...
            }
        });

        // Setup Google SignIn method
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mEditTextEmail = findViewById(R.id.edtEmail);
        mEditTextPassword = findViewById(R.id.edtPassword);
        mGoogleSignIn = findViewById(R.id.btn_google_login);
        mBtnSignUp = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_google_login).setOnClickListener(this);

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","onClick register");
                createNewUser(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
            }
        });

        mBtnSignIn = findViewById(R.id.button_already_account);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","onClick already");
                signInAlreadyAccount(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
            }
        });


        /*
        mEditTextEmail = findViewById(R.id.editText);
        mButton = findViewById(R.id.button);
        myListView = findViewById(R.id.list_view);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users);

        myListView.setAdapter(adapter);

        refDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String username = dataSnapshot.getValue(String.class);
                Log.d("onChildAdded",username);
                users.add(username);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String username = dataSnapshot.getValue(String.class);
                //String keyIndex = dataSnapshot.getKey();
                //users.set(Integer.valueOf(keyIndex),username);
                Log.d("onChildChanged username", username);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                Log.d("onChildRemoved",username);
                users.remove(username);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("onChildMoved","ok");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("onChildCancelled","ok");
                users.clear();
                adapter.notifyDataSetChanged();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference childRef = refDb.child("email");
                name = mEditTextEmail.getText().toString();
                childRef.setValue(name);
                Toast.makeText(MainActivity.this, "data added!", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }



    private void signInAlreadyAccount(String email, String password) {
        Log.d("MainActivity", "signIn Already Account:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MainActivity", "signInAlreadyAccount:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MainActivity", "signInAlreadyAccount:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressBar();
                    }
                });
    }


    private void createNewUser(String email, String password) {
        Log.d("MainActivity", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressBar();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("MainActivity", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w("MainActivity", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                hideProgressBar();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        //Check if the user is signed in (non null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEditTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEditTextEmail.setError("Required.");
            valid = false;
        } else {
            mEditTextEmail.setError(null);
        }

        String password = mEditTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mEditTextPassword.setError("Required.");
            valid = false;
        } else {
            mEditTextPassword.setError(null);
        }
        return valid;
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Log.d("onStart", "Aggiornare l'UI");
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }else
            Log.d("onStart", "Nessun utente corrente. Default UI");
    }


    private void sendEmailVerification() {
        // Disable button
        //findViewById(R.id.verifyEmailButton).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        //findViewById(R.id.verifyEmailButton).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("MainActivity", "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }



    // TODO - inserire questi metodi e la ProgressBar in una superclasse BaseActivity,
    //  in modo che tutte le successive activities possono ereditare.

    public void setProgressBar(int resId) {
        mProgressBar = findViewById(resId);
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainLayout.setBackgroundColor(Color.GRAY);
            //mainLayout.setVisibility(View.GONE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainLayout.setBackgroundColor(Color.WHITE);

        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google_login:
                signInByGoogle();
                break;
            case R.id.btn_fb_login:
                signInByFacebook();
        }
    }

    private void signInByFacebook() {
    }


    private void signInByGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // Google Client get the SignInIntent and after call startActivityResult(signInIntent, RC_SIGN_IN)
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("MainActivity", "Google sign in failed", e);
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.getId());
        Log.d("MainActivity", "access token:" + account.getIdToken());
        if(account.getIdToken() != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("MainActivity", "signInWithGoogle:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("MainActivity", "signInWithFacebook:failure", task.getException());
                                Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }else{
            //TODO - user need to be created, so display Registration Activity
            mAuth.createUserWithEmailAndPassword(account.getEmail(), account.getId())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d("MainActivity", "createUserWithGoogle:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //TODO - creare dati user in Firebase RealTime Database
                                //updateUI(user);
                            } else {
                                Log.w("MainActivity", "createUserWithGoogle:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                            hideProgressBar();
                        }
                    });
        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("MainActivity", "access token:" + accessToken);
        if(accessToken != null) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("MainActivity", "signInWithFacebook:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("MainActivity", "signInWithFacebook:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                        }
                    });
        }else{
            //TODO - user need to be created, so display Registration Activity
            Log.d("MainActivity","FacebookLogin: access token null");
        }
    }


    // fino qui
}

package com.example.ecoleenligne;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    public ProgressBar mProgressBar;
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


        mEditTextEmail = findViewById(R.id.edtEmail);
        mEditTextPassword = findViewById(R.id.edtPassword);
        mBtnSignUp = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();


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

    /*
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button) {

        } else if (i == R.id.button_already_account) {

        } else if (i == R.id.signOutButton) {
            signOut();
        } else if (i == R.id.verifyEmailButton) {
            sendEmailVerification();
        } else if (i == R.id.reloadButton) {
            reload();
        }
    }
    */

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
        //hideProgressBar();
    }

    // fino qui
}

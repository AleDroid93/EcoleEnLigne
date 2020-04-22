package com.example.ecoleenligne.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecoleenligne.HomeActivity2;
import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.UserInfo;
import com.example.ecoleenligne.repositories.UserInfoRepository;
import com.example.ecoleenligne.viewmodels.UserInfoViewModel;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener{
    private final int RC_SIGN_IN = 1;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    public ProgressBar mProgressBar;
    private TextView mRegistrationLink;
    private CallbackManager mCallbackManager;
    private LoginButton mFacebookSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton mGoogleSignIn;
    private Button mBtnSignIn;
    private FirebaseAuth mAuth;
    private NavController navController;
    private UserInfoRepository userInfoRepository;
    private UserInfoViewModel model;
    private Observer<UserInfo> observerUserInfo;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int id = this.getId();
        String id_name = this.getString(id);
        Log.d("SignInFragment", "id: "+id);
        Log.d("SignInFragment", "id_name: "+id_name);
        navController = Navigation.findNavController(view);
        // Setup Facebook Login method
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookSignIn = view.findViewById(R.id.btn_fb_login);
        mFacebookSignIn.setReadPermissions("email", "public_profile");
        registerFacebookCallback();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        mEditTextEmail = view.findViewById(R.id.edtEmail);
        mEditTextPassword = view.findViewById(R.id.edtPassword);
        mGoogleSignIn = view.findViewById(R.id.btn_google_login);
        setGooglePlusButtonText(mGoogleSignIn, getString(R.string.google));
        mBtnSignIn = view.findViewById(R.id.btn_email_signin);
        mBtnSignIn.setOnClickListener(SignInFragment.this);
        mRegistrationLink = view.findViewById(R.id.tv_register_now);
        mRegistrationLink.setOnClickListener(SignInFragment.this);
        mGoogleSignIn.setOnClickListener(SignInFragment.this);
        mFacebookSignIn.setOnClickListener(SignInFragment.this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        observerUserInfo = getUserInfoObserver();
    }

    private void registerFacebookCallback() {
        mFacebookSignIn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("SignInFragment", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("SignInFragment", "facebook:onCancel");
                Toast.makeText(getActivity(), "Facebook login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("SignInFragment", "facebook:onError", error);
                Toast.makeText(getActivity(), "Facebook login failed.\n"+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Observer<UserInfo> getUserInfoObserver() {
        model = ViewModelProviders.of(SignInFragment.this).get(UserInfoViewModel.class);
        final Observer<UserInfo> observerUserInfo = new Observer<UserInfo>() {
            @Override
            public void onChanged(@Nullable UserInfo userInfo) {
                //Bundle bundle = new Bundle();
                //bundle.putParcelable("user",userInfo);
                Intent intent = new Intent(getActivity(), HomeActivity2.class);
                intent.putExtra("user", userInfo);
                //navController.navigate(R.id.action_signInFragment_to_homeActivity2, bundle);
                startActivity(intent);
            }
        };
        return observerUserInfo;
    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        int length = signInButton.getChildCount();
        for (int i = 0; i < length; i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tv.setBackground(getActivity().getDrawable(R.drawable.button_google));
                    tv.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.drawable.google_24),
                            null, null, null);
                    tv.setCompoundDrawablePadding(0);
                    Log.i("SignInFragment", "padding logo-testo: "+ tv.getCompoundDrawablePadding());
                }
                return;
            }else{
                Log.d("SignInFragment", "view: "+ v.getClass().getName());
            }
        }
    }


    private void signInByEmailAndPassword(String email, String password) {
        Log.d("LoginActivity", "signIn Already Account:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInFragment", "signInByEmailAndPassword:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String name = user.getDisplayName();
                            Log.i("SignInFragment", "user info: \n{\n \t email: " + email + ",\n \t name: " + name + "}");
                            String uid = user.getUid();
                            userInfoRepository = UserInfoRepository.getInstance();
                            model.init(uid);
                            model.getUserInfoRepository().observe(SignInFragment.this, observerUserInfo);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInFragment", "signInByEmailAndPassword:failure", task.getException());
                            Toast.makeText(getActivity(), "Sign in failed. "
                                            + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        //hideProgressBar();
                    }
                });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google_login:
                signInByGoogle();
                break;
            case R.id.btn_fb_login:
                //signInByFacebook();
                Log.d("SignInFragment","hey");
                //registerFacebookCallback();
                break;
            case R.id.btn_email_signin:
                Log.d("SignInFragment","onClick: signInByEmail button pressed");
                signInByEmailAndPassword(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
                break;
            case R.id.tv_register_now:
                Log.d("SignInFragment","onClick: signup textView pressed");
                navController.navigate(R.id.action_signInFragment_to_signUpFragment);
                break;
        }
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
                Log.w("LoginActivity", "Google sign in failed", e);
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("SignInFragment", "firebaseAuthWithGoogle:" + account.getId());
        Log.d("SignInFragment", "access token:" + account.getIdToken());
        if (account.getIdToken() != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignInFragment", "signInWithGoogle:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();
                                String email = user.getEmail();
                                String name = user.getDisplayName();
                                Log.i("SignInFragment", "user info: \n{\n \t uid:" + uid + "," +
                                        "\n \t email: " + email + ",\n \t name: " + name + "\n}");
                                userInfoRepository = UserInfoRepository.getInstance();
                                model.init(uid);
                                model.getUserInfoRepository().observe(SignInFragment.this, observerUserInfo);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignInFragment", "signInWithGoogle:failure", task.getException());
                                Snackbar.make(getView().findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("LoginActivity", "access token:" + accessToken);
        if(accessToken != null) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginActivity", "signInWithFacebook:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email = user.getEmail();
                                String name = user.getDisplayName();
                                String uid = user.getUid();
                                Log.i("SignInFragment", "user info: \n{\n \t email: "+email+",\n \t name: "+name+"}");
                                userInfoRepository = UserInfoRepository.getInstance();
                                model.init(uid);
                                model.getUserInfoRepository().observe(SignInFragment.this, observerUserInfo);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignInFragment", "signInWithFacebook:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else{
            Log.d("SignInFragment","FacebookLogin: access token null");
        }
    }

}

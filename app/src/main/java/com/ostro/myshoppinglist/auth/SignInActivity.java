package com.ostro.myshoppinglist.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.BaseActivity;
import com.ostro.myshoppinglist.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public class SignInActivity extends BaseActivity {

    @BindView(R.id.main_container)
    LinearLayout mainViewContainer;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public int getLayoutResources() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.login_title));
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Timber.d("Signed in");
                } else {
                    Timber.d("Signed out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInClick() {
        if (validateFields(etEmail.getText().toString(),
                etPassword.getText().toString())) {

            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),
                    etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignInActivity.this,
                                        getString(R.string.error_logging_in), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
            errorSnackBar(mainViewContainer, getString(R.string.error_fill_all_fields));
        }
    }

    private boolean validateFields(String email, String password) {
        return !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password);
    }

    @OnClick(R.id.tv_sign_up)
    public void onSignUpClick() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}

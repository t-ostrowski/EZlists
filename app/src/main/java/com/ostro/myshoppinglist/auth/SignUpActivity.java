package com.ostro.myshoppinglist.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.BaseActivity;
import com.ostro.myshoppinglist.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.main_container)
    LinearLayout mainViewContainer;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etPasswordConfirmation;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @Override
    public int getLayoutResources() {
        return R.layout.activity_sign_up;
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        if (validateFields(etLastName.getText().toString(),
                etFirstName.getText().toString(),
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etPasswordConfirmation.getText().toString())) {
            if (passwordsAreEqual(etPassword.getText().toString(),
                    etPasswordConfirmation.getText().toString())) {

                mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                        etPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this,
                                            getString(R.string.sign_up_successful), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this,
                                            getString(R.string.sign_up_failed), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else {
                errorSnackBar(mainViewContainer, getString(R.string.sign_up_passwords_not_equal));
            }
        } else {
            errorSnackBar(mainViewContainer, getString(R.string.error_fill_all_fields));
        }
    }

    private boolean validateFields(String lastname, String firstname,
                                   String email, String password,
                                   String passwordConfirmation) {
        return !TextUtils.isEmpty(lastname) &&
                !TextUtils.isEmpty(firstname) &&
                !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(passwordConfirmation);
    }

    private boolean passwordsAreEqual(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

}

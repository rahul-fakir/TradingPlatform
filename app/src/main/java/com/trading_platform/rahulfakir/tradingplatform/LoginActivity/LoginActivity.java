package com.trading_platform.rahulfakir.tradingplatform.LoginActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.trading_platform.rahulfakir.tradingplatform.DataValidation.DataValidation;
import com.trading_platform.rahulfakir.tradingplatform.MainActivity.MainActivity;
import com.trading_platform.rahulfakir.tradingplatform.R;

public class LoginActivity extends AppCompatActivity {

    //  Firebase
    private FirebaseAuth mAuth;

    //  Components
    private Button btnLogin;
    private EditText edtEmailAddress, edtPassword;
    private ProgressBar pgLogginIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  Component Initialization
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        pgLogginIn = (ProgressBar) findViewById(R.id.pgLoggingIn);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //  Firebase Initialization
        mAuth = FirebaseAuth.getInstance();

        //  Action Handlers
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            pgLogginIn.setVisibility(View.VISIBLE);
            String emailAddress = edtEmailAddress.getText().toString();
            String password = edtPassword.getText().toString();

            DataValidation validator = new DataValidation();

            DataValidation.ValidationResult emailValidationResult = validator.validateEmailAddress(emailAddress);
            DataValidation.ValidationResult passwordValidationResult =  validator.validatePassword(password);

            if (emailValidationResult.getValidationCode() != 0) {
                displayErrorMessage(emailValidationResult.getValidationMessage());
                return;
            } else if (passwordValidationResult.getValidationCode() != 0) {
                displayErrorMessage(passwordValidationResult.getValidationMessage());
                return;
            }

            attemptUserLogin(emailAddress, password);
            }
        });
    }

    private void attemptUserLogin(String emailAddress, String password) {
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pgLogginIn.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                });

    }

    private void displayErrorMessage(String errorMessage) {
        pgLogginIn.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }

}

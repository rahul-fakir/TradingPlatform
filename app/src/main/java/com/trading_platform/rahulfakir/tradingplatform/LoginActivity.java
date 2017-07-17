package com.trading_platform.rahulfakir.tradingplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    //  Firebase
    private FirebaseAuth mAuth;

    //  Components
    private Button btnLogin;
    private EditText edtEmailAddress, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  Component Initialization
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //  Firebase Initialization
        mAuth = FirebaseAuth.getInstance();

        //  Action Handlers
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = edtEmailAddress.getText().toString();
                String password = edtPassword.getText().toString();

                DataValidation validator = new DataValidation();

                DataValidation.ValidationResult emailValidationResult = validator.validateEmailAddress(emailAddress);
                DataValidation.ValidationResult passwordValidationResult =  validator.validatePassword(password);

                if (emailValidationResult.getValidationCode() != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), emailValidationResult.getValidationMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (passwordValidationResult.getValidationCode() != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), passwordValidationResult.getValidationMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                attemptUserLogin(emailAddress, password);
            }
        });
    }

    private void attemptUserLogin(String emailAddress, String password) {
        Toast toast = Toast.makeText(getApplicationContext(), "logging user in", Toast.LENGTH_SHORT);
        toast.show();
    }


}

package com.trading_platform.rahulfakir.tradingplatform;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    // Components
    Button btnCreateAccount;
    EditText edtFirstName, edtLastName, edtEmailAddress, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //  Component Initialization
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = edtEmailAddress.getText().toString();
                String password = edtPassword.getText().toString();
                String firstName = edtFirstName.getText().toString();
                String lastName = edtLastName.getText().toString();

                DataValidation validator = new DataValidation();
                DataValidation.ValidationResult firstNameValidationResult = validator.validateName(firstName, "First Name");
                DataValidation.ValidationResult lastNameValidationResult = validator.validateName(lastName, "Last Name");
                DataValidation.ValidationResult emailValidationResult = validator.validateEmailAddress(emailAddress);
                DataValidation.ValidationResult passwordValidationResult =  validator.validatePassword(password);

                if (firstNameValidationResult.getValidationCode() != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), firstNameValidationResult.getValidationMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (lastNameValidationResult.getValidationCode() != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), lastNameValidationResult.getValidationMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (emailValidationResult.getValidationCode() != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), emailValidationResult.getValidationMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else if (passwordValidationResult.getValidationCode() != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), passwordValidationResult.getValidationMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                proceedToAccountRegistration(firstName, lastName, emailAddress, password);
            }
        });
    }

    private void proceedToAccountRegistration(String firstName, String lastName, String emailAddress, String password) {
        Intent intent = new Intent(getApplicationContext(), AccountSetupActivity.class);
        intent.putExtra("FIRST_NAME", firstName);
        intent.putExtra("LAST_NAME", lastName);
        intent.putExtra("EMAIL_ADDRESS", emailAddress);
        intent.putExtra("PASSWORD", password);
        startActivity(intent);
    }
}

package com.trading_platform.rahulfakir.tradingplatform.RegistrationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.trading_platform.rahulfakir.tradingplatform.DataValidation.DataValidation;
import com.trading_platform.rahulfakir.tradingplatform.MainActivity.MainActivity;
import com.trading_platform.rahulfakir.tradingplatform.R;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmBalanceModel;
import com.trading_platform.rahulfakir.tradingplatform.Realm.RealmModels.RealmUserModel;
import com.trading_platform.rahulfakir.tradingplatform.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;

public class RegistrationActivity extends AppCompatActivity {

    //  Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    //Realm
    private Realm realm;
    // Components
    Button btnCreateAccount;
    EditText edtFirstName, edtLastName, edtEmailAddress, edtPassword;
    ProgressBar pgCreatingAccount;

    //  Internal variables
    String emailAddress, password, firstName, lastName, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        database = FirebaseDatabase.getInstance();
        realm = Realm.getDefaultInstance();

        //  Component Initialization
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        pgCreatingAccount = (ProgressBar) findViewById(R.id.pgLoggingIn);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgCreatingAccount.setVisibility(View.VISIBLE);
                emailAddress = edtEmailAddress.getText().toString();
                password = edtPassword.getText().toString();
                firstName = edtFirstName.getText().toString();
                lastName = edtLastName.getText().toString();

                DataValidation validator = new DataValidation();
                DataValidation.ValidationResult firstNameValidationResult = validator.validateName(firstName, "First Name");
                DataValidation.ValidationResult lastNameValidationResult = validator.validateName(lastName, "Last Name");
                DataValidation.ValidationResult emailValidationResult = validator.validateEmailAddress(emailAddress);
                DataValidation.ValidationResult passwordValidationResult =  validator.validatePassword(password);

                if (firstNameValidationResult.getValidationCode() != 0) {
                    displayErrorMessage(firstNameValidationResult.getValidationMessage());
                    return;
                } else if (lastNameValidationResult.getValidationCode() != 0) {
                    displayErrorMessage(lastNameValidationResult.getValidationMessage());
                    return;
                } else if (emailValidationResult.getValidationCode() != 0) {
                    displayErrorMessage(emailValidationResult.getValidationMessage());
                    return;
                } else if (passwordValidationResult.getValidationCode() != 0) {
                    displayErrorMessage(passwordValidationResult.getValidationMessage());
                    return;
                }

                createUserAccount();
            }
        });
    }

    private void createUserAccount() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pgCreatingAccount.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            uid = user.getUid();
                            setUserData();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    private void setUserData() {

        JSONObject params = new JSONObject();
        JSONObject idParams = new JSONObject();
        try {
            params.put("uid", uid);
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("baseCountry", "USA");

            idParams.put("ssn", "7292967");
            idParams.put("passportNumber", "A1085728");
            params.put("identificationData", idParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://us-central1-trading-platform-7fa0e.cloudfunctions.net/setupUserAccount", params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmUserModel user = realm.createObject(RealmUserModel.class);
                                user.setUid(uid);
                                user.setEmail(emailAddress);
                                user.setFirstName(firstName);
                                user.setLastName(lastName);

                                RealmBalanceModel balances = realm.createObject(RealmBalanceModel.class);
                                balances.setCurrentBalance(0.0);
                                realm.insertOrUpdate(balances);
                                realm.insertOrUpdate(user);
                            }



                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                pgCreatingAccount.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                System.out.println(error.getMessage());
                            }
                        });


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pgCreatingAccount.setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
        VolleySingleton volleyHandler = new VolleySingleton(getApplicationContext());
        volleyHandler.getInstance(this).addToRequestQueue(jsObjRequest);
    }



    private void displayErrorMessage(String errorMessage) {
        pgCreatingAccount.setVisibility(View.INVISIBLE);
        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }


}

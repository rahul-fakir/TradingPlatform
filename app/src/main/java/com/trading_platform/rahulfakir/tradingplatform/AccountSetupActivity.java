package com.trading_platform.rahulfakir.tradingplatform;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountSetupActivity extends AppCompatActivity {

    //  Firebase
    private FirebaseAuth mAuth;
    private  FirebaseDatabase database;

    //  Internal Variables
    private String firstName, lastName, emailAddress, password;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        Bundle extras = getIntent().getExtras();
        firstName = extras.get("FIRST_NAME").toString();
        lastName = extras.get("LAST_NAME").toString();
        emailAddress = extras.get("EMAIL_ADDRESS").toString();
        password = extras.get("PASSWORD").toString();

        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        setUserData(user.getUid());
                        updateSetupProgress(user.getUid());
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

    }

    private void setUserData(String uid) {
        DatabaseReference ref = database.getReference("/users/" + uid);

        ref.child("userData/firstName").setValue(firstName);
        ref.child("userData/lastName").setValue(lastName);
        ref.child("setupData/userDataSetup").setValue(true);
    }

    private void updateSetupProgress(String uid) {
        final DatabaseReference ref = database.getReference("/users/" + uid + "/setupData");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Boolean.valueOf(dataSnapshot.child("setupComplete").toString())) {
                    ref.removeEventListener(this);
                }

                // Update UI based on progress here

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    
}

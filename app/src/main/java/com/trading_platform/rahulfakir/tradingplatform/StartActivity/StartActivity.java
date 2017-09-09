package com.trading_platform.rahulfakir.tradingplatform.StartActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trading_platform.rahulfakir.tradingplatform.LoginActivity.LoginActivity;
import com.trading_platform.rahulfakir.tradingplatform.MainActivity.MainActivity;
import com.trading_platform.rahulfakir.tradingplatform.R;
import com.trading_platform.rahulfakir.tradingplatform.RegistrationActivity.RegistrationActivity;

public class StartActivity extends AppCompatActivity {

    //  Components
    Button btnLogin, btnRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        //  Component Initialization
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);

        //  Event Handlers
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }
}

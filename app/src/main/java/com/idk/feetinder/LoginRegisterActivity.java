// Catherine
package com.idk.feetinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button login;
    private Button register;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        getSupportActionBar().hide();

        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.getApplicationContext().CONNECTIVITY_SERVICE);

        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        if(!connected){ // no network
            //no wifi screen
        }

        if(user != null){ // check if already logged in
            Intent intent = new Intent(LoginRegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(view -> {
            Intent intent = new Intent(LoginRegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        register.setOnClickListener(view -> {
            Intent intent = new Intent(LoginRegisterActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
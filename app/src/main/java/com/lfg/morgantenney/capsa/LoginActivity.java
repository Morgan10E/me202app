package com.lfg.morgantenney.capsa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        String userID = sharedPref.getString(getString(R.string.user_id_shared_pref_key), "");

        Log.e("ME202", "Found userID " + userID + " already logged in!");

        if (userID.length() != 0) {
            Intent gardenListIntent = new Intent(getApplicationContext(), GardenListActivity.class);
            startActivity(gardenListIntent);
            finish();
        }

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        emailEditText = (EditText) findViewById(R.id.emailText);
        passwordEditText = (EditText) findViewById(R.id.passwordText);

        final Firebase ref = new Firebase("https://hydrophonic.firebaseio.com");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidInput()) return;
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Intent gardenConnectIntent = new Intent(getApplicationContext(), GardenConnectActivity.class);
//                        gardenConnectIntent.putExtra(getString(R.string.uuid_key), authData.getUid());

                        String userID = authData.getUid();

                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.user_id_shared_pref_key), userID);
                        editor.apply();

                        startActivity(gardenConnectIntent);
                        finish();
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast failToast = Toast.makeText(getApplicationContext(), getString(R.string.login_fail), Toast.LENGTH_SHORT);
                        failToast.show();
                    }
                });
//                Intent gardenConnectIntent = new Intent(getApplicationContext(), GardenConnectActivity.class);
//                startActivity(gardenConnectIntent);
//                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidInput()) return;
//                Intent gardenConnectIntent = new Intent(getApplicationContext(), GardenConnectActivity.class);
//                startActivity(gardenConnectIntent);
//                finish();

                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast successToast = Toast.makeText(getApplicationContext(), getString(R.string.register_success) + email, Toast.LENGTH_SHORT);
                        successToast.show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast errorToast = Toast.makeText(getApplicationContext(), getString(R.string.register_fail) + email, Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                });
            }
        });
    }

    private boolean isValidInput() {
        boolean valid = emailEditText.getText().length() != 0 && passwordEditText.getText().length() != 0;
        if (!valid) {
            Toast failToast = Toast.makeText(getApplicationContext(), getString(R.string.bad_input), Toast.LENGTH_SHORT);
            failToast.show();
        }
        return valid;
    }
}

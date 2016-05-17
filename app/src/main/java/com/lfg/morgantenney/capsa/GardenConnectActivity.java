package com.lfg.morgantenney.capsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.particle.android.sdk.devicesetup.ParticleDeviceSetupLibrary;
import io.particle.android.sdk.utils.Toaster;
import io.particle.android.sdk.utils.ui.Ui;

public class GardenConnectActivity extends AppCompatActivity {

    Button connectButton;
    Button skipButton;
    ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_connect);
        ParticleDeviceSetupLibrary.init(this.getApplicationContext(), GardenConnectActivity.class);

        receiver = new ParticleDeviceSetupLibrary.DeviceSetupCompleteReceiver() {

            @Override
            public void onSetupSuccess(String configuredDeviceId) {
                Toast.makeText(getApplicationContext(), "Registered " + configuredDeviceId, Toast.LENGTH_SHORT).show();
                closeSetup(configuredDeviceId);
            }

            @Override
            public void onSetupFailure() {
                Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
            }
        };
        receiver.register(getApplicationContext());


        connectButton = (Button) findViewById(R.id.startConnectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeDeviceSetup();
            }
        });

        skipButton = (Button) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSetup("");
            }
        });
    }

    public void invokeDeviceSetup() {
        ParticleDeviceSetupLibrary.startDeviceSetup(this);
    }

    private void closeSetup(String deviceID) {
        receiver.unregister(getApplicationContext());
        Intent listIntent = new Intent(getApplicationContext(), GardenListActivity.class);
        listIntent.putExtra(getString(R.string.device_id_key), deviceID);
        startActivity(listIntent);
        finish();
    }
}

package com.lfg.morgantenney.capsa;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by morgantenney on 5/10/16.
 */
public class Capsa extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}

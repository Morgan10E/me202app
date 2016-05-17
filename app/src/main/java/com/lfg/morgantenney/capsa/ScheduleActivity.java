package com.lfg.morgantenney.capsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.lang.reflect.Array;

public class ScheduleActivity extends AppCompatActivity {

    private Firebase scheduleFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        String photonID = getIntent().getStringExtra(getString(R.string.schedule_device_id_key));

        String[] timeslots = {"12:00am - 1:00am", "1:00am - 2:00am", "2:00am - 3:00am", "3:00am - 4:00am", "4:00am - 5:00am",
                "5:00am - 6:00am", "6:00am - 7:00am", "7:00am - 8:00am", "8:00am - 9:00am", "9:00am - 10:00am", "10:00am - 11:00am",
                "11:00am - 12:00pm", "12:00pm - 1:00pm", "1:00pm - 2:00pm", "2:00pm - 3:00pm", "3:00pm - 4:00pm", "4:00pm - 5:00pm",
                "5:00pm - 6:00pm", "6:00pm - 7:00pm", "7:00pm - 8:00pm", "8:00pm - 9:00pm", "9:00pm - 10:00pm", "10:00pm - 11:00pm", "11:00pm - 12:00am"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScheduleActivity.this, R.layout.schedule_row_layout, timeslots);
        final ListView lv = (ListView) findViewById(R.id.LV);
        lv.setAdapter(adapter);
        ImageView im = (ImageView) findViewById(R.id.image);
        im.setImageResource(R.drawable.tallplant);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lv.isItemChecked(position)) {
                    scheduleFirebase.child(adapter.getItem(position)).setValue(true);
                } else {
                    scheduleFirebase.child(adapter.getItem(position)).setValue(null);
                }
            }
        });


        Firebase rootRef = new Firebase("https://hydrophonic.firebaseio.com/web/data");
        scheduleFirebase = rootRef.child(getString(R.string.garden_schedule_firebase_child)).child(photonID);

        Query queryRef = scheduleFirebase.orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("ME202",dataSnapshot.getKey());
                lv.setItemChecked(adapter.getPosition(dataSnapshot.getKey()), true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("ME202",dataSnapshot.getKey());
                lv.setItemChecked(adapter.getPosition(dataSnapshot.getKey()), false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}

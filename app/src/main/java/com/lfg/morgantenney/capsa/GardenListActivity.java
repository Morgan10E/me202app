package com.lfg.morgantenney.capsa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Map;

public class GardenListActivity extends AppCompatActivity {

    private ArrayList<Garden> list;
    private ListView gardenListView;
    private GardenAdapter gAdapter;
    private String userID;

    private Firebase gardenListFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_list);

        gardenListView = (ListView) findViewById(R.id.gardenListView);

        list = new ArrayList<Garden>();

        gAdapter = new GardenAdapter(
                getApplicationContext(),
                R.layout.garden_row_layout,
                list
        );

        gardenListView.setAdapter(gAdapter);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        userID = sharedPref.getString(getString(R.string.user_id_shared_pref_key), "");
        Log.e("ME202", "USERID " + userID);

        Firebase rootRef = new Firebase("https://hydrophonic.firebaseio.com/web/data");
        gardenListFirebase = rootRef.child(getString(R.string.garden_list_firebase_child)).child(userID);
        // FIREBASE LOADING HERE

        Query queryRef = gardenListFirebase.orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Garden entry = dataSnapshot.getValue(Garden.class);
                if (!list.contains(entry)) {
                    Log.e("ME202", "FOUND CHILD");
                    gAdapter.add(entry);
                    Log.e("ME202", list.size() + "");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("ME202", "CHILD CHANGED");
                Garden entry = dataSnapshot.getValue(Garden.class);
                list.set(list.indexOf(entry), entry);
                gAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Garden entry = dataSnapshot.getValue(Garden.class);
                list.remove(entry);
                Log.e("ME202", "REMOVED CHILD");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //ADD NEW GARDEN IF IT EXISTS
        addNewGarden();

        // ANY CLICK OR LONG PRESS ACTIONS HERE
        gardenListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                final Garden longClicked = list.get(pos);

                final Dialog modifyGardenDialog = new Dialog(GardenListActivity.this);
                modifyGardenDialog.setContentView(R.layout.garden_modify_layout);

                final EditText newGardenNameText = (EditText)modifyGardenDialog.findViewById(R.id.newName);
                Button okButton = (Button)modifyGardenDialog.findViewById(R.id.okButton);
                Button cancelButton = (Button)modifyGardenDialog.findViewById(R.id.cancelButton);
                Button deleteButton = (Button)modifyGardenDialog.findViewById(R.id.deleteButton);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newGardenName = newGardenNameText.getText().toString();
                        if (newGardenName.length() == 0) {
                            Toast idToast = Toast.makeText(getApplicationContext(), getString(R.string.bad_input), Toast.LENGTH_SHORT);
                            idToast.show();
//                        } else if (userIDs.contains(newUserID)) {
//                            Toast idToast = Toast.makeText(getApplicationContext(), getString(R.string.already_exists), Toast.LENGTH_SHORT);
//                            idToast.show();
                        } else {
                            longClicked.name = newGardenName;
                            gardenListFirebase.child(longClicked.firebaseKey).setValue(longClicked);
                            modifyGardenDialog.dismiss();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyGardenDialog.dismiss();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gardenListFirebase.child(longClicked.firebaseKey).setValue(null);
                        gAdapter.remove(longClicked);
                        modifyGardenDialog.dismiss();
                    }
                });
                modifyGardenDialog.show();

                return true;
            }
        });

        gardenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Garden clicked = list.get(position);

                Intent scheduleIntent = new Intent(getApplicationContext(), ScheduleActivity.class);
                scheduleIntent.putExtra(getString(R.string.schedule_device_id_key), clicked.photonID);
                startActivity(scheduleIntent);
            }
        });
    }


    public void addNewGarden() {
        String gardenID = getIntent().getStringExtra(getString(R.string.device_id_key));
        if (gardenID == null || gardenID.length() == 0) {
            Toast noTextToast = Toast.makeText(this, getString(R.string.no_new_garden), Toast.LENGTH_SHORT);
            noTextToast.show();
        } else {
            Firebase fKey = gardenListFirebase.push();
            Garden newEntry = new Garden(gardenID, getString(R.string.water_level_fine), getString(R.string.light_state_on), fKey.getKey(), gardenID);
            if (list.contains(newEntry)) {
                fKey.setValue(null);
            } else {
                gAdapter.add(newEntry);
                fKey.setValue(newEntry);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionLogout:
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);

                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.user_id_shared_pref_key));
                editor.commit();

                startActivity(loginIntent);
                finish();
                return true;

            case R.id.actionNewGarden:
                Intent newGardenIntent = new Intent(getApplicationContext(), GardenConnectActivity.class);
                startActivity(newGardenIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

package com.example.assignment10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    DatabaseHelper myDb;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);
        listView = findViewById(R.id.lv);

        setToListView();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        // WHEN THE SCREEN IS ABOUT TO TURN OFF
        if (ScreenReceiver.wasScreenOn) {
            // THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
            myDb.insertData("closed");
            setToListView();
        } else {
            // THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        // ONLY WHEN SCREEN TURNS ON
        if (!ScreenReceiver.wasScreenOn) {
            // THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
            myDb.insertData("opened");
            setToListView();
        } else {
            // THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onResume();
    }

    private void setToListView(){
        ArrayList<DataModel> dataModelArrayList =  new ArrayList<>();;
        Cursor res = myDb.getAll();

        if(res.getCount() == 0){
            return;
        }

        while(res.moveToNext()){
            DataModel playerModel = new DataModel();
            playerModel.setEvent(res.getString(1));
            playerModel.setTimeStamp(res.getString(2));

            dataModelArrayList.add(playerModel);
        }

        setupListview(dataModelArrayList);
    }

    private void setupListview(ArrayList<DataModel> dataModelArrayList){
        listAdapter = new ListAdapter(this, dataModelArrayList);
        listView.setAdapter(listAdapter);
    }
}

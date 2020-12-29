package com.example.bgctub_transport_tracker_trans_authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransportInfoDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String userId;
    private TextView statusTextView;
    private TextView driverNameTextView,driverContactTextView,driverAddressTextView;
    private TextView transportNameTextView, transportNumberTextView;
    private TextView schedule_time_TextView,schedule_day_TextView,scheduleRoadTextView, start_loc_TextView,destinitionTextView;
    private TextView copyTextView,copyAllUsersInfoTextView;
    private DatabaseReference transportInfoDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_info_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        //get userId from transport information fragment
        userId=getIntent().getStringExtra("userId");


        statusTextView=findViewById(R.id.driver_status_textview);

        driverNameTextView=findViewById(R.id.driver_name_textview);
        driverContactTextView=findViewById(R.id.driver_contact_textview);
        driverAddressTextView=findViewById(R.id.driver_address_textview);


        transportNameTextView=findViewById(R.id.transport_name_textview);
        transportNumberTextView=findViewById(R.id.transport_number_textview);

        schedule_time_TextView=findViewById(R.id.schedule_time_textview);
        schedule_day_TextView=findViewById(R.id.schedule_day_textview);
        scheduleRoadTextView=findViewById(R.id.shedule_road_textview);
        start_loc_TextView=findViewById(R.id.schedule_start_loc_textview);
        destinitionTextView=findViewById(R.id.schedule_destinition_textview);

        copyTextView=findViewById(R.id.copy_contact_textview);
        copyTextView.setOnClickListener(this);

        copyAllUsersInfoTextView = findViewById(R.id.copy_driver_users_info_textview);
        copyAllUsersInfoTextView.setOnClickListener(this);


        //database path and references
        final String DATABASE_PATH= "driver_app" + "/" +"transport_info_location"+"/"+ userId;

        transportInfoDatabaseRef= FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //add information method
        addInformation();
    }


    //if data available data add to textView**
    public void addInformation(){

        transportInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    String name = snapshot.child("transport_information").child("driver_name").getValue().toString();
                    String contact=snapshot.child("transport_information").child("driver_contact").getValue().toString();
                    String address=snapshot.child("transport_information").child("driver_address").getValue().toString();
                    String transName = snapshot.child("transport_information").child("vehicle_name").getValue().toString();
                    String transNumber = snapshot.child("transport_information").child("vehicle_number").getValue().toString();
                    String schTime = snapshot.child("transport_information").child("start_time_schedule").getValue().toString();
                    String schDate = snapshot.child("transport_information").child("start_date_schedule").getValue().toString();
                    String schRoad = snapshot.child("transport_information").child("travel_road").getValue().toString();
                    String startLoc = snapshot.child("transport_information").child("start_location").getValue().toString();
                    String destination = snapshot.child("transport_information").child("destinition").getValue().toString();

                    driverNameTextView.setText(name);
                    driverContactTextView.setText(contact);
                    driverAddressTextView.setText(address);
                    transportNameTextView.setText(transName);
                    transportNumberTextView.setText(transNumber);
                    schedule_time_TextView.setText(schTime);
                    schedule_day_TextView.setText(schDate);
                    scheduleRoadTextView.setText(schRoad);
                    start_loc_TextView.setText(startLoc);
                    destinitionTextView.setText(destination);

                    //for driver status **
                    try {
                        String status = snapshot.child("status").getValue().toString();
                        if (status.equals("active")) {
                            //if active change text color to green and set text active
                            statusTextView.setText(status);
                            statusTextView.setTextColor(Color.GREEN);
                        }
                        if (status.equals("inactive")) {
                            statusTextView.setText(status);
                            statusTextView.setTextColor(Color.RED);
                        }
                    }catch (Exception exception){

                    }

                }catch(Exception exception){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransportInfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==copyTextView){
            String contact= driverContactTextView.getText().toString().trim();
            if(contact!=null){
                //copy driver contact number to clipboard**
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Driver Contact",contact);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(TransportInfoDetailsActivity.this,"Contact Number Copied", Toast.LENGTH_LONG).show();
            }
        }
        if(v==copyAllUsersInfoTextView){
            String name = driverNameTextView.getText().toString().trim();
            String contact=driverContactTextView.getText().toString().trim();
            String address= driverAddressTextView.getText().toString().trim();
            String transName = transportNameTextView.getText().toString().trim();
            String transNumber =transportNumberTextView.getText().toString().trim();
            String schTime = schedule_time_TextView.getText().toString().trim();
            String schDate = schedule_day_TextView.getText().toString().trim();
            String schRoad = scheduleRoadTextView.getText().toString().trim();
            String startLoc = start_loc_TextView.getText().toString().trim();
            String destination = destinitionTextView.getText().toString().trim();


            String allInformation ="Driver Profile:\n\n"
                    + "Name:\n" + name + "\n\n"
                    + "Contact Number:\n" + contact + "\n\n"
                    + "Address:\n" + address + "\n\n"

                    +"Vehicle Information:\n\n"
                    + "Company Name:\n" + transName + "\n\n"
                    + "Vehicle Number:\n" + transNumber+ "\n\n"

                    +"Journey Schedule:\n\n"
                    + "Start Time:\n" + schTime + "\n\n"
                    + "Start Date:\n" + schDate+ "\n\n"
                    + "Starting Place:\n" + startLoc+ "\n\n"
                    + "Destination:\n" + destination+ "\n\n"
                    + "Road:\n" + schRoad;

            //copy driver contact number to clipboard**
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Bus Information", allInformation);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(TransportInfoDetailsActivity.this, "Bus Information Copied", Toast.LENGTH_LONG).show();
        }
    }
}
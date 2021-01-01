package com.example.bgctub_transport_tracker_trans_authority.ui.transport_info;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_trans_authority.R;
import com.example.bgctub_transport_tracker_trans_authority.TransportInfoDetailsActivity;
import com.example.bgctub_transport_tracker_trans_authority.adapter.CustomList;
import com.example.bgctub_transport_tracker_trans_authority.data_secure.DataSecure;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransportInformationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private TransportInformationViewModel mViewModel;
    private ListView busListView;
    private ArrayList<String> busList;
    private ArrayList<String> idList;
    private CustomList arrayAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private DatabaseReference vehicleListDatabaseRef;
    private DataSecure dataSecure;

    public static TransportInformationFragment newInstance() {
        return new TransportInformationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.transport_information_fragment, container, false);

        //for encoding and decoding
        dataSecure=new DataSecure();

        //database ref and others**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "driver_app" + "/" + "transport_info_location";
        vehicleListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);


        //for listView setup**
        busListView = root.findViewById(R.id.busListView);
        busList = new ArrayList<String>();
        arrayAdapter = new CustomList(busList, getContext());
        busListView.setAdapter(arrayAdapter);
        getUserInfoAndPass();

        //for userId list**
        idList = new ArrayList<String>();


        busListView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TransportInformationViewModel.class);
        // TODO: Use the ViewModel
    }


    //get All driver id and fetch info summary and pass id to details information view**
    public void getUserInfoAndPass() {
        vehicleListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    //get all id if available**
                    for (DataSnapshot userIdSnapShot : snapshot.getChildren()) {


                        //get all data from id**
                        userId = userIdSnapShot.getKey();

                        try {
                            String vehicle_name = dataSecure.dataDecode(snapshot.child(userId).child("transport_information").child("vehicle_name").getValue().toString());
                            String vehicle_number = dataSecure.dataDecode(snapshot.child(userId).child("transport_information").child("vehicle_number").getValue().toString());
                            String driver_name = dataSecure.dataDecode(snapshot.child(userId).child("transport_information").child("driver_name").getValue().toString());


                            //add data to list**
                            busList.add("Company Name: " + vehicle_name
                                    + "\n" + "Vehicle Number: " + vehicle_number
                                    + "\n" + "Driver Name: " + driver_name
                            );
                            //add id to list**
                            idList.add(userId);
                        } catch (Exception ex) {

                        }

                        //notify if data change**
                        arrayAdapter.notifyDataSetChanged();
                    }

                    //refresh the list to remove duplicate**
                    busList = refreshArrayList(busList);

                } catch (Exception exception) {
                    //if no data available give message**
                    Toast.makeText(getActivity(), "Sorry information not available", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //refresh arrayList for remove duplicate data**
    public <String> ArrayList<String> refreshArrayList(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<String>();
        for (String data : list) {
            if (!newList.contains(data)) {
                newList.add(data);
            }
        }

        return newList;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get user id with listView position because two concept in arrayList same position**
        //get user id and sent to details information activity**

        Intent intent = new Intent(getActivity(), TransportInfoDetailsActivity.class);
        intent.putExtra("userId", idList.get(position));
        startActivity(intent);
    }

}
package com.example.bgctub_transport_tracker_trans_authority.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_trans_authority.ProfileUpdateActivity;
import com.example.bgctub_transport_tracker_trans_authority.R;
import com.example.bgctub_transport_tracker_trans_authority.data_secure.DataSecure;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private TextView nameTextView,genderTextView,emailTextView,contactTextView,office_no_TextView,postTextView;
    private ImageButton editProfileButton;
    private FirebaseAuth mAuth;
    private DatabaseReference authorityInfoDatabaseRef;
    private FirebaseUser mUser;
    private DataSecure dataSecure;
    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.profile_fragment, container, false);
        nameTextView=root.findViewById(R.id.authority_name_textview);
        genderTextView=root.findViewById(R.id.authority_gender_textview);
        emailTextView=root.findViewById(R.id.authority_email_textview);
        contactTextView= root.findViewById(R.id.authority_contact_textview);
        office_no_TextView= root.findViewById(R.id.auth_office_num_textview);
        postTextView= root.findViewById(R.id.auth_job_post);
        editProfileButton = root.findViewById(R.id.profile_View_edit_imgBtn);

        editProfileButton.setOnClickListener(this);

        //for encoding and decoding
        dataSecure=new DataSecure();

        //firebase authentication and database reference**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        final String DATABASE_PATH = "transport_authority_app" + "/"+"authority_info"+"/"+ userId +"/"+ "profile";
        authorityInfoDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //add data to respected field
        addDataInField();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }


    //add data to respective textView if data available**
    public void addDataInField() {
        authorityInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = dataSecure.dataDecode(snapshot.child("name").getValue().toString());
                    String gender = dataSecure.dataDecode(snapshot.child("gender").getValue().toString());
                    String email = dataSecure.dataDecode(snapshot.child("email").getValue().toString());
                    String contact=dataSecure.dataDecode(snapshot.child("contact").getValue().toString());
                    String office_no=dataSecure.dataDecode(snapshot.child("office_no").getValue().toString());
                    String post=dataSecure.dataDecode(snapshot.child("post").getValue().toString());

                    nameTextView.setText(name);
                    genderTextView.setText(gender);
                    emailTextView.setText(email);
                    contactTextView.setText(contact);
                    office_no_TextView.setText(office_no);
                    postTextView.setText(post);

                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == editProfileButton) {
            startActivity(new Intent(getActivity(), ProfileUpdateActivity.class));
        }
    }
}
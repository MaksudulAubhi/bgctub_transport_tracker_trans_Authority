package com.example.bgctub_transport_tracker_trans_authority;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_trans_authority.data_secure.DataSecure;
import com.example.bgctub_transport_tracker_trans_authority.model.ProfileInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEdiText, contactEdiText, office_no_EditText, job_post_EditText;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton,maleRadioButton,femaleRadioButton,othersRadioButton;
    private Button profileUpdateButton;
    private TextView genderErrorTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference authorityInfoDatabaseRef;
    private ProgressDialog progressDialog;
    private String gender;
    private DataSecure dataSecure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nameEdiText=findViewById(R.id.authority_name_editText);
        contactEdiText=findViewById(R.id.auth_contact_editText);
        office_no_EditText=findViewById(R.id.auth_office_no_editText);
        job_post_EditText=findViewById(R.id.auth_post_editText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton=findViewById(R.id.radioMaleText);
        femaleRadioButton=findViewById(R.id.radioFemaleText);
        othersRadioButton=findViewById(R.id.radioOthersText);
        profileUpdateButton = findViewById(R.id.profile_update_button);
        genderErrorTextView = findViewById(R.id.genderErrorTextView);
        profileUpdateButton.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);

        //for encoding and decoding
        dataSecure=new DataSecure();

        //firebase authentication and database reference**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String userId = mUser.getUid();
        final String DATABASE_PATH = "transport_authority_app" + "/"+"authority_info"+"/"+ userId +"/"+ "profile";
        authorityInfoDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //check user logon or not, if not go to signIn activity
        if (mUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        //add data to editText if available
        addDataInField();


    }

    //add data to respective editText if data available**
    public void addDataInField(){
        authorityInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    String name = dataSecure.dataDecode(snapshot.child("name").getValue().toString());
                    String gender = dataSecure.dataDecode(snapshot.child("gender").getValue().toString());
                    String contact=dataSecure.dataDecode(snapshot.child("contact").getValue().toString());
                    String office_no=dataSecure.dataDecode(snapshot.child("office_no").getValue().toString());
                    String post=dataSecure.dataDecode(snapshot.child("post").getValue().toString());

                    nameEdiText.setText(name);
                    contactEdiText.setText(contact);
                    office_no_EditText.setText(office_no);
                    job_post_EditText.setText(post);

                    //for gender
                    if(gender.equals("Male")){
                        genderRadioGroup.check(R.id.radioMaleText);
                    }
                    if(gender.equals("Female")){
                        genderRadioGroup.check(R.id.radioFemaleText);
                    }
                    if(gender.equals("Others")){
                        genderRadioGroup.check(R.id.radioOthersText);
                    }

                }catch (Exception exception){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileUpdateActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void authorityInfoUpdate() {
        //other options
        String userId = mUser.getUid();
        String name = nameEdiText.getText().toString().trim();
        String email = mUser.getEmail();
        String contact=contactEdiText.getText().toString().trim();
        String office_no=office_no_EditText.getText().toString().trim();
        String job_post=job_post_EditText.getText().toString().trim();



        //input validation**
        if (TextUtils.isEmpty(name)) {
            nameEdiText.setError("Please enter your full name");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            contactEdiText.setError("Please enter your phone number");
            return;
        }
        if (TextUtils.isEmpty(office_no)) {
            office_no_EditText.setError("Please enter your office number");
            return;
        }
        if (TextUtils.isEmpty(job_post)) {
            job_post_EditText.setError("Please enter job post");
            return;
        }

        //For gender radio button validation and get text
        int genderID = genderRadioGroup.getCheckedRadioButtonId();
        if (genderID == -1) {
            genderErrorTextView.setVisibility(View.VISIBLE);
            return;
        } else {
            genderRadioButton = (RadioButton) findViewById(genderID);
            gender = genderRadioButton.getText().toString().trim();
        }

        progressDialog.setMessage("Updating Information");
        progressDialog.show();

        //data upload**
        ProfileInformation profileInformation=new ProfileInformation(userId,
                dataSecure.dataEncode(name),dataSecure.dataEncode(gender),
                dataSecure.dataEncode(email),dataSecure.dataEncode(contact),
                dataSecure.dataEncode(office_no),dataSecure.dataEncode(job_post));
        try {
            authorityInfoDatabaseRef.setValue(profileInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileUpdateActivity.this,"Profile Updated Successfully", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileUpdateActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    Toast.makeText(ProfileUpdateActivity.this,"Please try again later",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });


        }catch(Exception exception){

            Toast.makeText(this,"Please try again later",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onClick(View v) {
        if(v==profileUpdateButton){
            //update  information
            authorityInfoUpdate();
        }
    }
}
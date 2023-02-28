package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    TextView birthdayPicker,profileUsername, profileEmail ;
    ConstraintLayout profileImageConst ;
    CircleImageView profileImage ;

    MaterialToolbar profileToolbar ;

    AutoCompleteTextView districtName, upazilaName;
    EditText profileName, phoneNumber ;

    Uri imageUriResultCrop;

    private HashMap<String,String> userDetails ;

    DatabaseReference updateDatabase = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference mDatabase;

    private String name, dateOfBirth, phone, email ;

    String uid ;

    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        profileImageConst = findViewById(R.id.profileImage_const_layout);
        profileImage = findViewById(R.id.circleImageView);
        birthdayPicker = findViewById(R.id.birthday_datepicker);
        districtName = findViewById(R.id.district_auto);
        upazilaName = findViewById(R.id.upazila_auto);
        profileName = findViewById(R.id.profile_name);
        phoneNumber = findViewById(R.id.phone_number);
        profileEmail = findViewById(R.id.profile_email);
        profileUsername = findViewById(R.id.profile_uname);

        profileToolbar = findViewById(R.id.profile_toolbar);




        mDatabase = FirebaseDatabase.getInstance().getReference();





        
//        -------------not available to edit-----------------
        profileUsername.setOnClickListener(v -> {
            Toast.makeText(this, "Not editable", Toast.LENGTH_SHORT).show();   
        });
        profileEmail.setOnClickListener(v->{
            Toast.makeText(this, "Not editable", Toast.LENGTH_SHORT).show();
        });
        //-------------- read name and user name and all details from database ------------

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        if(user != null ) {

            mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String districtname= String.valueOf(snapshot.child("district").getValue());
                    String upazilaname=String.valueOf(snapshot.child("upazila").getValue());

                    districtNameListAdapter(districtname,upazilaname);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            userDetails = new HashMap<String,String>();
            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DataSnapshot data : task.getResult().getChildren()){
                            userDetails.put(data.getKey(),(String) data.getValue());
                        }

                        profileName.setText(userDetails.get("name"));
                        profileUsername.setText(userDetails.get("username"));
                        birthdayPicker.setText(userDetails.get("DOB"));
                        phoneNumber.setText(userDetails.get("phone"));
                        profileEmail.setText(userDetails.get("email"));
                        Picasso.get().load(userDetails.get("propic")).into(profileImage);

                    }
                }


            });

        }


        profileImageConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"),CODE_IMG_GALLERY);
            }
        });


//        ---------------- DOB datepicker ------------
        birthdayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        MyProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                birthdayPicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        profileToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit_profile_done) {
                    updateUserData(uid);
                    Intent i = new Intent(MyProfileActivity.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                return false;
            }
        });


        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));

    }

    private void districtNameListAdapter(String districtname, String upazilaname) {

//        ------------ district list -------------
        ArrayList<String> districtList = new ArrayList<>();
        mDatabase.child("location").child("district").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for(DataSnapshot list: task.getResult().getChildren()){
                        districtList.add((String) list.getValue());
                    }
                }

            }
        });


        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item,districtList);
        districtName.setText(districtname);
        districtName.setAdapter(districtAdapter);
        districtName.setOnItemClickListener((parent, view, position, id) -> {
            updateDatabase.child(uid).child("district").setValue(parent.getItemAtPosition(position).toString());
        });



//        --------------------- upazilla list -------------
        ArrayList<String> upazilaList = new ArrayList<>();
        mDatabase.child("location").child(districtname).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for(DataSnapshot list: task.getResult().getChildren()){
                        upazilaList.add(String.valueOf(list.getValue()));
                    }
                }
            }
        });

        ArrayAdapter<String> upazilaAdapter = new ArrayAdapter<>(MyProfileActivity.this, android.R.layout.simple_spinner_dropdown_item,upazilaList);
        upazilaName.setText(upazilaname);
        upazilaName.setAdapter(upazilaAdapter);
        upazilaName.setOnItemClickListener((parent, view, position, id) -> {
            updateDatabase.child(uid).child("upazila").setValue(parent.getItemAtPosition(position).toString());
        });



    }



    private void updateUserData(String uid) {


        //        -------------------------- track changed details by user -----------------
        name = profileName.getText().toString();
        dateOfBirth = birthdayPicker.getText().toString();
        phone = phoneNumber.getText().toString();
        email = profileEmail.getText().toString();

        updateDatabase.child(uid).child("name").setValue(name);
        updateDatabase.child(uid).child("DOB").setValue(dateOfBirth);
        updateDatabase.child(uid).child("phone").setValue(phone);




    }

    private void downloadImageUrl(String uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/"+uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                updateDatabase.child(uid).child("propic").setValue(uri.toString());

                Toast.makeText(MyProfileActivity.this, "Data updated successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyProfileActivity.this, "Failed to download image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyProfileActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }


//    -------------- ON ACTIVITY RESULT --------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            if(imageUri!=null){
                startCrop(imageUri);
            }
        }
        else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            imageUriResultCrop = UCrop.getOutput(data);
            if(imageUriResultCrop!=null){
                profileImage.setImageURI(imageUriResultCrop);

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading ...");
                progressDialog.show();


                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageUploader = storage.getReference().child("images/"+uid+".jpg");



                imageUploader.putFile(imageUriResultCrop)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.hide();
                                downloadImageUrl(uid);
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyProfileActivity.this, "Image upload Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        }

    }

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));

        uCrop.withAspectRatio(1,1);

        uCrop.withMaxResultSize(450,450);

        uCrop.withOptions(getCropOption());

        uCrop.start(MyProfileActivity.this);
    }

    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionQuality(70);

        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        options.setHideBottomControls(false);

        options.setFreeStyleCropEnabled(true);

        options.setStatusBarColor(getResources().getColor(R.color.statusbar));
        options.setToolbarColor(getResources().getColor(R.color.splashstatus));
        options.setToolbarTitle("Crop image");
        return options;
    }


}
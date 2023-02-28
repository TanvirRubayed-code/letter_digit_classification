package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Signup_activity extends AppCompatActivity {

    TextInputEditText signUpPassword, signUpconfirmPassword, signUpUsername, signupEmail, signUpName;
    CheckBox SignupCheckbox ;
    TextView SignInText ;
    Button SignupButton;
    ProgressBar SignupProgressBar ;
    LinearLayout CreateAccount ;

    private String name, username, email, password, confirmpassword, profilePicUrl;
    private Boolean signUpFlag=false;

    private FirebaseAuth auth;


    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private BroadcastReceiver broadcastReceiver;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpName = findViewById(R.id.name);
        signUpUsername = findViewById(R.id.signup_username);
        signupEmail = findViewById(R.id.signup_email);
        signUpPassword = findViewById(R.id.signup_password);
        signUpconfirmPassword = findViewById(R.id.signup_confirm_password);
        SignupCheckbox = findViewById(R.id.sign_up_checkbox);
        SignInText = findViewById(R.id.sign_in_text);
        SignupButton = findViewById(R.id.sign_up_button);
        SignupProgressBar = findViewById(R.id.signup_progress);
        CreateAccount = findViewById(R.id.create_account_layout);


        auth = FirebaseAuth.getInstance();


        downloadDefaultProfileURL();





//         -----------------Internet connection check and show alert dialog ----------

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));




        signUpUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }



            @Override
            public void afterTextChanged(Editable editable) {
                DatabaseReference mData = FirebaseDatabase.getInstance().getReference("users");
                String typename = String.valueOf(editable);
                mData.orderByChild("username").equalTo(typename).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            signUpUsername.setError("Username is not available");
                            signUpUsername.requestFocus();
                            signUpFlag=true;
                        }
                        else{
                            signUpFlag=false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!signUpFlag){
                    validateData();
                }else {
                    Toast.makeText(Signup_activity.this, "Correct all fields.", Toast.LENGTH_SHORT).show();
                }
            }

        });



        SignupCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!SignupCheckbox.isChecked()){
                    signUpPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    signUpconfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else {
                    signUpPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    signUpconfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });




        SignInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),login_activity.class);
                startActivity(intent);
                finish();
            }
        });



        //Change status bar coclor
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }
    }


    //    -----------this method is called for unregisteredReceiver of internet connection check --------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            openMain();
        }
    }

    private void openMain() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();

    }

    private void validateData() {
        name = signUpName.getText().toString();
        username = signUpUsername.getText().toString();
        email = signupEmail.getText().toString();
        password = signUpPassword.getText().toString();
        confirmpassword = signUpconfirmPassword.getText().toString();

        if(name.isEmpty()){
            signUpName.setError("Required");
            signUpName.requestFocus();
        }else if(username.isEmpty()){
            signUpUsername.setError("Required");
            signUpUsername.requestFocus();
        }else if(email.isEmpty()){
            signupEmail.setError("Required");
            signupEmail.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signupEmail.setError("Invalid email address");
            signupEmail.requestFocus();
        }
        else if(password.isEmpty()){
            signUpPassword.setError("Required");
            signUpPassword.requestFocus();
        }else if(confirmpassword.isEmpty()){
            signUpconfirmPassword.setError("Required");
            signUpconfirmPassword.requestFocus();
        }
        else if(!password.equals(confirmpassword)){
            signUpconfirmPassword.setError("Password didn't matched");
            signUpconfirmPassword.requestFocus();
        }
        else {
            createUser();
        }
    }

    private void downloadDefaultProfileURL() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/profile_pic.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                updateDatabase.child(uid).child("propic").setValue(uri.toString());
                profilePicUrl = uri.toString();
                Toast.makeText(Signup_activity.this, "download successfull", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(Signup_activity.this, "download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser() {

        SignupProgressBar.setVisibility(View.VISIBLE);
        CreateAccount.setVisibility(View.INVISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        SignupProgressBar.setVisibility(View.INVISIBLE);
                        CreateAccount.setVisibility(View.VISIBLE);

                        if(task.isSuccessful()){

                            FirebaseUser user = auth.getCurrentUser();
                            uploadUserData(String.valueOf(user.getUid()));
                        }
                        else {
                            Toast.makeText(Signup_activity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup_activity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    void uploadUserData(String userID) {


        HashMap<String, String > user = new HashMap<>();
        user.put("name",name);
        user.put("username",username);
        user.put("email",email);
        user.put("DOB","");
        user.put("phone","");
        user.put("district","");
        user.put("upazila","");
        user.put("propic",profilePicUrl);

        database.child("users").child(userID).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup_activity.this, "User successfully created", Toast.LENGTH_SHORT).show();
                            openMain();

                        }
                        else {
                            Toast.makeText(Signup_activity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup_activity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_activity extends AppCompatActivity {

    CheckBox LoginCheckbox, LoginSavePassword ;
    TextInputEditText PasswordLogin, LoginUsername;
    TextView SignUpText;
    Button LoginButton;
    ProgressBar ProgressBar ;
    LinearLayout MyaccountLayout ;


    private FirebaseAuth mAuth;
    FirebaseFirestore firestore ;
    private FirebaseAuth authcheck = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences ;



    private BroadcastReceiver broadcastReceiver;


    String email , password ;

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences = this.getSharedPreferences("pass", Context.MODE_PRIVATE);

        boolean checkpassbox = sharedPreferences.getBoolean("savepass", false);
        if(authcheck.getCurrentUser()!=null && checkpassbox){
            openMain();
        }
    }




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//         LoginCheckbox = findViewById(R.id.login_checkbox);
         SignUpText = findViewById(R.id.sign_up_text);
         LoginButton = findViewById(R.id.login_button);
         PasswordLogin = findViewById(R.id.password_login);
         LoginUsername = findViewById(R.id.login_username);
         ProgressBar = findViewById(R.id.loginprogress);
         MyaccountLayout = findViewById(R.id.my_account_layout);
         LoginSavePassword = findViewById(R.id.save_pass_checkbox);


         firestore = FirebaseFirestore.getInstance();
         mAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences("pass", Context.MODE_PRIVATE);


//         -----------------Internet connection check and show alert dialog ----------

         broadcastReceiver = new NetworkBroadcast();
         registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



         LoginButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 validitycheck();
             }
         });




//         LoginCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//             @Override
//             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                 if(!LoginCheckbox.isChecked()){
//                     PasswordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                 }
//                 else {
//                     PasswordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                 }
//             }
//         });

        LoginSavePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean loginsave = true;

                if(isChecked){
                     SharedPreferences.Editor editor = sharedPreferences.edit();
                     editor.putBoolean("savepass", loginsave);
                     editor.apply();
                }
            }
        });

//


        SignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Signup_activity.class);
                startActivity(intent);
            }
        });
//
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

    private void validitycheck() {
       
        email = LoginUsername.getText().toString();
        password = PasswordLogin.getText().toString();
        
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please, provide all fields", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            LoginUsername.setError("Invalid email address");
            LoginUsername.requestFocus();
        }else {
            loginUser();
        }
    }

    private void loginUser() {

        ProgressBar.setVisibility(View.VISIBLE);
        MyaccountLayout.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        ProgressBar.setVisibility(View.INVISIBLE);
                        MyaccountLayout.setVisibility(View.VISIBLE);

                        if(task.isSuccessful()){

                            openMain();

                        }
                        else {
                            Toast.makeText(login_activity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_activity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openMain() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
package com.example.techforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
CountryCodePicker ccp;
EditText phoneNo,password;
CheckBox rememberMe;
Button ForgotPassword,login,createAccount;
ImageView backArrow;
RelativeLayout login_progressBar;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ccp = findViewById(R.id.login_country_code_picker);
        phoneNo = findViewById(R.id.login_phone_number_editText);
        password = findViewById(R.id.login_password_editText);
        rememberMe = findViewById(R.id.remember_me);
        ForgotPassword = findViewById(R.id.forgot_password);
        login = findViewById(R.id.login_button);
        createAccount = findViewById(R.id.create_account);
        backArrow = findViewById(R.id.login_back_button);
        login_progressBar = findViewById(R.id.login_progress_bar);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(this,Dashboard.class);
            startActivity(intent);
            finish();
        }

//      1.  if user click on login
//        collect the data and send to login function
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

//      2.  if user click on create account take it to signupactivity from signupfunction
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCreateAccount();
            }
        });


//   3.if user clicks on forgot password take it to forgot password activity from recoverPassword
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                take to forgot password
            }
        });

//        if user clicks on back button finish the current activity.
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }






    private void userCreateAccount() {
        Intent intent= new Intent(this,signup.class);
        startActivity(intent);
        finish();
    }

    private void userLogin() {

        String phone=phoneNo.getText().toString();
        if(phone.isEmpty()){
            Toast.makeText(MainActivity.this,"Please a valid phone no.",Toast.LENGTH_SHORT).show();
            return ;}
        if(phone.charAt(0)==0)
        { Toast.makeText(MainActivity.this, "Do not start with 0", Toast.LENGTH_SHORT).show();
            return;}
        else if(phone.length() != 10)
        {
            Toast.makeText(MainActivity.this, "Please enter a valid Phone No.", Toast.LENGTH_SHORT).show();
            return;}

        String countryCode=ccp.getSelectedCountryCode();
        boolean remember=rememberMe.isChecked();

        SharedPreferences sharedPreferences=getSharedPreferences("com.example.techforum",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("remember",remember);
        editor.apply();




        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference("userDetails").child("+"+countryCode+phone);
//        Toast.makeText(this, countryCode+phone, Toast.LENGTH_SHORT).show();
        String pass=password.getText().toString();
        if(pass.isEmpty()){Toast.makeText(MainActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();return;}
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details=snapshot.getValue(userDetails.class);
                if(details==null){
                    Toast.makeText(MainActivity.this,"You do not have an account",Toast.LENGTH_SHORT).show();
                }else if(!details.getPassword().equals(pass)){
                    Toast.makeText(MainActivity.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences username=getSharedPreferences("username",MODE_PRIVATE);
                    SharedPreferences.Editor edit=username.edit();
                    edit.putString("name",details.getName());
                    edit.putString("profilePic",details.getProfilePic());
                    edit.apply();
                    Intent goToOtpActivity=new Intent(MainActivity.this,OtpActivity.class);
                    goToOtpActivity.putExtra("completePhoneNo","+"+countryCode+phone);
                    goToOtpActivity.putExtra("userName","");
                    goToOtpActivity.putExtra("userEmail","");
                    goToOtpActivity.putExtra("userPassword","");
                    goToOtpActivity.putExtra("activityName","login");
                    startActivity(goToOtpActivity);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Cannot reach server "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }
}
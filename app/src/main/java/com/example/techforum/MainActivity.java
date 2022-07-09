package com.example.techforum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
//        String userName=name.getText().toString();
//        String userEmail=email.getText().toString();
//        String userPassword=password.getText().toString();
        if(phone.charAt(0)==0)
        { Toast.makeText(MainActivity.this, "Do not start with 0", Toast.LENGTH_SHORT).show();
            return;}
        else if(phone.length() != 10)
        {
            Toast.makeText(MainActivity.this, "Please enter a valid Phone No.", Toast.LENGTH_SHORT).show();
            return;}

        String countryCode=ccp.getSelectedCountryCode();

        //put them together
        String completePhoneNo="+"+countryCode+phone;
        //send to next activity
        Intent goToOtpActivity=new Intent(MainActivity.this,OtpActivity.class);
        goToOtpActivity.putExtra("completePhoneNo",completePhoneNo);
        goToOtpActivity.putExtra("userName","");
        goToOtpActivity.putExtra("userEmail","");
        goToOtpActivity.putExtra("userPassword","");
        goToOtpActivity.putExtra("activityName","login");
        startActivity(goToOtpActivity);
        finish();
    }
}
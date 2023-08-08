package com.example.techforum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class signup extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText phoneNo,password,name,email;
    Button login,createAccount;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ccp = findViewById(R.id.signup_country_code_picker);
        phoneNo = findViewById(R.id.signup_phone_edittext);
        password = findViewById(R.id.signup_password_edittext);
        login = findViewById(R.id.signup_login_button);
        createAccount = findViewById(R.id.signup_createAccount);
        backArrow = findViewById(R.id.signup_back_button);
        name=findViewById(R.id.signup_name_edittext);
        email=findViewById(R.id.signup_email_edittext);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                send to next activity(otp verify).
                sendDetails();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        goBack();
    }

    private void sendDetails() {

        String phone=phoneNo.getText().toString();
        String userName=name.getText().toString();
        String userEmail=email.getText().toString();
        String userPassword=password.getText().toString();
        if(userName.length()<6||userEmail.length()<6||userPassword.length()<6)
        { Toast.makeText(signup.this, "Username Email and Password should be minimimum 6 characters", Toast.LENGTH_SHORT).show();
        return;}
        else if(phone.length() != 10)
        {
            Toast.makeText(signup.this, "Please enter a valid Phone No.", Toast.LENGTH_SHORT).show();
            return;}

        String countryCode=ccp.getSelectedCountryCode();
        String url="https://firebasestorage.googleapis.com/v0/b/tech-forum-a6098.appspot.com/o/images%2Fdownload.png?alt=media&token=fd672c80-a72b-435e-8cc9-982b6994aca2";
        SharedPreferences username=getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor edit=username.edit();

        SharedPreferences remember=getSharedPreferences("com.example.techforum",MODE_PRIVATE);
        SharedPreferences.Editor editor=remember.edit();
        editor.putBoolean("remember",true);

        edit.putString("name",userName);
        edit.putString("profilePic",url);
        edit.apply();

        //put them together
        String completePhoneNo="+"+countryCode+phone;
        //send to next activity
        Intent goToOtpActivity=new Intent(signup.this,OtpActivity.class);
        goToOtpActivity.putExtra("completePhoneNo",completePhoneNo);
        goToOtpActivity.putExtra("userName",userName);
        goToOtpActivity.putExtra("userEmail",userEmail);
        goToOtpActivity.putExtra("userPassword",userPassword);
        goToOtpActivity.putExtra("activityName","signup");
        startActivity(goToOtpActivity);
        finish();
    }

    public void goBack(){
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(signup.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        login.setOnClickListener(listener);
        backArrow.setOnClickListener(listener);
    }

}
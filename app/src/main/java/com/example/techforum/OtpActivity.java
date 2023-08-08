package com.example.techforum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
String phoneNo,userName,userEmail,userPassword,OTPid,fromActivity;
Button verifyOTP;
PinView OTP;
TextView DescriptionOfOTP;
ImageView back;
FirebaseAuth mAuth;
FirebaseDatabase database;
DatabaseReference myref;
ProgressBar Otp_progress_bar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        phoneNo=getIntent().getStringExtra("completePhoneNo");
        userName=getIntent().getStringExtra("userName");
        userEmail=getIntent().getStringExtra("userEmail");
        userPassword=getIntent().getStringExtra("userPassword");
        fromActivity=getIntent().getStringExtra("fromActivity");
        verifyOTP=findViewById(R.id.verifyOTP);
        OTP=findViewById(R.id.pin_view);
        DescriptionOfOTP=findViewById(R.id.otp_description_text);
        back=findViewById(R.id.OTP_back);
        DescriptionOfOTP.setText("Enter One Time Password sent to "+phoneNo);
        Otp_progress_bar=findViewById(R.id.Otp_progress_bar);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myref=database.getReference("userDetails").child(phoneNo);
        verify();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OtpActivity.this,signup.class);
                startActivity(intent);
                finish();
            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(OTPid,OTP.getText().toString());
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        });
    }

    private void verify() {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNo)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    OTPid=s;
                                }

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    String s=phoneAuthCredential.getSmsCode();
                                    OTP.setText(s);
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Toast.makeText(OtpActivity.this, "Login Failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Otp_progress_bar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=getIntent();
                            String activity=intent.getStringExtra("activityName");
                            Toast.makeText(OtpActivity.this, "Sucessfully loged in", Toast.LENGTH_SHORT).show();
                            if(activity.equals("signup"))
                                    sendDataToFirebase();
                            else if(activity.equals("login"))
                                readDataFromFirebase();
                        } else {

                            Toast.makeText(OtpActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(OtpActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void readDataFromFirebase() {
        userDetails userDetails=new userDetails();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getValue(userDetails.getClass());
                Intent intent=new Intent(OtpActivity.this,Dashboard.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OtpActivity.this, "We are unable to fetch data currently", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendDataToFirebase() {
        String profilePic="https://firebasestorage.googleapis.com/v0/b/tech-forum-a6098.appspot.com/o/images%2Fdownload.png?alt=media&token=fd672c80-a72b-435e-8cc9-982b6994aca2";
        userDetails userDetails=new userDetails(phoneNo,userName,userEmail,userPassword,profilePic);
        myref.setValue(userDetails);
        Otp_progress_bar.setVisibility(View.GONE);
        Intent intent=new Intent(OtpActivity.this,Dashboard.class);
        startActivity(intent);
        finish();
    }
}
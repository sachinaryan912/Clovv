package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpPage extends AppCompatActivity {

    private String verificationID;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private EditText enterOTPEditText;
    private TextView resend,time,title_details;
    public int counter;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    Dialog mDialog;
    DatabaseReference reference;
    String phnForUse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);


        mAuth = FirebaseAuth.getInstance();

        enterOTPEditText = findViewById(R.id.otpData);
        resend = findViewById(R.id.resend_otp);
        time = findViewById(R.id.time);
        title_details = findViewById(R.id.noOtpInformation);



        mDialog = new Dialog(OtpPage.this);
        mDialog.setContentView(R.layout.verify_dialog);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        final String phoneNumber = getIntent().getStringExtra("phonenumber");
        phnForUse = getIntent().getStringExtra("phonenumber");

        sendVerificationCode(phoneNumber);
        title_details.setText("We have sent code on "+phoneNumber);

        new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.valueOf(counter));
                counter++;
            }
            @Override
            public void onFinish() {
                time.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);
            }
        }.start();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //resend code
                resendVerficationCode(phoneNumber, forceResendingToken);

            }
        });

        //submit button onclick listner
        Button submitOTPBtn = findViewById(R.id.verify_btn);
        submitOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String code = enterOTPEditText.getText().toString().trim();

                if(code.isEmpty() || code.length() < 6)
                {
                    enterOTPEditText.setError("Enter OTP Code");
                    enterOTPEditText.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });


    }

    private void resendVerficationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {

        Toast.makeText(getApplicationContext(), "Resending code", Toast.LENGTH_SHORT).show();
        resend.setVisibility(View.GONE);
        time.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                30,
                TimeUnit.SECONDS,
                this,
                mCallBack,
                token

        );

    }

    private void verifyCode(String code)
    {

        mDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            mDialog.dismiss();
                            currentUser = mAuth.getCurrentUser();
                            reference = FirebaseDatabase.getInstance().getReference().child("customers").child(currentUser.getUid());

                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Intent intent = new Intent(OtpPage.this,Dashboard.class);
                                        Toast.makeText(OtpPage.this, "login success", Toast.LENGTH_LONG).show();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(OtpPage.this,UsernameActivity.class);
                                        intent.putExtra("phonenumbers",phnForUse);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        }else{
                            mDialog.dismiss();
                            Toast.makeText(OtpPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                30,
                TimeUnit.SECONDS,
                this,
                mCallBack

        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if(code != null)
            {
                verifyCode(code);
            }

            if(code == null)
            {
                signInWithCredentials(phoneAuthCredential);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpPage.this, "Too many attempts.Try after 1 hour", Toast.LENGTH_LONG).show();
        }
    };

}
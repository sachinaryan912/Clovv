package com.company.clovv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //call PhoneOTPPage with proceed button
        final EditText loginViaPhoneEditText  = findViewById(R.id.phone_number);

        Button proceedBtn = (Button) findViewById(R.id.generate_otp_btn);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = loginViaPhoneEditText.getText().toString().trim();

                if(number.isEmpty() || number.length() < 10 || number.length() > 10)
                {
                    loginViaPhoneEditText.setError("Enter the valid number");
                    loginViaPhoneEditText.requestFocus();
                    return;
                }

                String numbers = "+" + "91" + number;
                Intent intent = new Intent(LoginActivity.this, OtpPage.class);
                intent.putExtra("phonenumber",numbers);

                startActivity(intent);
                finish();
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {

                Intent intent = new Intent(this,Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

        }
    }
}
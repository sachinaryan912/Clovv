package com.company.clovv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.clovv.modal.CustomerDetailsModal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsernameActivity extends AppCompatActivity {

    ImageView backBtn;
    TextInputEditText nameVal;
    Button btnContinue;
    String id,number;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    DatabaseReference ref,reference;
    private CustomerDetailsModal customerDetailsModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        backBtn = findViewById(R.id.back_btn23);
        nameVal = findViewById(R.id.nameTxt);
        btnContinue = findViewById(R.id.cont_btn);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("customers").child(currentUser.getUid());
        reference = FirebaseDatabase.getInstance().getReference().child("customers");


        btnContinue.setEnabled(false);
        number = getIntent().getStringExtra("phonenumbers");




        nameVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count>3){
                    btnContinue.setEnabled(true);
                }else {
                    btnContinue.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameVal.getText().toString();
                customerDetailsModal = new CustomerDetailsModal(name,number, currentUser.getUid(),"none",true);
                reference.child(currentUser.getUid()).setValue(customerDetailsModal);
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(UsernameActivity.this,Dashboard.class));
                finish();

            }
        });






        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsernameActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UsernameActivity.this,LoginActivity.class));
        finish();
    }
}
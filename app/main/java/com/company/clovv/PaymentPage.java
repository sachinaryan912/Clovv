package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PaymentPage extends AppCompatActivity {

    TextView txtTime,txtPrice;
    Button btnPay;
    String orderId,price;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        txtPrice = findViewById(R.id.orderPrice);
        txtTime = findViewById(R.id.timerTxt);
        btnPay = findViewById(R.id.btnPay);
        orderId = getIntent().getStringExtra("orderId");
        price = getIntent().getStringExtra("orderPrice");

        txtPrice.setText(getApplicationContext().getResources().getString(R.string.rs)+" "+price);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);



        progressDialog = new ProgressDialog(PaymentPage.this);
        progressDialog.setMessage("Payment processing...");
        progressDialog.setCancelable(false);


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                reference.child("paymentCompleted").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();

                            Intent intent = new Intent(PaymentPage.this,OrderPlacedActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();


                        }
                    }
                });





            }
        });



    }


//    public void countDownTimer(){
//
//        countDownTimer1 = new CountDownTimer(30000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//                txtTime.setText(""+String.format("%d : %d",
//                        TimeUnit.MILLISECONDS.toMinutes( counter*1000),
//                        TimeUnit.MILLISECONDS.toSeconds(counter*1000) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter*1000))));
//
//                counter--;
//
//            }
//            @Override
//            public void onFinish() {
//
//                 if (countDownTimer1!=null){
//                        countDownTimer1.cancel();
//                        countDownTimer1=null;
//                    }
//
//
//
//                Intent intent = new Intent(PaymentPage.this,Dashboard.class);
//                startActivity(intent);
//                finish();
//
//
//
//                }
//
//        }.start();
//
//
//
//
//
//    }


}
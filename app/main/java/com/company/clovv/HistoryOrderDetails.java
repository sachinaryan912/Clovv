package com.company.clovv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryOrderDetails extends AppCompatActivity {

    CardView cardOrderDetails;
    TextView orderIdTxt;
    LottieAnimationView imgCollapse;
    Animation startAnimation,stopAnimation;
    boolean isIconRotated = false;
    LinearLayout orderList;
    DatabaseReference reference;
    ImageView c1,c2,c3,c4;
    View v1,v2,v3;
    TextView txt1,txt2,txt3,txt4,orderStatusTxt;
    ImageView img_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order_details);

        orderIdTxt = findViewById(R.id.orderIdtxtData);
        imgCollapse = findViewById(R.id.collapseIcon);
        cardOrderDetails = findViewById(R.id.cardOrderDetails);
        orderList = findViewById(R.id.orderdetailsList);
        orderStatusTxt = findViewById(R.id.orderStatustxtData);
        img_status = findViewById(R.id.imge_status);

        c1 = findViewById(R.id.circle1);
        c2 = findViewById(R.id.circle2);
        c3 = findViewById(R.id.circle3);
        c4 = findViewById(R.id.circle4);

        v1 = findViewById(R.id.viewLine1);
        v2 = findViewById(R.id.viewLine2);
        v3 = findViewById(R.id.viewLine3);

        txt1 = findViewById(R.id.step1Txt);
        txt2 = findViewById(R.id.step2Txt);
        txt3 = findViewById(R.id.step3Txt);
        txt4 = findViewById(R.id.step4Txt);

        String orderId = getIntent().getStringExtra("orderId");

        orderIdTxt.setText("OrderId#"+orderId);

        startAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_out);
        stopAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(orderId);

        txt1.setText("Ordered");
        c1.setBackgroundResource(R.drawable.tick_track);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //do something
                    boolean isAccepted,isCancelled,isOutForDelivery,isOrderExpire,isOrderSuccess;
                    isAccepted = Boolean.parseBoolean(snapshot.child("accepted").getValue().toString());
                    isCancelled = Boolean.parseBoolean(snapshot.child("canceled").getValue().toString());
                    isOutForDelivery = Boolean.parseBoolean(snapshot.child("outForDelivery").getValue().toString());
                    isOrderExpire = Boolean.parseBoolean(snapshot.child("orderExpired").getValue().toString());
                    isOrderSuccess = Boolean.parseBoolean(snapshot.child("orderSuccess").getValue().toString());

                    if (isAccepted && !isCancelled && !isOutForDelivery
                            && !isOrderExpire && !isOrderSuccess){

                        orderStatusTxt.setText("Order Accepted");
                        img_status.setBackgroundResource(R.drawable.tick_varient2);

                        txt2.setText("Accepted");
                        c2.setBackgroundResource(R.drawable.tick_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        txt3.setText("Out for delivery");
                        c3.setBackgroundResource(R.drawable.undefined_track);
                        v2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.greyline));

                        txt4.setText("Order Delivered");
                        c4.setBackgroundResource(R.drawable.undefined_track);
                        v3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.greyline));

                    }

                    if (isAccepted && isCancelled && !isOutForDelivery
                            && !isOrderExpire && !isOrderSuccess){

                        txt2.setText("Accepted");
                        c2.setBackgroundResource(R.drawable.tick_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));


                        orderStatusTxt.setText("Order Cancelled");
                        img_status.setBackgroundResource(R.drawable.cancel_track);


                        txt3.setText("Cancelled");
                        txt3.setTextColor(getApplicationContext().getResources().getColor(R.color.redColor));
                        c3.setBackgroundResource(R.drawable.cancel_track);
                        v2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        v3.setVisibility(View.GONE);
                        c4.setVisibility(View.GONE);
                        txt4.setVisibility(View.GONE);
                    }

                    if (!isAccepted && isCancelled && !isOutForDelivery
                            && !isOrderExpire && !isOrderSuccess){

                        orderStatusTxt.setText("Order Cancelled");
                        img_status.setBackgroundResource(R.drawable.cancel_track);


                        txt2.setText("Cancelled");
                        txt2.setTextColor(getApplicationContext().getResources().getColor(R.color.redColor));
                        c2.setBackgroundResource(R.drawable.cancel_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));


                        txt3.setVisibility(View.GONE);
                        c3.setVisibility(View.GONE);
                        v2.setVisibility(View.GONE);
                        v3.setVisibility(View.GONE);
                        c4.setVisibility(View.GONE);
                        txt4.setVisibility(View.GONE);
                    }

                    if (isAccepted && !isCancelled && isOutForDelivery
                            && !isOrderExpire && !isOrderSuccess){

                        txt2.setText("Accepted");
                        c2.setBackgroundResource(R.drawable.tick_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        txt3.setText("Out for delivery");
                        c3.setBackgroundResource(R.drawable.tick_track);
                        v2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        orderStatusTxt.setText("Out for delivery");
                        img_status.setBackgroundResource(R.drawable.deliveryman);

                        txt4.setText("Order Delivered");
                        c4.setBackgroundResource(R.drawable.undefined_track);
                        v3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.greyline));

                    }

                    if (isAccepted && !isCancelled && isOutForDelivery
                            && !isOrderExpire && isOrderSuccess){

                        txt2.setText("Accepted");
                        c2.setBackgroundResource(R.drawable.tick_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        txt3.setText("Out for delivery");
                        c3.setBackgroundResource(R.drawable.tick_track);
                        v2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));


                        txt4.setText("Order Delivered");
                        c4.setBackgroundResource(R.drawable.tick_track);
                        v3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        orderStatusTxt.setText("Order Delivered");
                        img_status.setBackgroundResource(R.drawable.tick_varient2);
//
                    }

                    if (!isAccepted && isCancelled && !isOutForDelivery
                            && isOrderExpire && !isOrderSuccess){

                        txt2.setText("Order Expired");
                        txt2.setTextColor(getApplicationContext().getResources().getColor(R.color.greyline));
                        c2.setBackgroundResource(R.drawable.expire_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        orderStatusTxt.setText("Order Expire");
                        img_status.setBackgroundResource(R.drawable.expired);

                        v2.setVisibility(View.GONE);
                        v3.setVisibility(View.GONE);
                        c3.setVisibility(View.GONE);
                        c4.setVisibility(View.GONE);
                        txt3.setVisibility(View.GONE);
                        txt4.setVisibility(View.GONE);



                    }

                    if (isAccepted && isCancelled && !isOutForDelivery
                            && isOrderExpire && !isOrderSuccess){

                        txt2.setText("Accepted");
                        c2.setBackgroundResource(R.drawable.tick_track);
                        v1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        txt3.setText("Payment failed");
                        txt3.setTextColor(getApplicationContext().getResources().getColor(R.color.redColor));
                        c3.setBackgroundResource(R.drawable.payment_failed);
                        v2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.purple_700));

                        orderStatusTxt.setText("Payment failed");
                        img_status.setBackgroundResource(R.drawable.error_pay);


                        v3.setVisibility(View.GONE);
                        c4.setVisibility(View.GONE);
                        txt4.setVisibility(View.GONE);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cardOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isIconRotated){
                    imgCollapse.playAnimation();
                    imgCollapse.setMinAndMaxProgress(0.5f,1.0f);
                    orderList.startAnimation(stopAnimation);
                    orderList.setVisibility(View.VISIBLE);


                    isIconRotated=true;
                }else {
                    imgCollapse.playAnimation();
                    imgCollapse.setMinAndMaxProgress(0.0f,0.5f);

                    orderList.startAnimation(startAnimation);
                    orderList.setVisibility(View.GONE);

                    isIconRotated=false;
                }

            }
        });


    }
}
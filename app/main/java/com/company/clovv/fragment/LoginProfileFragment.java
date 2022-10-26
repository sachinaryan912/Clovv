package com.company.clovv.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.company.clovv.AddressBookActivity;
import com.company.clovv.Dashboard;
import com.company.clovv.FavActivity;
import com.company.clovv.LoginActivity;
import com.company.clovv.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginProfileFragment extends Fragment {

    Button lgn;
    LinearLayout lMain,lUseless;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference ref;
    TextView txtNameVal,txtPhoneVal;
    CardView btnLogOut;
    Dialog logoutDialog;
    CardView favCard,addressCard;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_profile, container, false);

        lMain = v.findViewById(R.id.withLogin);
        lUseless = v.findViewById(R.id.withoutLogin);

        mAuth = FirebaseAuth.getInstance();

        txtNameVal = v.findViewById(R.id.nameCust);
        txtPhoneVal = v.findViewById(R.id.phoneCust);
        btnLogOut = v.findViewById(R.id.card_log_out);
        favCard = v.findViewById(R.id.fav_card);
        addressCard = v.findViewById(R.id.addCard);

        favCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FavActivity.class));

            }
        });
        addressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddressBookActivity.class));

            }
        });




        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            lUseless.setVisibility(View.GONE);
            lMain.setVisibility(View.VISIBLE);
            currentUser = mAuth.getCurrentUser();
            ref = FirebaseDatabase.getInstance().getReference().child("customers").child(currentUser.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        txtNameVal.setText(name);
                        txtPhoneVal.setText(phone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

            //logout btn
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //show warning
                    logoutDialog = new Dialog(getContext());
                    logoutDialog.setContentView(R.layout.logout_confirmation_dialog);
                    logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    logoutDialog.setCancelable(false);
                    logoutDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
                    logoutDialog.getWindow().setGravity(Gravity.CENTER);

                    logoutDialog.show();


                    logoutDialog.findViewById(R.id.can_out_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logoutDialog.dismiss();

                        }
                    });

                    logoutDialog.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            logoutDialog.dismiss();
                            mAuth.signOut();
                            startActivity(new Intent(getContext(), Dashboard.class));



                        }
                    });





                }
            });


        }else {
            lUseless.setVisibility(View.VISIBLE);
            lMain.setVisibility(View.GONE);
        }




        lgn = v.findViewById(R.id.login_profile);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open cart
                startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            lUseless.setVisibility(View.GONE);
            lMain.setVisibility(View.VISIBLE);


        }else {
            lUseless.setVisibility(View.VISIBLE);
            lMain.setVisibility(View.GONE);
        }

    }
}
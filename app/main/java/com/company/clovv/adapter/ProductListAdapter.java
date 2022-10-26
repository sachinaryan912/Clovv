package com.company.clovv.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.company.clovv.DBHelper;
import com.company.clovv.R;
import com.company.clovv.modal.ProductListModal;
import com.company.clovv.modal.SqLiteModalCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ProductListModal> listProducts;
    DBHelper helper;
    Dialog btmPackDialog;
    int qt = 1;
    public boolean favBtn = false;
    Dialog shopOverlapDialog;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;
    FirebaseFirestore firebaseFirestore;


    public ProductListAdapter(Context context, ArrayList<ProductListModal> listProducts) {
        this.context = context;
        this.listProducts = listProducts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //create database
        helper = new DBHelper(context);

        View v = LayoutInflater.from(context).inflate(R.layout.product_item_layout,parent,false);
        return new MyViewHolder(v);






    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ProductListModal productListModal = listProducts.get(position);

        if (productListModal.getProductPackageType().equals("Packed")){
            holder.txt_pack.setText(productListModal.getProductPackageType());
            holder.txt_name.setText(productListModal.getProductName()+" ( "+productListModal.getProductPackageWeight()+productListModal.getProductWtType()+" ) ");
            holder.txt_price.setText(context.getResources().getString(R.string.rs) + productListModal.getProductPrice());
        }else {
            holder.txt_pack.setText(productListModal.getProductPackageType());
            holder.txt_name.setText(productListModal.getProductName());
            holder.txt_price.setText(context.getResources().getString(R.string.rs) + productListModal.getProductPrice()+" "+productListModal.getProductWeightPerSym());
        }





        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("favProducts")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if (document.getData().containsValue(productListModal.getProductId())){
//                                        holder.l_fav.playAnimation();
                                        holder.l_fav.setMinAndMaxProgress(0.5f,1.0f);
                                    }else {
//                                        holder.l_fav.playAnimation();
                                        holder.l_fav.setMinAndMaxProgress(0.0f,0.5f);
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });






        }










        holder.l_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser()!=null){


                    try{


                        firebaseFirestore.collection("favProducts")
                                .document(currentUser.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()){
                                                if (document.getData().containsValue(productListModal.getProductId())){
                                                    //remove
                                                    // Remove the 'capital' field from the document
                                                    Map<String,Object> updates = new HashMap<>();
                                                    updates.put(productListModal.getProductId(), FieldValue.delete());

                                                    firebaseFirestore.collection("favProducts")
                                                            .document(currentUser.getUid())
                                                            .update(updates)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        holder.l_fav.playAnimation();
                                                                        holder.l_fav.setMinAndMaxProgress(0.5f,1.0f);

                                                                        Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                }else{
                                                    String key = String.valueOf(System.currentTimeMillis());
                                                    HashMap<String, String> map = new HashMap<>();
                                                    map.put(key,productListModal.getProductId());

                                                    firebaseFirestore.collection("favProducts")
                                                            .document(currentUser.getUid())
                                                            .set(map, SetOptions.merge())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        holder.l_fav.playAnimation();
                                                                        holder.l_fav.setMinAndMaxProgress(0.0f,0.5f);
                                                                        Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }else {
                                                String key = String.valueOf(System.currentTimeMillis());
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put(key,productListModal.getProductId());

                                                firebaseFirestore.collection("favProducts")
                                                        .document(currentUser.getUid())
                                                        .set(map, SetOptions.merge())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    holder.l_fav.playAnimation();
                                                                    holder.l_fav.setMinAndMaxProgress(0.0f,0.5f);
                                                                    Toast.makeText(context, "added", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });



                    }catch (Exception e){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }









                }else {
                    Toast.makeText(context, "please login to add favourite", Toast.LENGTH_SHORT).show();
                }







            }
        });



        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = context.getSharedPreferences("cartItem", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();

                //open bottom sheet
                String packType = productListModal.getProductPackageType();

                if (packType.equals("Packed")){

                    btmPackDialog = new Dialog(v.getRootView().getContext());
                    btmPackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View v2 = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.btn_packed_item,null);
                    btmPackDialog.setContentView(v2);
                    btmPackDialog.setCancelable(true);

                    //dialog
                    shopOverlapDialog = new Dialog(v.getRootView().getContext());
                    shopOverlapDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View v3 = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.shop_confirmation_alert,null);
                    shopOverlapDialog.setContentView(v3);
                    shopOverlapDialog.setCancelable(false);
                    shopOverlapDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    shopOverlapDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    shopOverlapDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;





                    TextView nameTxt = btmPackDialog.findViewById(R.id.product_name_dialog);
                    TextView descTxt = btmPackDialog.findViewById(R.id.product_desc_dialog);
                    TextView priceTxt = btmPackDialog.findViewById(R.id.product_price_dialog);
                    TextView errorData = btmPackDialog.findViewById(R.id.errorTxt);

                    errorData.setVisibility(View.GONE);


                    nameTxt.setText(productListModal.getProductName());
                    descTxt.setText(productListModal.getProductDescription());
                    priceTxt.setText(context.getResources().getString(R.string.rs) + productListModal.getProductPrice());

                    EditText quantVal = btmPackDialog.findViewById(R.id.quant_val);
                    quantVal.setText(String.valueOf(qt));

                    LinearLayout addBtn = btmPackDialog.findViewById(R.id.add_quant_btn);






                    btmPackDialog.findViewById(R.id.add_quant_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //add quant
                            qt=qt+1;

                            quantVal.setText(String.valueOf(qt));
                            if (qt==Integer.parseInt(productListModal.getProductItemAvailable())){
                                errorData.setVisibility(View.VISIBLE);
                                addBtn.setClickable(false);
                                Toast.makeText(context, "Stock limit exceed", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });

                    btmPackDialog.findViewById(R.id.remove_quant_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (qt == 1){
                                btmPackDialog.dismiss();
                            }else {
                                qt=qt-1;
                                quantVal.setText(String.valueOf(qt));
                            }

                            if (qt<=Integer.parseInt(productListModal.getProductItemAvailable())){
                                errorData.setVisibility(View.GONE);
                                addBtn.setClickable(true);
                                return;
                            }

                        }
                    });

                    btmPackDialog.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String productUnitPrice = String.valueOf(productListModal.getProductPrice());
                            String shopId = productListModal.getProductOwnerId();
                            String productName = productListModal.getProductName();
                            String qVal = quantVal.getText().toString().trim();
                            String packTypeVal = productListModal.getProductPackageType();
                            String productId = productListModal.getProductId();
                            String totalStock = productListModal.getProductItemAvailable();
                            double productPrice = productListModal.getProductPrice()*Integer.parseInt(qVal);



                            Cursor res = helper.getData();
                            if (res.getCount()==0){
                                Boolean insertData = helper.insertCartData(productName,productPrice,shopId,qVal,packTypeVal,"none",productUnitPrice,"none",productId,productListModal.getProductWtType(),totalStock);
                                if (insertData){
                                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(context, "This item already present in your cart", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                int idIndex = res.getColumnIndex("shopIdData");
                                while (res.moveToNext()){
                                    String id = res.getString(idIndex);
                                    if (id.equals(shopId)){
                                        Boolean insertData = helper.insertCartData(productName,productPrice,shopId,qVal,packTypeVal,"none",productUnitPrice,"none",productId,productListModal.getProductWtType(),totalStock);
                                        if (insertData){
                                            Toast.makeText(context, "item added", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "This item already present in your cart", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        shopOverlapDialog.show();

                                        shopOverlapDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shopOverlapDialog.dismiss();

                                            }
                                        });

                                        shopOverlapDialog.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shopOverlapDialog.dismiss();
                                                helper.deleteAllCart();


                                                Boolean insertData = helper.insertCartData(productName,productPrice,shopId,qVal,packTypeVal,"none",productUnitPrice,"none",productId,productListModal.getProductWtType(),totalStock);
                                                if (insertData){
                                                    Toast.makeText(context, "item added", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(context, "This item already present in your cart", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });



                                    }

                                }

                            }


                            //end of storing data in sqlite



                            btmPackDialog.dismiss();
                        }
                    });


                    btmPackDialog.show();
                    qt = 1;



                    btmPackDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    btmPackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btmPackDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
                    btmPackDialog.getWindow().setGravity(Gravity.BOTTOM);

                }else {

                    btmPackDialog = new Dialog(v.getRootView().getContext());
                    btmPackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View v3 = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.btm_unpack_dialog,null);
                    btmPackDialog.setContentView(v3);
                    btmPackDialog.setCancelable(true);

                    //dialog
                    shopOverlapDialog = new Dialog(v.getRootView().getContext());
                    shopOverlapDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View v4 = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.shop_confirmation_alert,null);
                    shopOverlapDialog.setContentView(v4);
                    shopOverlapDialog.setCancelable(false);
                    shopOverlapDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    shopOverlapDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    shopOverlapDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                    Spinner spin_wt = btmPackDialog.findViewById(R.id.spinner_wt_dialog);
                    ArrayAdapter<CharSequence> adapter2;


                    if (productListModal.getProductWtType().equals("KG") || productListModal.getProductWtType().equals("gram")){
                        adapter2 = ArrayAdapter
                                .createFromResource(v.getRootView().getContext(),R.array.weight_type_kg, android.R.layout.simple_spinner_item);


                    }else {
                        adapter2 = ArrayAdapter
                                .createFromResource(v.getRootView().getContext(),R.array.weight_type_l, android.R.layout.simple_spinner_item);

                    }

                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_wt.setAdapter(adapter2);





                    TextView nameTxt = btmPackDialog.findViewById(R.id.product_name_dialog);
                    TextView descTxt = btmPackDialog.findViewById(R.id.product_desc_dialog);
                    TextView priceTxt = btmPackDialog.findViewById(R.id.product_price_dialog);

                    String pri = productListModal.getProductPrice()+" "+productListModal.getProductWeightPerSym();


                    nameTxt.setText(productListModal.getProductName());
                    descTxt.setText(productListModal.getProductDescription());
                    priceTxt.setText(pri);
                    EditText edtValWeight = btmPackDialog.findViewById(R.id.edt_wt_val);

                    btmPackDialog.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String unpkQuantity = edtValWeight.getText().toString().trim();
                            String userWtVal = spin_wt.getSelectedItem().toString();
                            String shopPerWtVal = productListModal.getProductWeightPerSym();
                            String shopId = productListModal.getProductOwnerId();
                            String productName = productListModal.getProductName();
                            String productId = productListModal.getProductId();
                            String totalStockUnpacked = productListModal.getProductItemAvailable();
                            String proUnitPrice = String.valueOf(productListModal.getProductPrice());

                            double priceProduct = 0;

                            //calculating price of unpacked product
                            if (userWtVal.equals("L") && shopPerWtVal.equals("/L")){
                                //calculate price
                                priceProduct = Integer.parseInt(unpkQuantity)*productListModal.getProductPrice();

                            }else if (userWtVal.equals("L") && shopPerWtVal.equals("/ml")){

                                float unit = Integer.parseInt(unpkQuantity)*1000;
                                priceProduct = unit*productListModal.getProductPrice();

                            }else if (userWtVal.equals("ml") && shopPerWtVal.equals("/L")){

                                float unit = Float.parseFloat(unpkQuantity)/1000;
                                priceProduct = unit*productListModal.getProductPrice();

                            }else if (userWtVal.equals("ml") && shopPerWtVal.equals("/ml")){
                                priceProduct = Integer.parseInt(unpkQuantity)*productListModal.getProductPrice();

                            }else if (userWtVal.equals("KG") && shopPerWtVal.equals("/KG")){
                                priceProduct = Integer.parseInt(unpkQuantity)*productListModal.getProductPrice();

                            }else if (userWtVal.equals("KG") && shopPerWtVal.equals("/gram")){
                                float unit = Integer.parseInt(unpkQuantity)*1000;
                                priceProduct = unit*productListModal.getProductPrice();

                            }else if (userWtVal.equals("gram") && shopPerWtVal.equals("/KG")){
                                float unit = Float.parseFloat(unpkQuantity)/1000;
                                priceProduct = unit*productListModal.getProductPrice();

                            }else if (userWtVal.equals("gram") && shopPerWtVal.equals("/gram")){
                                priceProduct = Integer.parseInt(unpkQuantity)*productListModal.getProductPrice();

                            }




                            //end of calculation




                            //store data in sqlite
                            Cursor res = helper.getData();
                            if (res.getCount()==0){
                                Boolean insertData = helper.insertCartData(productName,priceProduct,shopId,unpkQuantity,productListModal.getProductPackageType(),userWtVal,proUnitPrice,shopPerWtVal,productId,productListModal.getProductWtType(),totalStockUnpacked);
                                if (insertData){
                                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(context, "This item already present in your cart", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                int idIndex = res.getColumnIndex("shopIdData");
                                while (res.moveToNext()){
                                    String id = res.getString(idIndex);
                                    if (id.equals(shopId)){
                                        Boolean insertData = helper.insertCartData(productName,priceProduct,shopId,unpkQuantity,productListModal.getProductPackageType(),userWtVal,proUnitPrice,shopPerWtVal,productId,productListModal.getProductWtType(),totalStockUnpacked);
                                        if (insertData){
                                            Toast.makeText(context, "item added", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "This item already present in your cart", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {

                                        shopOverlapDialog.show();

                                        shopOverlapDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shopOverlapDialog.dismiss();

                                            }
                                        });

                                        double finalPriceProduct = priceProduct;
                                        shopOverlapDialog.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                shopOverlapDialog.dismiss();
                                                helper.deleteAllCart();


                                                Boolean insertData = helper.insertCartData(productName, finalPriceProduct,shopId,unpkQuantity,productListModal.getProductPackageType(),userWtVal,proUnitPrice,shopPerWtVal,productId,productListModal.getProductWtType(),totalStockUnpacked);
                                                if (insertData){
                                                    Toast.makeText(context, "item added", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(context, "This item already present in your cart", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });



                                    }

                                }

                            }


                            //end of storing data in sqlite




                            btmPackDialog.dismiss();
                        }
                    });


                    btmPackDialog.show();

                    btmPackDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    btmPackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    btmPackDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
                    btmPackDialog.getWindow().setGravity(Gravity.BOTTOM);

                }





            }
        });

        if (productListModal.getProductImageUrl()!=null){

            Picasso.get().load(productListModal.getProductImageUrl()).into(holder.img_product);

        }

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_name,txt_price,txt_type,txt_pack;
        ImageView img_product;
        Button btn_add;
        LottieAnimationView l_fav;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name = itemView.findViewById(R.id.product_name1);
            txt_price = itemView.findViewById(R.id.product_price1);
            btn_add = itemView.findViewById(R.id.add_btn);
            l_fav = itemView.findViewById(R.id.lot_fav);
            txt_pack = itemView.findViewById(R.id.packType);
            img_product = itemView.findViewById(R.id.product_image1);

        }
    }
}

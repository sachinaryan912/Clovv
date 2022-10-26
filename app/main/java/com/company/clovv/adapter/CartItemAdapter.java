package com.company.clovv.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.clovv.DBHelper;
import com.company.clovv.Interface.ChangeNumberItemListener;
import com.company.clovv.R;
import com.company.clovv.modal.SqLiteModalCart;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    Context context;
    ArrayList<SqLiteModalCart> listCart;
    ChangeNumberItemListener changeNumberItemListener;


    DBHelper helper;
    Dialog editCartDialog,editCartDialogPacked;
    int quant;


    public CartItemAdapter(Context context, ArrayList<SqLiteModalCart> listCart, ChangeNumberItemListener changeNumberItemListener) {
        this.context = context;
        this.listCart = listCart;
        this.changeNumberItemListener = changeNumberItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        helper = new DBHelper(context);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_lay,parent,false);

        return new MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SqLiteModalCart sqLiteModalCart = listCart.get(position);
        holder.pName.setText(sqLiteModalCart.getpName());
        holder.pPrice.setText(context.getResources().getString(R.string.rs)+sqLiteModalCart.getPricee());

        if (sqLiteModalCart.getProductType().equals("Packed")){
            holder.pQuantity.setText("Quantity : "+sqLiteModalCart.getpQuantity());
        }else {
            holder.pQuantity.setText("Quantity : "+sqLiteModalCart.getpQuantity()+" "+sqLiteModalCart.getUserWt());
        }





        holder.edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for unpacked
                editCartDialog = new Dialog(v.getRootView().getContext());
                editCartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View v3 = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.update_btm_unpacked_dialog,null);
                editCartDialog.setContentView(v3);
                editCartDialog.setCancelable(true);

                //for packed
                editCartDialogPacked = new Dialog(v.getRootView().getContext());
                editCartDialogPacked.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View v2 = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.update_btm_packed_dialog,null);
                editCartDialogPacked.setContentView(v2);
                editCartDialogPacked.setCancelable(true);

                //check condition
                if (sqLiteModalCart.getProductType().equals("Packed")){
                    editCartDialogPacked.show();

                    TextView txtName = editCartDialogPacked.findViewById(R.id.product_name_dialog);
                    TextView txtPrice = editCartDialogPacked.findViewById(R.id.product_price_dialog);
                    EditText etQuantity = editCartDialogPacked.findViewById(R.id.quant_val);


                    txtName.setText(sqLiteModalCart.getpName());
                    txtPrice.setText(context.getResources().getString(R.string.rs)+" "+sqLiteModalCart.getUnitPrice());
                    quant = Integer.parseInt(sqLiteModalCart.getpQuantity());
                    etQuantity.setText(String.valueOf(quant));
                    TextView errorData = editCartDialogPacked.findViewById(R.id.errorTxt);
                    errorData.setVisibility(View.GONE);
                    LinearLayout addBtn = editCartDialogPacked.findViewById(R.id.add_quant_btn);


                    editCartDialogPacked.findViewById(R.id.add_quant_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            quant=quant+1;
                            etQuantity.setText(String.valueOf(quant));

                            if (quant==Integer.parseInt(sqLiteModalCart.getTotalStock())){
                                errorData.setVisibility(View.VISIBLE);
                                addBtn.setClickable(false);
                                Toast.makeText(context, "Stock limit exceed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    });

                    editCartDialogPacked.findViewById(R.id.remove_quant_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (quant ==1){
                                editCartDialogPacked.dismiss();
                            }else {
                                quant=quant-1;
                                etQuantity.setText(String.valueOf(quant));
                            }

                            if (quant<=Integer.parseInt(sqLiteModalCart.getTotalStock())){
                                errorData.setVisibility(View.GONE);
                                addBtn.setClickable(true);
                                return;
                            }

                        }
                    });

                    editCartDialogPacked.findViewById(R.id.delete_btn_from_cart).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //delete item

                                Boolean checkDelete = helper.deleteCartItem(sqLiteModalCart.getpName());
                                if (checkDelete){
                                    Toast.makeText(v.getContext(), "item deleted", Toast.LENGTH_SHORT).show();

                                    //refresh data
                                    listCart.clear();
                                    notifyDataSetChanged();
                                    changeNumberItemListener.change();

                                    editCartDialogPacked.dismiss();
                                }else {
                                    Toast.makeText(v.getContext(), "failed to delete", Toast.LENGTH_SHORT).show();
                                    editCartDialogPacked.dismiss();
                                }

                        }
                    });

                    editCartDialogPacked.findViewById(R.id.update_cart_pack).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            String updatedPrice = String.valueOf(quant*Float.parseFloat(sqLiteModalCart.getUnitPrice()));
                            Boolean updateCartItem = helper.updateCartData(sqLiteModalCart.getpName(), updatedPrice,String.valueOf(quant),"none");

                            if (updateCartItem){
                                listCart.clear();
                                Toast.makeText(context, "cart item updated", Toast.LENGTH_SHORT).show();
                                editCartDialogPacked.dismiss();
                                changeNumberItemListener.change();

                            }else {
                                Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show();
                                editCartDialogPacked.dismiss();
                            }



                        }
                    });



                }else {
                    editCartDialog.show();


                    TextView txtProductName,txtUnitPrice,error;
                    Spinner spinnerWt;
                    EditText edQuantity;
                    Button delBtn,updateBtn;

                    txtProductName = editCartDialog.findViewById(R.id.product_name_dialog);
                    txtUnitPrice = editCartDialog.findViewById(R.id.product_price_dialog);
                    spinnerWt = editCartDialog.findViewById(R.id.spinner_wt_dialog);
                    edQuantity = editCartDialog.findViewById(R.id.edt_wt_val);
                    error = editCartDialog.findViewById(R.id.errorTxt);
                    updateBtn = editCartDialog.findViewById(R.id.update_cart_unpack);
                    delBtn = editCartDialog.findViewById(R.id.delete_btn_from_cart);

                    error.setVisibility(View.GONE);

                    txtProductName.setText(sqLiteModalCart.getpName());
                    txtUnitPrice.setText(sqLiteModalCart.getUnitPrice());

                    ArrayAdapter<CharSequence> adapter = null;


                    if (sqLiteModalCart.getUserWt().equals("KG") ){
                        adapter = ArrayAdapter
                                .createFromResource(v.getRootView().getContext(),R.array.weight_type_kg, android.R.layout.simple_spinner_item);


                    }else if (sqLiteModalCart.getUserWt().equals("gram")){
                        adapter = ArrayAdapter
                                .createFromResource(v.getRootView().getContext(),R.array.weight_type_kg_reverse, android.R.layout.simple_spinner_item);

                    }else if (sqLiteModalCart.getUserWt().equals("L")){
                        adapter = ArrayAdapter
                                .createFromResource(v.getRootView().getContext(),R.array.weight_type_l, android.R.layout.simple_spinner_item);

                    }else if (sqLiteModalCart.getUserWt().equals("ml")){
                        adapter = ArrayAdapter
                                .createFromResource(v.getRootView().getContext(),R.array.weight_type_l_reverse, android.R.layout.simple_spinner_item);

                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerWt.setAdapter(adapter);

                    edQuantity.setText(sqLiteModalCart.getpQuantity());

                    spinnerWt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //check
                            String spinVal = spinnerWt.getSelectedItem().toString();
                            String valueEdt = edQuantity.getText().toString();

                            if (sqLiteModalCart.getProductStockWtType().equals("KG")&&spinVal.equals("KG") ||
                                    sqLiteModalCart.getProductStockWtType().equals("L")&&spinVal.equals("L") ||
                                    sqLiteModalCart.getProductStockWtType().equals("gram")&&spinVal.equals("gram") ||
                                    sqLiteModalCart.getProductStockWtType().equals("ml")&&spinVal.equals("ml")){

                                //check
                                int stock = Integer.parseInt(sqLiteModalCart.getTotalStock());
                                int userValWeight = Integer.parseInt(valueEdt);

                                if (userValWeight>stock){
                                    updateBtn.setClickable(false);
                                    updateBtn.setEnabled(false);
                                    error.setVisibility(View.VISIBLE);
                                }else {
                                    updateBtn.setClickable(true);
                                    updateBtn.setEnabled(true);
                                    error.setVisibility(View.GONE);
                                }


                            }else if (sqLiteModalCart.getProductStockWtType().equals("KG")&&spinVal.equals("gram") ||
                                    sqLiteModalCart.getProductStockWtType().equals("L")&&spinVal.equals("ml")){

                                //check
                                int stock = Integer.parseInt(sqLiteModalCart.getTotalStock())*1000;
                                int userValWeight = Integer.parseInt(valueEdt);

                                if (userValWeight>stock){
                                    updateBtn.setClickable(false);
                                    updateBtn.setEnabled(false);
                                    error.setVisibility(View.VISIBLE);
                                }else {
                                    updateBtn.setClickable(true);
                                    updateBtn.setEnabled(true);
                                    error.setVisibility(View.GONE);
                                }

                            }else if (sqLiteModalCart.getProductStockWtType().equals("gram")&&spinVal.equals("KG") ||
                                    sqLiteModalCart.getProductStockWtType().equals("ml")&&spinVal.equals("L")){

                                //check
                                int stock = Integer.parseInt(sqLiteModalCart.getTotalStock());
                                int userValWeight = Integer.parseInt(valueEdt)*1000;

                                if (userValWeight>stock){
                                    updateBtn.setClickable(false);
                                    updateBtn.setEnabled(false);
                                    error.setVisibility(View.VISIBLE);
                                }else {
                                    updateBtn.setClickable(true);
                                    updateBtn.setEnabled(true);
                                    error.setVisibility(View.GONE);
                                }

                            }



                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    edQuantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {



                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            if (s.length()<1){
                                Toast.makeText(context, "Empty value", Toast.LENGTH_SHORT).show();
                            }else {

                                String spinVal = spinnerWt.getSelectedItem().toString();

                                    if (sqLiteModalCart.getProductStockWtType().equals("KG")&&spinVal.equals("KG") ||
                                            sqLiteModalCart.getProductStockWtType().equals("L")&&spinVal.equals("L") ||
                                            sqLiteModalCart.getProductStockWtType().equals("gram")&&spinVal.equals("gram") ||
                                            sqLiteModalCart.getProductStockWtType().equals("ml")&&spinVal.equals("ml")){

                                        //check
                                        int stock = Integer.parseInt(sqLiteModalCart.getTotalStock());
                                        int userValWeight = Integer.parseInt(String.valueOf(s));

                                        if (userValWeight>stock){
                                            updateBtn.setClickable(false);
                                            updateBtn.setEnabled(false);
                                            error.setVisibility(View.VISIBLE);
                                        }else {
                                            updateBtn.setClickable(true);
                                            updateBtn.setEnabled(true);
                                            error.setVisibility(View.GONE);
                                        }


                                    }else if (sqLiteModalCart.getProductStockWtType().equals("KG")&&spinVal.equals("gram") ||
                                            sqLiteModalCart.getProductStockWtType().equals("L")&&spinVal.equals("ml")){

                                        //check
                                        int stock = Integer.parseInt(sqLiteModalCart.getTotalStock())*1000;
                                        int userValWeight = Integer.parseInt(String.valueOf(s));

                                        if (userValWeight>stock){
                                            updateBtn.setClickable(false);
                                            updateBtn.setEnabled(false);
                                            error.setVisibility(View.VISIBLE);
                                        }else {
                                            updateBtn.setClickable(true);
                                            updateBtn.setEnabled(true);
                                            error.setVisibility(View.GONE);
                                        }

                                    }else if (sqLiteModalCart.getProductStockWtType().equals("gram")&&spinVal.equals("KG") ||
                                            sqLiteModalCart.getProductStockWtType().equals("ml")&&spinVal.equals("L")){

                                        //check
                                        int stock = Integer.parseInt(sqLiteModalCart.getTotalStock());
                                        int userValWeight = Integer.parseInt(String.valueOf(s))*1000;

                                        if (userValWeight>stock){
                                            updateBtn.setClickable(false);
                                            updateBtn.setEnabled(false);
                                            error.setVisibility(View.VISIBLE);
                                        }else {
                                            updateBtn.setClickable(true);
                                            updateBtn.setEnabled(true);
                                            error.setVisibility(View.GONE);
                                        }

                                }


                            }

                        }
                    });

                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editCartDialog.dismiss();

                            String qValData = edQuantity.getText().toString();


                            String weightVal = spinnerWt.getSelectedItem().toString();
                            String shopWeightPerVal = sqLiteModalCart.getShopPerWeight();

                            double priceProduct = 0;

                            if (!qValData.equals("")){
                                int qVal = Integer.parseInt(qValData);
                                if (qVal<1){

                                    Toast.makeText(context, "Please enter valid weight", Toast.LENGTH_SHORT).show();

                                }else {
                                    //check

                                    //calculating price of unpacked product
                                    if (weightVal.equals("L") && shopWeightPerVal.equals("/L")){
                                        //calculate price
                                        priceProduct = qVal*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("L") && shopWeightPerVal.equals("/ml")){

                                        float unit = qVal*1000;
                                        priceProduct = unit*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("ml") && shopWeightPerVal.equals("/L")){

                                        float unit = qVal/1000;
                                        priceProduct = unit*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("ml") && shopWeightPerVal.equals("/ml")){
                                        priceProduct = qVal*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("KG") && shopWeightPerVal.equals("/KG")){
                                        priceProduct = qVal*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("KG") && shopWeightPerVal.equals("/gram")){
                                        float unit = qVal*1000;
                                        priceProduct = unit*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("gram") && shopWeightPerVal.equals("/KG")){
                                        float unit = qVal/1000;
                                        priceProduct = unit*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }else if (weightVal.equals("gram") && shopWeightPerVal.equals("/gram")){
                                        priceProduct = qVal*Float.parseFloat(sqLiteModalCart.getUnitPrice());

                                    }

                                    //end of calculation

                                    String updatedPric = String.format("%.2f",priceProduct);


                                    Boolean updateUnpacked = helper.updateCartData(sqLiteModalCart.getpName(), updatedPric,String.valueOf(qVal),weightVal);
                                    if (updateUnpacked){

                                        listCart.clear();
                                        Toast.makeText(context, "cart item updated", Toast.LENGTH_SHORT).show();
                                        editCartDialog.dismiss();
                                        changeNumberItemListener.change();

                                    }else {
                                        Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show();
                                        editCartDialog.dismiss();
                                    }

                                    //check ends


                                }

                            }else {
                                Toast.makeText(context, "Please enter valid weight", Toast.LENGTH_SHORT).show();
                            }




                        }
                    });

                    delBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //del

                            Boolean checkDelete = helper.deleteCartItem(sqLiteModalCart.getpName());
                            if (checkDelete){
                                Toast.makeText(v.getContext(), "item deleted", Toast.LENGTH_SHORT).show();

                                //refresh data
                                listCart.clear();
                                notifyDataSetChanged();
                                changeNumberItemListener.change();

                            }else {
                                Toast.makeText(v.getContext(), "failed to delete", Toast.LENGTH_SHORT).show();
                            }
                            editCartDialog.dismiss();


                        }
                    });




                }

                editCartDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editCartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editCartDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
                editCartDialog.getWindow().setGravity(Gravity.BOTTOM);


                editCartDialogPacked.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editCartDialogPacked.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editCartDialogPacked.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
                editCartDialogPacked.getWindow().setGravity(Gravity.BOTTOM);


            }
        });



    }


    //remove items from cart

    public void removeItem(int position) {
        listCart.remove(position);
        notifyItemRemoved(position);
    }




    @Override
    public int getItemCount() {
        return listCart.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pName,pPrice,pQuantity;
        LinearLayout edtBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pName = itemView.findViewById(R.id.txtName);
            pPrice = itemView.findViewById(R.id.txtPrice);
            pQuantity = itemView.findViewById(R.id.txtQuant);
            edtBtn = itemView.findViewById(R.id.editBtn);


        }
    }


}

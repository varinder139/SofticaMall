package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;
    private TextView title, price, quantity;
    private ImageView productImage, orderedIndicator, packedIndicator, shippedIndicator, deliveredIndicator;
    private ProgressBar o_p_progress, p_s_progress, s_d_progresss;
    private TextView orderedTitle, packedTitle, shippedTitle, deliveredTitle;
    private TextView orderedDate, packedDate, shippedDate, deliveredDate;
    private TextView orderedBody, packedBody, shippedBody, deliveredBody;
    private LinearLayout rateNowContainer;
    private int rating;
    private TextView fullName, address, pincode;
    private TextView totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount;
    private Dialog loadingDialog, cancelDialog;
    private SimpleDateFormat simpleDateFormat;
    private Button cancelOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Loading Dialog
        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading Dialog

        //cancel Order Dialog
        cancelDialog = new Dialog(OrderDetailsActivity.this);
        cancelDialog.setContentView(R.layout.o_cancel_dialog);
        cancelDialog.setCancelable(true);
        cancelDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
      //  cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //cancel Order Dialog


        position = getIntent().getIntExtra("Position", -1);
        final MyOrderItemModel model = DBqueries.myOrderItemModelList.get(position);

        title = findViewById(R.id.od_product_title);
        price = findViewById(R.id.od_product_price);
        quantity = findViewById(R.id.od_pro_qty);

        productImage = findViewById(R.id.od_product_image);

        orderedIndicator = findViewById(R.id.od_order_indiator);
        packedIndicator = findViewById(R.id.od_packed_indiator);
        shippedIndicator = findViewById(R.id.od_shiping_indiator);
        deliveredIndicator = findViewById(R.id.od_delived_indicator);

        o_p_progress = findViewById(R.id.od_order_packed_progressBar);
        p_s_progress = findViewById(R.id.od_packed_shiffing_progressBar);
        s_d_progresss = findViewById(R.id.od_shipping_delived_progressBar);

        orderedTitle = findViewById(R.id.od_orded_title);
        packedTitle = findViewById(R.id.od_packed_title);
        shippedTitle = findViewById(R.id.od_shipping_title);
        deliveredTitle = findViewById(R.id.od_delived_title);

        orderedDate = findViewById(R.id.od_orded_date);
        packedDate = findViewById(R.id.od_packed_date);
        shippedDate = findViewById(R.id.od_shipping_date);
        deliveredDate = findViewById(R.id.od_delived_date);

        orderedBody = findViewById(R.id.od_orded_body);
        packedBody = findViewById(R.id.od_packed_body);
        shippedBody = findViewById(R.id.od_shipping_body);
        deliveredBody = findViewById(R.id.od_delived_body);

        rateNowContainer = findViewById(R.id.rate_now_container);

        fullName = findViewById(R.id.fullName);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        totalItems = findViewById(R.id.total_items);
        totalItemPrice = findViewById(R.id.total_items_price);
        deliveryPrice = findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        savedAmount = findViewById(R.id.saved_amount);
        cancelOrderBtn = findViewById(R.id.cancel_btn);

        title.setText(model.getProductTitle());
        if (!model.getDisountedPrice().equals("")) {
            price.setText("Rs. "+model.getDisountedPrice()+"/-");
        }else {
            price.setText("Rs. "+model.getProdutPrice()+"/-");
        }
        quantity.setText("Qty :"+String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProdutImage()).into(productImage);

        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");
        switch (model.getOrderStatus()){
            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                p_s_progress.setVisibility(View.GONE);
                s_d_progresss.setVisibility(View.GONE);
                o_p_progress.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);

                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedIndicator.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);

                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                o_p_progress.setProgress(100);

                p_s_progress.setVisibility(View.GONE);
                s_d_progresss.setVisibility(View.GONE);


                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedIndicator.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);

                break;
            case "Shipped":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShifftDate())));

                o_p_progress.setProgress(100);
                p_s_progress.setProgress(100);

                s_d_progresss.setVisibility(View.GONE);

                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);
                break;

            case "Out for Delivery":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShifftDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliverDate())));

                o_p_progress.setProgress(100);
                p_s_progress.setProgress(100);
                s_d_progresss.setProgress(100);

                deliveredTitle.setText("Out for Delivery");
                deliveredBody.setText("Your order is out for delivery.");

                break;

            case "Delivered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShifftDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliverDate())));

                o_p_progress.setProgress(100);
                p_s_progress.setProgress(100);
                s_d_progresss.setProgress(100);


                break;
            case "Cancelled":
                if (model.getPackedDate().after(model.getOrderDate())){

                    if (model.getShifftDate().after(model.getPackedDate())){

                        //canceled here at level on deleivered status
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShifftDate())));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your Order has been cancelled");


                        o_p_progress.setProgress(100);
                        p_s_progress.setProgress(100);
                        s_d_progresss.setProgress(100);

                    }else{
                        ///cancelled at shippling level
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        shippedTitle.setText("Cancelled");
                        shippedBody.setText("Your Order has been cancelled");


                        o_p_progress.setProgress(100);
                        p_s_progress.setProgress(100);

                        s_d_progresss.setVisibility(View.GONE);

                        deliveredBody.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredIndicator.setVisibility(View.GONE);

                    }

                }else {
                    /// cancel at packing level
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    orderedDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                    packedTitle.setText("Cancelled");
                    packedBody.setText("Your Order has been cancelled");

                    o_p_progress.setProgress(100);

                    p_s_progress.setVisibility(View.GONE);
                    s_d_progresss.setVisibility(View.GONE);


                    shippedBody.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);
                    shippedIndicator.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);

                    deliveredBody.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredIndicator.setVisibility(View.GONE);
                }
                break;
        }

        ///////Rating Layout
        rating = model.getRating();
        setRating(rating);

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starposition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingDialog.show();
                    setRating(starposition);
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductID());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                            if (rating != 0){
                                Long increase = documentSnapshot.getLong(starposition+1+"_star") + 1;
                                Long decrease = documentSnapshot.getLong(rating+1+"_star") - 1;
                                transaction.update(documentReference,starposition+1+"_star", increase);
                                transaction.update(documentReference,rating+1+"_star", decrease);
                            }else {
                                Long increase = documentSnapshot.getLong(starposition+1+"_star") + 1;
                                transaction.update(documentReference,starposition+1+"_star", increase);
                            }


                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            Map<String, Object> myRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(model.getProductID())) {
                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(model.getProductID()), (long) starposition + 1);
                            } else {

                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), model.getProductID());
                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starposition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        DBqueries.myOrderItemModelList.get(position).setRating(starposition);
                                        if(DBqueries.myRatedIds.contains(model.getProductID())){
                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(model.getProductID()), Long.parseLong(String.valueOf(starposition+1)));
                                        }else {
                                            DBqueries.myRatedIds.add(model.getProductID());
                                            DBqueries.myRating.add(Long.parseLong(String.valueOf(starposition+1)));
                                        }


                                    }else {
                                        //error handle
                                        String error = task.getException().getMessage();
                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }
        ///////Rating Layout

        if (model.isCancellationRequested()){
            cancelOrderBtn.setVisibility(View.VISIBLE);
            cancelOrderBtn.setEnabled(false);
            cancelOrderBtn.setText("Cancellation in process. ");
            cancelOrderBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        }else {
            if (model.getOrderStatus().equals("Ordered") || model.getOrderStatus().equals("Packed")){
                cancelOrderBtn.setVisibility(View.VISIBLE);
                cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cancelDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelDialog.dismiss();
                            }
                        });

                        cancelDialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cancelDialog.dismiss();
                                loadingDialog.show();
                                Map<String, Object> map = new HashMap<>();
                                map.put("Order Id", model.getOrderId());
                                map.put("product Id", model.getProductID());
                                map.put("Order Cancelled", false);
                                FirebaseFirestore.getInstance().collection("CANCELLED ORDERS").document().set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){
                                               FirebaseFirestore.getInstance().collection("ORDERS").document(model.getOrderId()).collection("OrderItems").document(model.getProductID()).update("Cancellation requested", true)
                                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                          if (task.isSuccessful()){
                                                              model.setCancellationRequested(true);
                                                              cancelOrderBtn.setEnabled(false);
                                                              cancelOrderBtn.setText("Cancellation in process. ");
                                                              cancelOrderBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                              cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                                                          }else {
                                                              String error = task.getException().getMessage();
                                                              Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                          }
                                                               loadingDialog.dismiss();
                                                           }
                                                       });
                                           }else {
                                               loadingDialog.dismiss();
                                               String error = task.getException().getMessage();
                                               Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                           }
                                            }
                                        });
                            }
                        });
                        cancelDialog.show();
                    }
                });
            }
        }

        fullName.setText(model.getFullName());
        address.setText(model.getAddress());
        pincode.setText(model.getPincode());

        totalItems.setText("Price("+model.getProductQuantity()+" items)");

        Long totalItemPriceValue;

        if (model.getDisountedPrice().equals("")){
            totalItemPriceValue = model.getProductQuantity()*Long.valueOf(model.getProdutPrice());
            totalItemPrice.setText("Rs."+totalItemPriceValue+"/-");
        }else{
            totalItemPriceValue = model.getProductQuantity()*Long.valueOf(model.getDisountedPrice());
            totalItemPrice.setText("Rs."+totalItemPriceValue+"/-");
        }
        if (model.getDeliveryPrice().equals("FREE")){
            deliveryPrice.setText(model.getDeliveryPrice());
            totalAmount.setText(totalItemPrice.getText());
        }else {
            deliveryPrice.setText("Rs." + model.getDeliveryPrice() + "/-");
            totalAmount.setText("Rs."+ (totalItemPriceValue + Long.valueOf(model.getDeliveryPrice()))+"/-");
        }

        if (!model.getCuttedPrice().equals("")){
            if (!model.getDisountedPrice().equals("")){
                savedAmount.setText("You Saved Rs."+ model.getProductQuantity() * (Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getDisountedPrice())) +" On this Order");
            }else {
                savedAmount.setText("You Saved Rs."+ model.getProductQuantity() * (Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getProdutPrice())) +" On this Order");
            }
        }else{
            if (!model.getDisountedPrice().equals("")){
                savedAmount.setText("You Saved Rs."+ model.getProductQuantity() * (Long.valueOf(model.getProdutPrice()) - Long.valueOf(model.getDisountedPrice())) +" On this Order");
            }else {
                savedAmount.setText("You Saved Rs.0/- On this Order");
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRating(int starPosition1) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starButtion = (ImageView) rateNowContainer.getChildAt(x);
            starButtion.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));

            if (x <= starPosition1) {
                starButtion.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }
    }
}
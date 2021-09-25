package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    private Toolbar toolbar;

    public static CartAdaptor cartAdaptor;
    private RecyclerView deliveyRecyclerView;
    private Button changeorAddNewAddressBtn;
    private TextView totalAmount;
    private TextView fullName, fullAddress, pincode;
    private String name, mobileNo;
    private Button countinueBTN;
    private ImageView payOnline, cod;
    private TextView codTitle;
    private View divider;
    private ConstraintLayout orderComfirmationLayout;
    private ImageView countinueShoppingBtn;
    private TextView orderIDSet;
    private boolean successResponse = false;
    public static boolean fromCart;  // = false;
    public static boolean codOrderConfirm = false;
    private FirebaseFirestore firebaseFirestore;
    String order_id;
    private String paymentMethod = "PAYTM";

    public static final int SELECT_ADDRESS = 0;
    //public static final int selectAddress = 1;
    public static Dialog loadingDialog;
    private Dialog paymenyMethodDialog;

    public static List<CartItemModel> cartItemModelList;

    public static boolean getQtyIDs = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Checkout.preload(getApplicationContext());

        toolbar = findViewById(R.id.toolbar_d);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        changeorAddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullName = findViewById(R.id.fullName);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        countinueBTN = findViewById(R.id.cart_continue_btn);
        orderComfirmationLayout = findViewById(R.id.order_confirmation_layout);
        countinueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderIDSet = findViewById(R.id.order_id);

        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDs = true;

        ///send rendom otp
        order_id = UUID.randomUUID().toString().substring(0, 28);

        //Loading Dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading Dialog

        //payment method Dialog
        paymenyMethodDialog = new Dialog(DeliveryActivity.this);
        paymenyMethodDialog.setContentView(R.layout.payment_method);
        paymenyMethodDialog.setCancelable(true);
        paymenyMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymenyMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        payOnline = paymenyMethodDialog.findViewById(R.id.paytm_btn);
        cod = paymenyMethodDialog.findViewById(R.id.cod_btn);
        codTitle = paymenyMethodDialog.findViewById(R.id.cod_btn_title);
        divider = paymenyMethodDialog.findViewById(R.id.divider_payment_mentod);
        //payment method Dialog

        deliveyRecyclerView = findViewById(R.id.delivery_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveyRecyclerView.setLayoutManager(layoutManager);

        // List<CartItemModel> cartItemModelList = new ArrayList<>();
        //cartItemModelList.add(new CartItemModel(1, "Price (3 items)", "RS. 169999/-", "Free", "Rs. 169999/-", "Rs. 59999/-"));

        cartAdaptor = new CartAdaptor(cartItemModelList, totalAmount, false);
        deliveyRecyclerView.setAdapter(cartAdaptor);
        cartAdaptor.notifyDataSetChanged();

        changeorAddNewAddressBtn.setVisibility(View.VISIBLE);

        changeorAddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getQtyIDs = false;
                Intent intent = new Intent(getApplicationContext(), MyAddressesActivity.class);
                intent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(intent);
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "COD";
                placeOrderDetails();
            }
        });

        countinueBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allProductAvialable = true;
                for (CartItemModel cartItemModel : cartItemModelList) {
                    if (cartItemModel.isQtyError()) {
                        allProductAvialable = false;
                        break;
                    }


                    if (cartItemModel.getType() == CartItemModel.CART_ITEM) {
                        if (!cartItemModel.isCOD()) {
                            cod.setEnabled(false);
                            cod.setAlpha(0.5f);
                            codTitle.setAlpha(0.5f);
                            divider.setVisibility(View.GONE);
                            break;
                        } else {
                            cod.setEnabled(true);
                            cod.setAlpha(1f);
                            codTitle.setAlpha(1f);
                            divider.setVisibility(View.VISIBLE);

                        }
                    }
                }
                if (allProductAvialable) {
                    paymenyMethodDialog.show();
                }
            }
        });

        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getQtyIDs = false;
                // paymenyMethodDialog.dismiss();
                paymentMethod = "PAYTM";
                placeOrderDetails();

                // setUpPayment();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ///accessing the qty
        if (getQtyIDs) {
            loadingDialog.show();
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                for (int y = 0; y < cartItemModelList.get(x).getProdutQty(); y++) {
                    final String qtyDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyDocumentName).set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        cartItemModelList.get(finalX).getQtyIDs().add(qtyDocumentName);

                                        if (finalY + 1 == cartItemModelList.get(finalX).getProdutQty()) {

                                            firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQty()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                List<String> serverQty = new ArrayList<>();

                                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                    serverQty.add(queryDocumentSnapshot.getId());
                                                                }

                                                                long availableQty = 0;
                                                                boolean noLongerAvialable = true;

                                                                for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()) {
                                                                    cartItemModelList.get(finalX).setQtyError(false);
                                                                    if (!serverQty.contains(qtyId)) {
                                                                        //  notAvailableQty++;

                                                                        if (noLongerAvialable) {
                                                                            cartItemModelList.get(finalX).setInStock(false);
                                                                        } else {
                                                                            cartItemModelList.get(finalX).setQtyError(true);
                                                                            cartItemModelList.get(finalX).setMaxQty(availableQty);
                                                                            Toast.makeText(DeliveryActivity.this, "All Product may be not avialable in required qty! SORRY!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        availableQty++;
                                                                        noLongerAvialable = false;
                                                                    }
                                                                }
                                                                cartAdaptor.notifyDataSetChanged();

                                                            } else {
                                                                ///error handle
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });

                                        }
                                    } else {
                                        //error handling
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }
        } else {
            getQtyIDs = true;
        }
///accessing the qty

        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")) {
            fullName.setText(name + " " + mobileNo);
        } else {
            fullName.setText(name + " " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
        }

        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String loaclity = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();

        if (landmark.equals("")) {
            fullAddress.setText(flatNo + " " + loaclity + " " + city + " " + state);
        } else {
            fullAddress.setText(flatNo + " " + loaclity + " " + landmark + " " + city + " " + state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());

        if (codOrderConfirm) {
            showConfirmation(order_id);
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpPayment() {
        //loadingDialog.dismiss();
        final String customer_id = FirebaseAuth.getInstance().getUid();

        String finalAmount = totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2);
        finalAmount = finalAmount + "00";

        String orderPlacedAmount = String.valueOf(finalAmount);

        Checkout checkout = new Checkout();

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", customer_id);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", order_id);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", finalAmount);//pass amount in currency subunits if you pass 50000 then shows 500
            options.put("prefill.email", "varinder139@gmail.com");
            options.put("prefill.contact", "9999585043");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("hi", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        // String orderData = paymentData.getOrderId();

        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("Payment Status", "Paid");
        updateStatus.put("Order Status", "Ordered");
        firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userOrder = new HashMap<>();
                            userOrder.put("order_id", order_id);
                            userOrder.put("time", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                showConfirmation(order_id);
                                            } else {
                                                Toast.makeText(DeliveryActivity.this, "Failed to update user's Order List", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        } else {
                            Toast.makeText(DeliveryActivity.this, "Order CANCELLED", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Failed " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQtyIDs) {
            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                if (!successResponse) {
                    for (final String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))) {
                                            cartItemModelList.get(finalX).getQtyIDs().clear();

                                        }
                                    }
                                });
                    }
                } else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (successResponse) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showConfirmation(String data) {
        successResponse = true;
        codOrderConfirm = false;
        getQtyIDs = false;
        for (int x = 0; x < cartItemModelList.size() - 1; x++) {

            for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {

                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());


            }

        }


        if (MainActivity2.mainActivity != null) {
            MainActivity2.mainActivity.finish();
            MainActivity2.mainActivity = null;
            MainActivity2.showCart = false;
        } else {
            MainActivity2.resetMainAtivity = true;

        }

        if (ProductDetailActivity.productDetailAtivity != null) {
            ProductDetailActivity.productDetailAtivity.finish();
            ProductDetailActivity.productDetailAtivity = null;
        }

        //let the cart empty
        if (fromCart) {
            loadingDialog.show();
            Map<String, Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < DBqueries.cartList.size(); x++) {
                if (cartItemModelList.get(x).isInStock()) {
                    updateCartList.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductID());
                    cartListSize++;

                } else {
                    indexList.add(x);
                }
            }
            updateCartList.put("list_size", cartListSize);
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int x = 0; x < indexList.size(); x++) {
                            DBqueries.cartList.remove(indexList.get(x).intValue());
                            DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }
        countinueBTN.setEnabled(false);
        changeorAddNewAddressBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderIDSet.setText("Order ID " + data);
        orderComfirmationLayout.setVisibility(View.VISIBLE);
        countinueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void placeOrderDetails() {

        String userID = FirebaseAuth.getInstance().getUid();

        loadingDialog.show();
        for (CartItemModel cartItemModel : cartItemModelList) {
            if (cartItemModel.getType() == CartItemModel.CART_ITEM) {

                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID", order_id);
                orderDetails.put("product Id", cartItemModel.getProductID());
                orderDetails.put("Product Image", cartItemModel.getProdutImage());
                orderDetails.put("Product Title", cartItemModel.getProdutTitle());
                orderDetails.put("User Id", userID);
                orderDetails.put("Product Quantity", cartItemModel.getProdutQty());
                if (cartItemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted Price", cartItemModel.getCuttedPrice());
                } else {
                    orderDetails.put("Cutted Price", "");
                }
                orderDetails.put("Product Price", cartItemModel.getProductPrie());
                if (cartItemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen Id", cartItemModel.getSelectedCoupenId());
                } else {
                    orderDetails.put("Coupen Id", "");
                }
                if (cartItemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted Price", cartItemModel.getDiscountedPrice());
                } else {
                    orderDetails.put("Discounted Price", "");
                }
                orderDetails.put("Order date", FieldValue.serverTimestamp());
                orderDetails.put("Packed date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date", FieldValue.serverTimestamp());
                orderDetails.put("Order Status", "Ordered");
                orderDetails.put("Payment Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("FullName", fullName.getText());
                orderDetails.put("Pincode", pincode.getText());
                orderDetails.put("Free Coupens", cartItemModel.getFreeCoupn());
                orderDetails.put("Delivery Price", cartItemModelList.get(cartItemModelList.size() - 1).getDeliveryPrie());
                orderDetails.put("Cancellation request", false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems").document(cartItemModel.getProductID())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {

                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items", cartItemModel.getTotalItem());
                orderDetails.put("Total Items Price", cartItemModel.getTotalItemsPrice());
                orderDetails.put("Delivery Price", cartItemModel.getDeliveryPrie());
                orderDetails.put("Total Amount", cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount", cartItemModel.getSavedAmount());
                orderDetails.put("Payment Status", "not paid");
                orderDetails.put("Order Status", "Cancelled");

                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (paymentMethod.equals("PAYTM")) {
                                setUpPayment();
                            } else {
                                cod();
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
    }

    private void cod() {
        getQtyIDs = false;
        paymenyMethodDialog.dismiss();
        Intent otpIntent = new Intent(DeliveryActivity.this, OTPVerificationAtivity.class);
        otpIntent.putExtra("mobileNo", mobileNo.substring(0, 10));
        otpIntent.putExtra("OrderID", order_id);
        startActivity(otpIntent);
    }

}
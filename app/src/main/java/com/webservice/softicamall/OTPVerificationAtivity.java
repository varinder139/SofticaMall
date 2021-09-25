package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPVerificationAtivity extends AppCompatActivity {
    private TextView phoneNo;
    private EditText otp;
    private Button verifyBTn;
    private String userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification_ativity);

        phoneNo = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        verifyBTn = findViewById(R.id.verify_btn);

        phoneNo.setText("Verification Code has been sent to +91 " + getIntent().getStringExtra("mobileNo"));

        Random random = new Random();
        final int OTP_number = random.nextInt(999999 - 111111) + 111111;
        String SMS_API = "https://www.fast2sms.com/dev/bulkV2";

        //// desi juggad
        Toast.makeText(this, "OTP is : " + OTP_number, Toast.LENGTH_LONG).show();

        verifyBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().equals(String.valueOf(OTP_number))) {
                    Map<String, Object> updateStatus = new HashMap<>();
                   // updateStatus.put("Payment Status", "COD");
                    updateStatus.put("Order Status", "Ordered");

                    final String order_id = getIntent().getStringExtra("OrderID");

                    FirebaseFirestore.getInstance().collection("ORDERS").document(order_id).update(updateStatus)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> userOrder = new HashMap<>();
                                        userOrder.put("order_id", order_id);
                                        userOrder.put("time", FieldValue.serverTimestamp());
                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            DeliveryActivity.codOrderConfirm = true;
                                                            finish();
                                                        }else {
                                                            Toast.makeText(OTPVerificationAtivity.this, "Failed to update user's Order List", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(OTPVerificationAtivity.this, "Order CANCELLED", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(OTPVerificationAtivity.this, "OTP incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });


       /* StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                verifyBTn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (otp.getText().toString().equals(String.valueOf(OTP_number))){
                            DeliveryActivity.codOrderConfirm = true;
                            finish();

                        }else {
                            Toast.makeText(OTPVerificationAtivity.this, "OTP incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(OTPVerificationAtivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("authorization", "A5JsoYVQ9F7fegmI8MSavUdkxThCXpzwtR6P2EDi3GrZKc0ybBHOwfnEK1oLhXBgQu4qbZJiWazlmetA");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("message", "your verifiation otp is");
               // body.put("variables_values", "Rahul|8888888888|6695");
                body.put("route", "dlt");
                body.put("numbers", userNumber);
                body.put("flash", "0");

                return body;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerificationAtivity.this);
        requestQueue.add(stringRequest);  */
    }
}
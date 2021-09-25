package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.webservice.softicamall.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {
    private int previousAddress;
    private LinearLayout addNewAddressBtn;
    private Toolbar toolbar;
    private RecyclerView myAddressesRecyclerView;
    private Button deliverHerebtn;
    private static Address_adaptor address_adaptor;
    private TextView addressSaved;
    private Dialog loadingDialog;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addNewAddressBtn = findViewById(R.id.add_new_address_btn);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Adresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Loading Dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                addressSaved.setText(String.valueOf(DBqueries.addressesModelList.size())+" saved addresses");
            }
        });
       // loadingDialog.show();
        //loading Dialog

        myAddressesRecyclerView = findViewById(R.id.addresses_recyclerview);
        deliverHerebtn = findViewById(R.id.delived_here_btn);
        addressSaved = findViewById(R.id.address_saved);
        previousAddress = DBqueries.selectedAddress;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

//        List<AddressesModel> addressesModelList = new ArrayList<>();
  //      addressesModelList.add(new AddressesModel("Varinder", "Plot no123, patiala", "123456", true));


        mode = getIntent().getIntExtra("MODE", -1);

        if (mode == SELECT_ADDRESS){
            deliverHerebtn.setVisibility(View.VISIBLE);
        }else{
            deliverHerebtn.setVisibility(View.GONE);
        }

        deliverHerebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DBqueries.selectedAddress != previousAddress){
                    final int previousAddressIndex = previousAddress;
                    loadingDialog.show();
                    Map<String, Object> updateSeletion = new HashMap<>();
                    updateSeletion.put("selected_" + String.valueOf(previousAddress+1), false);
                    updateSeletion.put("selected_" + String.valueOf(DBqueries.selectedAddress+1), true);

                    previousAddress = DBqueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                            .update(updateSeletion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                finish();
                            }else {
                                previousAddress = previousAddressIndex;
                                String error = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                }else {
                    finish();
                }
            }
        });

        address_adaptor = new Address_adaptor(DBqueries.addressesModelList, mode, loadingDialog);
        myAddressesRecyclerView.setAdapter(address_adaptor);
        ((SimpleItemAnimator)myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        address_adaptor.notifyDataSetChanged();

        ///click listener on addnew address btn
        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this, AddAddressActivity.class);
                 if (mode != SELECT_ADDRESS){
                     addAddressIntent.putExtra("INTENT", "manage");
                 }else {
                     addAddressIntent.putExtra("INTENT", "null");
                 }
                startActivity(addAddressIntent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        addressSaved.setText(String.valueOf(DBqueries.addressesModelList.size())+" saved addresses");
    }

    public static void refreshItem(int deselect, int select){
        address_adaptor.notifyItemChanged(deselect);
        address_adaptor.notifyItemChanged(select);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       // int id = item.getItemId();

        if (item.getItemId() == android.R.id.home){
            if (mode == SELECT_ADDRESS) {
                if (DBqueries.selectedAddress != previousAddress) {
                    DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSeleted(false);
                    DBqueries.addressesModelList.get(previousAddress).setSeleted(true);
                    DBqueries.selectedAddress = previousAddress;
                }
            }

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mode == SELECT_ADDRESS) {
            if (DBqueries.selectedAddress != previousAddress) {
                DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSeleted(false);
                DBqueries.addressesModelList.get(previousAddress).setSeleted(true);
                DBqueries.selectedAddress = previousAddress;
            }
        }
        super.onBackPressed();
    }
}
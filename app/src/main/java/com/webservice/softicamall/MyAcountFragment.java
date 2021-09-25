package com.webservice.softicamall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MyAcountFragment extends Fragment {


    public MyAcountFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton settingBtn;
    public static final int MANAGE_ADDRESS = 1;
    private Button viewAllAddressBtn, signOutBtn;
    private CircleImageView profileView, currentOrderImage;
    private TextView name, email, tvCurrentOrderStatus;
    private LinearLayout layoutContainer, recentOrderContainer;
    private Dialog loadingDialog;
    private ImageView orderIndicator, packedIndicator, shippedIndicator, deliveredIndicator;
    private ProgressBar o_p_progress, p_s_progress, s_d_progress;
    private TextView youreRecetOrderTitle;
    private TextView addressName, fulladdress, addressPinode;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_acount, container, false);

        //Loading Dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading Dialog


        profileView = view.findViewById(R.id.myACC_profile_img);
        name = view.findViewById(R.id.a_user_name);
        email = view.findViewById(R.id.ac_mail_id);
        layoutContainer = view.findViewById(R.id.layout_container);
        currentOrderImage = view.findViewById(R.id.current_order_img);
        tvCurrentOrderStatus = view.findViewById(R.id.tv_current_status_order);
        orderIndicator = view.findViewById(R.id.os_ordered_indicator);
        packedIndicator = view.findViewById(R.id.os_packed_indicator);
        shippedIndicator = view.findViewById(R.id.os_shifft_indicator);
        deliveredIndicator = view.findViewById(R.id.os_delived_indicator);
        o_p_progress = view.findViewById(R.id.order_packed_progressBar);
        p_s_progress = view.findViewById(R.id.packed_shifft_progressBar);
        s_d_progress = view.findViewById(R.id.shiffed_delived_progressBar);
        youreRecetOrderTitle = view.findViewById(R.id.your_recent_orders_title);
        recentOrderContainer = view.findViewById(R.id.recent_order_container);
        addressName = view.findViewById(R.id.address_full_name);
        fulladdress = view.findViewById(R.id.complete_addesses);
        addressPinode = view.findViewById(R.id.address_pincode);
        signOutBtn = view.findViewById(R.id.sign_out_btn);
        settingBtn = view.findViewById(R.id.ac_stting_buttion);



        ///load oders
        layoutContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
               // loadingDialog.setOnDismissListener(null);
                for (MyOrderItemModel orderItemModel : DBqueries.myOrderItemModelList){
                    if (!orderItemModel.isCancellationRequested()){
                        if (!orderItemModel.getOrderStatus().equals("Delivered") && !orderItemModel.getOrderStatus().equals("Cancelled")){
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProdutImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(currentOrderImage);
                            tvCurrentOrderStatus.setText(orderItemModel.getOrderStatus());

                            switch (orderItemModel.getOrderStatus()){
                                case "Ordered":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    break;
                                case "Packed":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    o_p_progress.setProgress(100);
                                    break;
                                case "Shipped":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    o_p_progress.setProgress(100);
                                    p_s_progress.setProgress(100);
                                    break;
                                case "Out for Delivery":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                                    o_p_progress.setProgress(100);
                                    p_s_progress.setProgress(100);
                                    s_d_progress.setProgress(100);

                                    break;

                            }

                        }
                    }
                }
                int i = 0;
                for (MyOrderItemModel myorderItemModel : DBqueries.myOrderItemModelList){
                    if (i < 4) {
                        if (myorderItemModel.getOrderStatus().equals("Delivered")) {
                            Glide.with(getContext()).load(myorderItemModel.getProdutImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into((ImageView) recentOrderContainer.getChildAt(i));
                            i++;

                        }
                    }else {
                        break;
                    }
                }
                if (i == 0){
                    youreRecetOrderTitle.setText("No Recent Orders.");
                }
                if (i < 3){
                    for (int x = i; x < 4; x++){
                        recentOrderContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
////////////////////////// here load the address
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBqueries.addressesModelList.size() == 0){
                            addressName.setText("No Address");
                            fulladdress.setText("-");
                            addressPinode.setText("-");
                        }else{
                            setAddress();
                        }
                    }
                });
                DBqueries.loadAddress(getContext(), loadingDialog, false);

            }
        });
        DBqueries.loadOrders(getContext(), null, loadingDialog);

        viewAllAddressBtn = view.findViewById(R.id.view_all_address_btn);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyAddressesActivity.class);
                intent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(intent);
            }
        });
        //////////////sigm out here
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                Intent registerintent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerintent);
                getActivity().finish();

            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateUserInfoActivity.class);
                intent.putExtra("Name", name.getText());
                intent.putExtra("Email", email.getText());
                intent.putExtra("Photo", DBqueries.profile);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);
        if (!DBqueries.profile.equals("")){
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(profileView);
        }else {
            profileView.setImageResource(R.drawable.placeholder);
        }

        if (!loadingDialog.isShowing()){
            if (DBqueries.addressesModelList.size() == 0){
                addressName.setText("No Address");
                fulladdress.setText("-");
                addressPinode.setText("-");
            }else{
                setAddress();
            }
        }
    }

    private void setAddress(){
        String nameText, mobileNo;
        nameText = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")) {
            addressName.setText(nameText + " " + mobileNo);
        } else {
            addressName.setText(nameText + " " + mobileNo + " or " + DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
        }

        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String loaclity = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();

        if (landmark.equals("")) {
            fulladdress.setText(flatNo + " " + loaclity + " " + city + " " + state);
        } else {
            fulladdress.setText(flatNo + " " + loaclity + " " + landmark + " " + city + " " + state);
        }
        addressPinode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
    }
}
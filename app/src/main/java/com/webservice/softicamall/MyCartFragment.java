package com.webservice.softicamall;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MyCartFragment extends Fragment {


    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemRecylerView;

    private Button continueBtn;

    private Dialog loadingDialog;
    public static CartAdaptor cartAdaptor;
    private TextView totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        //Loading Dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading Dialog

        continueBtn = view.findViewById(R.id.cart_continue_btn);
        totalAmount = view.findViewById(R.id.total_cart_amount);

        cartItemRecylerView = view.findViewById(R.id.recyler_cart_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemRecylerView.setLayoutManager(layoutManager);



       // List<CartItemModel> cartItemModelList = new ArrayList<>();
       // cartItemModelList.add(new CartItemModel(0, R.drawable.banner, "Samgung Glaxy", 2, "Rs. 49999/-", "Rs. 59999/-", 1, 0, 0));

      //  cartItemModelList.add(new CartItemModel(1, "Price (3 items)", "RS. 169999/-", "Free", "Rs. 169999/-", "Rs. 59999/-"));

        cartAdaptor = new CartAdaptor(DBqueries.cartItemModelList, totalAmount, true);
        cartItemRecylerView.setAdapter(cartAdaptor);
        cartAdaptor.notifyDataSetChanged();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart = true;

                for (int x = 0; x < DBqueries.cartItemModelList.size(); x++){
                    CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                    if (cartItemModel.isInStock()){
                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                loadingDialog.show();
                if (DBqueries.addressesModelList.size() == 0) {
                    DBqueries.loadAddress(getContext(), loadingDialog, true);
                }else {
                    Intent deilveryIntent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(deilveryIntent);

                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdaptor.notifyDataSetChanged();
//////////////////
        if (DBqueries.rewardModelList.size() == 0){
            loadingDialog.show();
            DBqueries.loadReward(getContext(), loadingDialog, false);
        }
////////////////////////////

        if (DBqueries.cartItemModelList.size() == 0){
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(), loadingDialog, true, new TextView(getContext()), totalAmount);
        }else {
            if (DBqueries.cartItemModelList.get(DBqueries.cartItemModelList.size()-1).getType() == CartItemModel.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (CartItemModel cartItemModel : DBqueries.cartItemModelList){
            if (!TextUtils.isEmpty(cartItemModel.getSelectedCoupenId())){
                for (RewardModel rewardModle : DBqueries.rewardModelList) {
                    if (rewardModle.getCoupenId().equals(cartItemModel.getSelectedCoupenId())){
                        rewardModle.setAlreadyUsed(false);
                    }
                }
                cartItemModel.setSelectedCoupenId(null);
                if (MyRewardsFragment.myRewardsAdaptor !=null) {
                    MyRewardsFragment.myRewardsAdaptor.notifyDataSetChanged();
                }
            }
        }
    }
}
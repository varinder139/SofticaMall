package com.webservice.softicamall;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdaptor extends RecyclerView.Adapter<MyRewardsAdaptor.ViewHolder> {
    private List<RewardModel> rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerView;
    private LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedCoupenTitle;
    private TextView selectedCoupenExpiryDate;
    private TextView selectedoupenBody;
    private TextView disountedPrie;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModelList;

    public MyRewardsAdaptor(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }

    public MyRewardsAdaptor(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen,
                            String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView disountedPrie) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedoupenBody = coupenBody;
        this.disountedPrie = disountedPrie;
    }
    public MyRewardsAdaptor(int cartItemPosition, List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen,
                            String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody, TextView disountedPrie, List<CartItemModel> cartItemModelList) {
        this.cartItemPosition = cartItemPosition;
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedoupenBody = coupenBody;
        this.disountedPrie = disountedPrie;
        this.cartItemModelList = cartItemModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (useMiniLayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getDiscount();
        boolean alreadyUse = rewardModelList.get(position).isAlreadyUsed();
        String coupenId = rewardModelList.get(position).getCoupenId();

        holder.seData(coupenId,type, validity, body, upperLimit, lowerLimit,discORamt,alreadyUse);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cpnTitle, cpnExpiryDate, cpnBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cpnTitle = itemView.findViewById(R.id.rs_coupen_title);
            cpnExpiryDate = itemView.findViewById(R.id.rs_rewards_valididty);
            cpnBody = itemView.findViewById(R.id.rs_rewards_body);
        }

        private void seData(final String coupenId,final String type, final Date validity, final String body, final String upperLimit, final String lowerLimit, final String discORamt, final boolean alreadyUsed) {
            if (type.equals("Discount")){
                cpnTitle.setText(type);
            }else {
                cpnTitle.setText("Flat Rs. "+discORamt+" OFF");
            }

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/YYYY");
            if (alreadyUsed){
                cpnExpiryDate.setText("Already Used");
                cpnExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cpnBody.setTextColor(Color.parseColor("#50ffffff"));
                cpnTitle.setTextColor(Color.parseColor("#50ffffff"));
            }else {
                cpnBody.setTextColor(Color.parseColor("#ffffff"));
                cpnTitle.setTextColor(Color.parseColor("#ffffff"));
                cpnExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPurple));
                cpnExpiryDate.setText("till "+simpleDateFormat.format(validity));

            }


            cpnBody.setText(body);


            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!alreadyUsed){
                            selectedCoupenTitle.setText(type);
                            selectedCoupenExpiryDate.setText(simpleDateFormat.format(validity));
                            selectedoupenBody.setText(body);
                            if (Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)) {
                                if (type.equals("Discount")) {
                                    Long discountAmount = Long.valueOf(productOriginalPrice) * Long.valueOf(discORamt) / 100;
                                    disountedPrie.setText("Rs. " + String.valueOf(Long.valueOf(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    disountedPrie.setText("Rs. " + String.valueOf(Long.valueOf(productOriginalPrice) - Long.valueOf(discORamt)) + "/-");
                                }

                                if (cartItemPosition!= -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                                }
                            } else {
                                if (cartItemPosition != -1) {
                                    cartItemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                                }
                                disountedPrie.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Sorry! Product dose not matches the coupen terms", Toast.LENGTH_SHORT).show();
                            }

                            if (coupensRecyclerView.getVisibility() == View.GONE) {
                                coupensRecyclerView.setVisibility(View.VISIBLE);

                                selectedCoupen.setVisibility(View.GONE);
                            } else {
                                coupensRecyclerView.setVisibility(View.GONE);
                                selectedCoupen.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }
    }
}

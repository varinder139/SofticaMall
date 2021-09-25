package com.webservice.softicamall;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdaptor extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdaptor(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {

        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(view);

            case CartItemModel.TOTAL_AMOUNT:

                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(view2);

            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resoures = cartItemModelList.get(position).getProdutImage();
                String tittle = cartItemModelList.get(position).getProdutTitle();
                Long freeoupen = cartItemModelList.get(position).getFreeCoupn();
                String productPrie = cartItemModelList.get(position).getProductPrie();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offerApplied = cartItemModelList.get(position).getOfferApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQty = cartItemModelList.get(position).getProdutQty();
                Long maxQuantity = cartItemModelList.get(position).getMaxQty();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                Long stockQty = cartItemModelList.get(position).getStockQty();
                boolean COD =cartItemModelList.get(position).isCOD();

                ((CartItemViewHolder) holder).setItemDEtails(productID, resoures, tittle, freeoupen, productPrie, cuttedPrice, offerApplied, position, inStock, String.valueOf(productQty), maxQuantity, qtyError, qtyIds, stockQty, COD);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProdutQty()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrie())*quantity;
                        } else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())*quantity;
                        }

                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice())){
                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartItemModelList.get(x).getProductPrie() )) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrie()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice() )) * quantity;
                            }

                        }else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrie()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice() )) * quantity;
                            }

                        }
                    }
                }
                if (totalItemPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }
                //String totalItems = cartItemModelList.get(position).getTotalItems();
                //String totaItemPrice = cartItemModelList.get(position).getTotalItemPrie();
                // String deliveryPrice = cartItemModelList.get(position).getDeliveryPrice();
                // String totalAmount = cartItemModelList.get(position).getTotalAmount();
                // String savedAmount = cartItemModelList.get(position).getSavedAmount();

                cartItemModelList.get(position).setTotalItem(totalItems);
                cartItemModelList.get(position).setTotalItemsPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrie(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);
                ((CartTotalAmountViewHolder) holder).setTotaAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);
                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage, freeCoupenIcon;
        private TextView produtTile, freeCoupen, productPrice, cuttedPrice, offerAupplied, coupenApplied, productQty;
        private LinearLayout deleteBtn;
        private LinearLayout coupenRedemptionLayout;
        private TextView coupenRedemptionBody;
        private Button redeemBtn;
        private ImageView codIndicator;

        ////coupenDialog
        private TextView coupenTitle, coupenExpiryDate, coupenBody;
        private TextView discountedPrice;
        private TextView originalPrice;
        private RecyclerView coupenReyclerView;
        private LinearLayout seletedCoupen;
        ////coupenDialog
        private Button removeCpnBtn, applyCpnBtn;
        private LinearLayout applyORremoveBtnContainer;
        private TextView footerText;
        private String productOriginalPrice;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image1);
            freeCoupenIcon = itemView.findViewById(R.id.free_coupen_icon);
            produtTile = itemView.findViewById(R.id.cart_product_title);
            freeCoupen = itemView.findViewById(R.id.tv_free_coupen);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offerAupplied = itemView.findViewById(R.id.offer_appled);
            coupenApplied = itemView.findViewById(R.id.coupn_applied);
            productQty = itemView.findViewById(R.id.produt_qty);
            redeemBtn = itemView.findViewById(R.id.coupen_redemption_btn);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            coupenRedemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_coupen_redemption);
            codIndicator = itemView.findViewById(R.id.cod_indicatior);

        }

        private void setItemDEtails(final String productID, String resoures, String title, Long freeCoupensNo, final String productPrieText, String cuttedPrieText, Long offersAppliedNo, final int position, boolean inStock, final String qty, final Long maxQty, boolean qtyerror, final List<String> qtyIds, final long stockQty, boolean cod) {
            // productImage.setImageResource(resoures);
            Glide.with(itemView.getContext()).load(resoures).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(productImage);
            produtTile.setText(title);

            final Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
            checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
            checkCoupenPriceDialog.setCancelable(false);
            checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (cod){
                codIndicator.setVisibility(View.VISIBLE);
            }else {
                codIndicator.setVisibility(View.INVISIBLE);
            }

            if (inStock) {
                if (freeCoupensNo > 0) {

                    freeCoupenIcon.setVisibility(View.VISIBLE);
                    freeCoupen.setVisibility(View.VISIBLE);

                    if (freeCoupensNo == 1) {
                        freeCoupen.setText("free " + freeCoupensNo + " Coupen");
                    } else {
                        freeCoupen.setText("free " + freeCoupensNo + " Coupens");
                    }
                } else {
                    freeCoupenIcon.setVisibility(View.INVISIBLE);
                    freeCoupen.setVisibility(View.INVISIBLE);
                }

                productPrice.setText("Rs. " + productPrieText + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs. " + cuttedPrieText + "/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);


                ///////coupen dialog

                ImageView toggleReyclerView = checkCoupenPriceDialog.findViewById(R.id.toggal_recycler_view_btn);

                coupenReyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_reylerview);
                seletedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen_container);
                coupenTitle = checkCoupenPriceDialog.findViewById(R.id.rs_coupen_title);
                coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.rs_rewards_valididty);
                coupenBody = checkCoupenPriceDialog.findViewById(R.id.rs_rewards_body);

                footerText = checkCoupenPriceDialog.findViewById(R.id.footer_text);
                applyORremoveBtnContainer = checkCoupenPriceDialog.findViewById(R.id.apply_or_remove_container);
                removeCpnBtn = checkCoupenPriceDialog.findViewById(R.id.remove_button);
                applyCpnBtn = checkCoupenPriceDialog.findViewById(R.id.apply_button);

                footerText.setVisibility(View.GONE);
                applyORremoveBtnContainer.setVisibility(View.VISIBLE);

                originalPrice = checkCoupenPriceDialog.findViewById(R.id.tv_original_price);
                discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);



                LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                coupenReyclerView.setLayoutManager(layoutManager);
                /////cut from here

                ///for coupen dialog
                originalPrice.setText(productPrice.getText());
                productOriginalPrice = productPrieText;
                MyRewardsAdaptor myRewardsAdaptor = new MyRewardsAdaptor(position, DBqueries.rewardModelList, true, coupenReyclerView, seletedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice, cartItemModelList);
                coupenReyclerView.setAdapter(myRewardsAdaptor);
                myRewardsAdaptor.notifyDataSetChanged();
                ////for coupen dialod

                applyCpnBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                            for (RewardModel rewardModle : DBqueries.rewardModelList) {
                                if (rewardModle.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                    rewardModle.setAlreadyUsed(true);
                                    coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewad_gradient_background));
                                    coupenRedemptionBody.setText(rewardModle.getCoupenBody());
                                    redeemBtn.setText("Coupen");
                                }
                            }
                            coupenApplied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(4, discountedPrice.getText().length() - 2));
                            productPrice.setText(discountedPrice.getText());
                            String offerDiscountedAmount = String.valueOf(Long.valueOf(productPrieText) - Long.valueOf(discountedPrice.getText().toString().substring(4, discountedPrice.getText().length() - 2)));
                            coupenApplied.setText("Coupen applied - Rs."+offerDiscountedAmount+"/-");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            checkCoupenPriceDialog.dismiss();
                        }
                    }
                });

                removeCpnBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (RewardModel rewardModle : DBqueries.rewardModelList) {
                            if (rewardModle.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())){
                                rewardModle.setAlreadyUsed(false);
                            }
                        }
                        coupenTitle.setText("Coupen");
                        coupenExpiryDate.setText("validity");
                        coupenBody.setText("Tap the icon on the top right corner to select your coupen.");
                        coupenApplied.setVisibility(View.INVISIBLE);
                        coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenRed));
                        coupenRedemptionBody.setText("Apply your coupen here");
                        redeemBtn.setText("Redeem");

                        cartItemModelList.get(position).setSelectedCoupenId(null);
                        productPrice.setText("Rs."+productPrieText+"/-");
                        notifyItemChanged(cartItemModelList.size() - 1);
                        checkCoupenPriceDialog.dismiss();

                    }
                });

                toggleReyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogRecyclerView();
                    }
                });



               //////paste here
                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                    for (RewardModel rewardModle : DBqueries.rewardModelList) {
                        if (rewardModle.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.rewad_gradient_background));
                            coupenRedemptionBody.setText(rewardModle.getCoupenBody());
                            redeemBtn.setText("Coupen");

                            coupenBody.setText(rewardModle.getCoupenBody());
                            if (rewardModle.getType().equals("Discount")){
                                coupenTitle.setText(rewardModle.getType());
                            }else {
                                coupenTitle.setText("Flat Rs. "+rewardModle.getDiscount()+" OFF");
                            }
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/YYYY");
                            coupenExpiryDate.setText("till "+simpleDateFormat.format(rewardModle.getTimestamp()));

                        }
                    }
                    discountedPrice.setText("Rs. "+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                    coupenApplied.setVisibility(View.VISIBLE);
                    productPrice.setText("Rs. "+cartItemModelList.get(position).getDiscountedPrice()+"/-");
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(productPrieText) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                    coupenApplied.setText("Coupen applied - Rs."+offerDiscountedAmount+"/-");

                }else {
                    coupenApplied.setVisibility(View.INVISIBLE);
                    coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenRed));
                    coupenRedemptionBody.setText("Apply your coupen here");
                    redeemBtn.setText("Redeem");

                }
                /////paste here

                ////coupen dialog

                productQty.setText("Qty: " + qty);
                if (!showDeleteBtn) {
                    if (qtyerror) {
                        productQty.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        productQty.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));

                    } else {
                        productQty.setTextColor(itemView.getContext().getResources().getColor(R.color.blackcolor));
                        productQty.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.blackcolor)));
                    }
                }

                productQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText qtyNo = quantityDialog.findViewById(R.id.qty_no);
                        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
                        qtyNo.setHint("Max " + String.valueOf(maxQty));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                quantityDialog.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(qtyNo.getText())) {
                                    if (Long.parseLong(qtyNo.getText().toString()) <= maxQty && Long.parseLong(qtyNo.getText().toString()) != 0) {

                                        if (itemView.getContext() instanceof MainActivity2) {
                                            cartItemModelList.get(position).setProdutQty(Long.parseLong(qtyNo.getText().toString()));

                                        } else {

                                            if (DeliveryActivity.fromCart) {
                                                cartItemModelList.get(position).setProdutQty(Long.parseLong(qtyNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModelList.get(position).setProdutQty(Long.parseLong(qtyNo.getText().toString()));
                                            }
                                        }
                                        productQty.setText("Qty: " + qtyNo.getText());
                                        notifyItemChanged(cartItemModelList.size() - 1);

                                        if (!showDeleteBtn) {
                                            DeliveryActivity.loadingDialog.show();
                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(false);

                                            final int initialQty = Integer.parseInt(qty);
                                            final int finalQty = Integer.parseInt(qtyNo.getText().toString()) - Integer.parseInt(qty);

                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                            if (finalQty > initialQty) {
                                                //////////////////paste here
                                                for (int y = 0; y < finalQty - initialQty; y++) {
                                                    final String qtyDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());

                                                    final int finalY = y;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyDocumentName).set(timeStamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.add(qtyDocumentName);

                                                                    if (finalY + 1 == finalQty - initialQty) {

                                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            List<String> serverQty = new ArrayList<>();

                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                                serverQty.add(queryDocumentSnapshot.getId());
                                                                                            }

                                                                                            long availableQty = 0;
                                                                                            // boolean noLongerAvialable = true;

                                                                                            for (String qtyId : qtyIds) {
                                                                                                if (!serverQty.contains(qtyId)) {
                                                                                                    DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                                    DeliveryActivity.cartItemModelList.get(position).setMaxQty(availableQty);
                                                                                                    Toast.makeText(itemView.getContext(), "All Product may be not avialable in required qty! SORRY!", Toast.LENGTH_SHORT).show();


                                                                                                } else {
                                                                                                    availableQty++;
                                                                                                }
                                                                                            }
                                                                                            DeliveryActivity.cartAdaptor.notifyDataSetChanged();

                                                                                        } else {
                                                                                            ///error handle
                                                                                            String error = task.getException().getMessage();
                                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                                    }
                                                                                });


                                                                    }
                                                                }
                                                            });
                                                }
                                            } else if (initialQty > finalQty) {
                                                for (int x = 0; x < initialQty - finalQty; x++) {
                                                    final String qtyId = qtyIds.get(qtyIds.size() - 1 - x);
                                                    // final int finalX = x;
                                                    final int finalX = x;
                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.remove(qtyId);
                                                                    DeliveryActivity.cartAdaptor.notifyDataSetChanged();
                                                                    if (finalX + 1 == initialQty - finalQty){
                                                                        DeliveryActivity.loadingDialog.dismiss();
                                                                    }
                                                                }
                                                            });
                                                }

                                            }

                                            /////////////////////////////////paste here

                                        }
                                        // quantityDialog.dismiss();
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantitty :" + maxQty.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                quantityDialog.dismiss();
                            }
                        });

                        quantityDialog.show();
                    }
                });

                if (offersAppliedNo > 0) {
                    offerAupplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(cuttedPrieText) - Long.valueOf(productPrieText));
                    offerAupplied.setText("Offer Applied - Rs."+offerDiscountedAmount+"/-");
                } else {
                    offerAupplied.setVisibility(View.INVISIBLE);
                }


            } else {
                productPrice.setText("Out Of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);
                freeCoupen.setVisibility(View.INVISIBLE);
                productQty.setVisibility(View.INVISIBLE);
                coupenApplied.setVisibility(View.GONE);
                offerAupplied.setVisibility(View.GONE);
                freeCoupenIcon.setVisibility(View.INVISIBLE);

            }


            if (showDeleteBtn) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }


            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RewardModel rewardModle : DBqueries.rewardModelList) {
                        if (rewardModle.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            rewardModle.setAlreadyUsed(false);
                        }
                    }
                    checkCoupenPriceDialog.show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModle : DBqueries.rewardModelList) {
                            if (rewardModle.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())){
                                rewardModle.setAlreadyUsed(false);
                            }
                        }
                        cartItemModelList.get(position).setSelectedCoupenId(null);
                    }

                        if (!ProductDetailActivity.running_cart_query) {
                        ProductDetailActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });

        }
        private void showDialogRecyclerView() {
            if (coupenReyclerView.getVisibility() == View.GONE) {
                coupenReyclerView.setVisibility(View.VISIBLE);

                seletedCoupen.setVisibility(View.GONE);
            } else {
                coupenReyclerView.setVisibility(View.GONE);
                seletedCoupen.setVisibility(View.VISIBLE);
            }

        }

    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {

        private TextView totalItems, totalItemPrice, devieryPrie, totalAmount, savedAmount;


        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            devieryPrie = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);

        }

        private void setTotaAmount(int tottalItemText, int totalItemPriceText, String deliveryPrieText, int totalAmountText, int savedAmountText) {
            totalAmount.setText("Price(" + tottalItemText + " items)");
            totalItemPrice.setText("Rs. " + totalItemPriceText + "/-");
            if (deliveryPrieText.equals("FREE")) {
                devieryPrie.setText(deliveryPrieText);
            } else {
                devieryPrie.setText("Rs. " + deliveryPrieText + "/-");
            }
            totalAmount.setText("Rs. " + totalAmountText + "/-");
            cartTotalAmount.setText("Rs. " + totalAmountText + "/-");
            savedAmount.setText("You saved Rs. " + savedAmountText + "/- on this order.");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                if(DeliveryActivity.fromCart) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                if (showDeleteBtn){
                    cartItemModelList.remove(cartItemModelList.size() - 1);

                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

}

package com.webservice.softicamall;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishListAdaptor extends RecyclerView.Adapter<WishListAdaptor.ViewHolder> {

    private boolean fromSearch;
    private List<WishListModel> wishListModelList;
    private Boolean wishList;
    private int lastPosition = -1;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishListAdaptor(List<WishListModel> wishListModelList, Boolean wishList) {
        this.wishListModelList = wishListModelList;
        this.wishList = wishList;
    }

    public List<WishListModel> getWishListModelList() {
        return wishListModelList;
    }

    public void setWishListModelList(List<WishListModel> wishListModelList) {
        this.wishListModelList = wishListModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = wishListModelList.get(position).getProductId();
        String resoures = wishListModelList.get(position).getProductImage();
        String tittle = wishListModelList.get(position).getProductTitle();
        long freCoupen = wishListModelList.get(position).getFreeCoupen();
        String rating = wishListModelList.get(position).getRating();
        long totalRating = wishListModelList.get(position).getTotalRating();
        String produtPrice = wishListModelList.get(position).getProdutPrie();
        String cuttedPrice = wishListModelList.get(position).getCuttedPrice();
        boolean paymentMethod = wishListModelList.get(position).isCOD();
        boolean inStok = wishListModelList.get(position).isInStok();

        holder.setData(productId, resoures, tittle, freCoupen, rating, totalRating, produtPrice, cuttedPrice, paymentMethod, position, inStok);

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView produtImage;
        private TextView prodtTitle;
        private TextView freeCpn;
        private ImageView coupIcon;
        private TextView rating, totalRating;
        private View priceutt;
        private TextView produtPrie, cuttedPrice, paymentMethod;
        private ImageButton deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            produtImage = itemView.findViewById(R.id.wl_produt_image);
            prodtTitle = itemView.findViewById(R.id.wl_produt_title);
            freeCpn = itemView.findViewById(R.id.wl_free_copn);
            coupIcon = itemView.findViewById(R.id.wl_coupn_icon);
            rating = itemView.findViewById(R.id.wl_tv_produt_rating_miniview);
            totalRating = itemView.findViewById(R.id.wl_total_rating);
            priceutt = itemView.findViewById(R.id.wl_price_cut);
            produtPrie = itemView.findViewById(R.id.wl_product_price);
            cuttedPrice = itemView.findViewById(R.id.wl_cutted_price);
            paymentMethod = itemView.findViewById(R.id.wl_payment_method);
            deleteBtn = itemView.findViewById(R.id.wl_delete_btn);
        }

        private void setData(final String productId, String resources, String title, long freeCoupenNo, String averageRate, long totalRatingNo, String price, String cuttedpriceValue, boolean paymethod, final int index, boolean inStock) {
            // produtImage.setImageResource(resources);
            Glide.with(itemView.getContext()).load(resources).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(produtImage);
            prodtTitle.setText(title);

            if (freeCoupenNo != 0 && inStock) {
                coupIcon.setVisibility(View.VISIBLE);
                if (freeCoupenNo == 1) {
                    freeCpn.setText("free " + freeCoupenNo + " coupen");
                } else {
                    freeCpn.setText("free " + freeCoupenNo + " coupens");
                }
            } else {
                coupIcon.setVisibility(View.INVISIBLE);
                freeCpn.setVisibility(View.INVISIBLE);
            }

            LinearLayout linearLayout = (LinearLayout) rating.getParent();
            if (inStock){

                rating.setVisibility(View.VISIBLE);
                totalRating.setVisibility(View.VISIBLE);
                produtPrie.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                rating.setText(averageRate);
                totalRating.setText("("+totalRatingNo + ")ratings");
                produtPrie.setText("RS. "+price+"/-");
                cuttedPrice.setText("Rs. "+cuttedpriceValue+"/-");
                if (paymethod) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            } else {
                rating.setVisibility(View.INVISIBLE);
                totalRating.setVisibility(View.INVISIBLE);
                produtPrie.setText("Out of Stock");
                produtPrie.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
            }



            if (wishList) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //deleteBtn.setEnabled(false);
                    if (!ProductDetailActivity.running_wishList_query) {
                        ProductDetailActivity.running_wishList_query = true;
                        DBqueries.removeFromWishList(index, itemView.getContext());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fromSearch){
                        ProductDetailActivity.fromSearh = true;
                    }
                    Intent intent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    intent.putExtra("PRODUCT_ID", productId);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}

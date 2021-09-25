package com.webservice.softicamall;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdaptor extends RecyclerView.Adapter<HorizontalProductScrollAdaptor.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizontalProductScrollAdaptor(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_sroll_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdaptor.ViewHolder holder, int position) {
        String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String description = horizontalProductScrollModelList.get(position).getProductDscription();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();

        holder.setData(productId, resource, title, description, price);



    }

    @Override
    public int getItemCount() {
        if (horizontalProductScrollModelList.size() > 8){
            return 8;
        }else {
            return horizontalProductScrollModelList.size();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productIamge;
        private TextView productTitle, productDescription, produtPrie;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            productIamge = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_title);
            productDescription = itemView.findViewById(R.id.h_s_product_description);
            produtPrie = itemView.findViewById(R.id.h_s_product_prie);


        }
        private void setData(final String productID, String resources, String title, String description, String prie){
            //productIamge.setImageResource(resources);
            Glide.with(itemView.getContext()).load(resources).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(productIamge);
            productTitle.setText(title);
            productDescription.setText(description);
            produtPrie.setText("Rs. "+prie+"/-");


            if (!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent productDetailintent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                        productDetailintent.putExtra("PRODUCT_ID", productID);
                        itemView.getContext().startActivity(productDetailintent);
                    }
                });
            }
        }

    }
}

package com.webservice.softicamall;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProdutLayoutAdaptor extends BaseAdapter {

    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProdutLayoutAdaptor(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {
        View view;
        if (convertView == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_sroll_item_layout, null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            ///set on clik listner
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productDetailsIntent = new Intent(viewGroup.getContext(), ProductDetailActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID", horizontalProductScrollModelList.get(i).getProductID());
                    viewGroup.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.h_s_product_image);
            TextView productTile = view.findViewById(R.id.h_s_product_title);
            TextView productDescription = view.findViewById(R.id.h_s_product_description);
            TextView productPrice = view.findViewById(R.id.h_s_product_prie);

            Glide.with(viewGroup.getContext()).load(horizontalProductScrollModelList.get(i).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(productImage);
            productTile.setText(horizontalProductScrollModelList.get(i).getProductTitle());
            productDescription.setText(horizontalProductScrollModelList.get(i).getProductDscription());
            productPrice.setText("Rs. "+horizontalProductScrollModelList.get(i).getProductPrice()+"/-");
        }else {
            view = convertView;

        }

        return view;
    }
}

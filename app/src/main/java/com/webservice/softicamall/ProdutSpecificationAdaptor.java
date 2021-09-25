package com.webservice.softicamall;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProdutSpecificationAdaptor extends RecyclerView.Adapter<ProdutSpecificationAdaptor.ViewHolder> {

    private List<ProductSpecificationModle> productSpecificationModleList;

    public ProdutSpecificationAdaptor(List<ProductSpecificationModle> productSpecificationModleList) {
        this.productSpecificationModleList = productSpecificationModleList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModleList.get(position).getType()) {
            case 0:
                return ProductSpecificationModle.speificationTitle;
            case 1:
                return ProductSpecificationModle.speificationBody;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductSpecificationModle.speificationTitle:
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(8, parent.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case ProductSpecificationModle.speificationBody:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout, parent, false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (productSpecificationModleList.get(position).getType()) {
            case ProductSpecificationModle.speificationTitle:
                  holder.setTitle(productSpecificationModleList.get(position).getTitle());
                break;
            case ProductSpecificationModle.speificationBody:
                String featureTitle = productSpecificationModleList.get(position).getFeatureName();
                String featureDetail = productSpecificationModleList.get(position).getFeatureValue();
                holder.setFeatures(featureTitle, featureDetail);
                break;
            default:
                return;
        }


    }

    @Override
    public int getItemCount() {
        return productSpecificationModleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView featureNmae;
        private TextView featureValue;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void setFeatures(String featureTitle, String featureDetail) {
            featureNmae = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);

            featureNmae.setText(featureTitle);
            featureValue.setText(featureDetail);
        }


    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

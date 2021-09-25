package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductDetailAdaptor extends FragmentPagerAdapter {

    public ProductDetailAdaptor(@NonNull FragmentManager fm, int totaltab, String productDescription, String produtOtherDetails, List<ProductSpecificationModle> productSpecificationModleList) {
        super(fm);
        this.totaltab = totaltab;
        this.productDescription = productDescription;
        this.produtOtherDetails = produtOtherDetails;
        this.productSpecificationModleList = productSpecificationModleList;
    }

    private int totaltab;
    private String productDescription, produtOtherDetails;
    private List<ProductSpecificationModle> productSpecificationModleList;



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProdutDescriptionFragment produtDescriptionFragment1 = new ProdutDescriptionFragment();
                produtDescriptionFragment1.body = productDescription;
                return produtDescriptionFragment1;

            case 1:
                ProductSpeificationFragment productSpeificationFragment = new ProductSpeificationFragment();
                productSpeificationFragment.productSpecificationModleList = productSpecificationModleList;
                return productSpeificationFragment;
            case 2:
                ProdutDescriptionFragment produtDescriptionFragment2 = new ProdutDescriptionFragment();
                produtDescriptionFragment2.body = produtOtherDetails;
                return produtDescriptionFragment2;

            default:

                return null;
        }
    }

    @Override
    public int getCount() {
        return totaltab;
    }
}

package com.webservice.softicamall;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProductSpeificationFragment extends Fragment {


    public ProductSpeificationFragment() {
        // Required empty public constructor
    }

    private RecyclerView productSpeciReylerView;
    public List<ProductSpecificationModle> productSpecificationModleList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_speification, container, false);

        productSpeciReylerView = view.findViewById(R.id.product_speifiation_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productSpeciReylerView.setLayoutManager(linearLayoutManager);



        /*
        productSpecificationModleList.add(new ProductSpecificationModle(0,"General"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));

        productSpecificationModleList.add(new ProductSpecificationModle(0,"Deisplay"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));

        productSpecificationModleList.add(new ProductSpecificationModle(0,"General"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));

        productSpecificationModleList.add(new ProductSpecificationModle(0,"Deisplay"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        productSpecificationModleList.add(new ProductSpecificationModle(1,"RAM", "4GB"));
        */


        ProdutSpecificationAdaptor produtSpecificationAdaptor = new ProdutSpecificationAdaptor(productSpecificationModleList);
        productSpeciReylerView.setAdapter(produtSpecificationAdaptor);
        produtSpecificationAdaptor.notifyDataSetChanged();

        return view;
    }
}
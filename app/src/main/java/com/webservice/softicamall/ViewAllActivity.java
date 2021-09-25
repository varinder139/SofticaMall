package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private GridView gridView;
    public static List<WishListModel> wishListModelList;
    public static List<HorizontalProductScrollModel> horizontalProductScrollModelList; //= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.vl_reycler_view);
        gridView = findViewById(R.id.vl_grid_view);

        int layout_code = getIntent().getIntExtra("layout_code", -1);

        if (layout_code == 0) {

            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);


            WishListAdaptor adaptor = new WishListAdaptor(wishListModelList, false);
            recyclerView.setAdapter(adaptor);
            adaptor.notifyDataSetChanged();

        } else if (layout_code == 1) {

            gridView.setVisibility(View.VISIBLE);


            GridProdutLayoutAdaptor gridProdutLayoutAdaptor = new GridProdutLayoutAdaptor(horizontalProductScrollModelList);
            gridView.setAdapter(gridProdutLayoutAdaptor);

        }

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
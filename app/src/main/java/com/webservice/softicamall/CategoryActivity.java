package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.webservice.softicamall.DBqueries.lists;
import static com.webservice.softicamall.DBqueries.loadFragmentData;
import static com.webservice.softicamall.DBqueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    HomePageAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String titile = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(titile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///home page Fake List
        List<SliderModel> sliderModelList = new ArrayList<>();
        sliderModelList.add(new SliderModel("null", "#ffffff"));
        sliderModelList.add(new SliderModel("null", "#ffffff"));
        sliderModelList.add(new SliderModel("null", "#ffffff"));
        sliderModelList.add(new SliderModel("null", "#ffffff"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#ffffff", horizontalProductScrollModelFakeList, new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#ffffff", horizontalProductScrollModelFakeList));

        ///home page Fake List

        categoryRecyclerView = findViewById(R.id.category_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getApplicationContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutManager);

        //List<HomePageModel> homePageModelList = new ArrayList<>();

        adaptor = new HomePageAdaptor(homePageModelFakeList);


        int listPosition = 0;
        for(int x = 0; x < loadedCategoriesNames.size(); x++){
            if (loadedCategoriesNames.get(x).equals(titile.toUpperCase())){
                 listPosition = x;
            }
        }
        if (listPosition == 0){
            loadedCategoriesNames.add(titile.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());

            loadFragmentData(categoryRecyclerView, this,loadedCategoriesNames.size() - 1, titile);
        }else {
            adaptor = new HomePageAdaptor(lists.get(listPosition));
        }
        categoryRecyclerView.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search_icon){
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
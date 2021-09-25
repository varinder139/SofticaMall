package com.webservice.softicamall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.webservice.softicamall.MainActivity2.showCart;
import static com.webservice.softicamall.RegisterActivity.setSignUpFragment;


public class ProductDetailActivity extends AppCompatActivity {
    public static boolean running_wishList_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailAtivity;

    private TextView produtTitle, averageRatingMiniView, tottalRatingMiniview, productPrice, cuttedPrice;
    private ImageView codIndiatior;
    private TextView tvCODIndiator;

    private TextView rewardTitle, rewardBody;

    ////product discription
    private ConstraintLayout productDeatilsOnlyContainer, productDeatailsTabsContainer;

    private ViewPager productDetailViewPager;
    private TabLayout produtDeatilTabLayout;
    private TextView productOnlyDescriptionBody;

    private String productDescription;
    private String productOtherDetails;

    public static boolean fromSearh = false;

    //public static int tabPsition = -1;

    private List<ProductSpecificationModle> productSpecificationModleList = new ArrayList<>();
    ////product discription

    private ViewPager prductImagesViewPagers;
    private TabLayout viewpagerIndiator;
    public static FloatingActionButton addToWishListBtn;
    public static boolean ALREADY_ADDED_TO_WISHlIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    ///////Rating Layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRating;
    private LinearLayout ratingNumbersContainer;
    private TextView totalRatingFigure;
    private LinearLayout ratingsProgressbarContainer;
    private TextView averageRating;

    /////Rating Layout

    private LinearLayout coupenRedemptionLayout;
    private Button buyNow, coupenRedeemBtn;
    private LinearLayout addToCartBtn;

    public static MenuItem cartItem;

    private FirebaseFirestore firebaseFirestore;

    ////coupenDialog
    private TextView coupenTitle, coupenExpiryDate, coupenBody;
    private TextView discountedPrice;
    private TextView originalPrice;
    private RecyclerView coupenReyclerView;
    private LinearLayout seletedCoupen;
    ////coupenDialog

    /////sign in dialog
    private Dialog signInDialog;
    ////sign in dialog
    //Loading Dialog
    private Dialog loadingDialog;
    //Loading Dialog

    private FirebaseUser currentUser;
    public static String productID;

    private DocumentSnapshot documentSnapshot;
    private TextView badgeCount;
    private boolean inStock = false;
    private String productOriginalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        prductImagesViewPagers = findViewById(R.id.product_images_viewpager);
        viewpagerIndiator = findViewById(R.id.viewpager_indicator);
        addToWishListBtn = findViewById(R.id.add_to_wish_list_btn);
        productDetailViewPager = findViewById(R.id.product_details_viewpager);
        produtDeatilTabLayout = findViewById(R.id.product_detail_tab_layout);
        buyNow = findViewById(R.id.buy_naow_btn);
        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        produtTitle = findViewById(R.id.produt_title);
        averageRatingMiniView = findViewById(R.id.wl_tv_produt_rating_miniview);
        tottalRatingMiniview = findViewById(R.id.tottal_rating_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        codIndiatior = findViewById(R.id.cod_indicator_imageview);
        tvCODIndiator = findViewById(R.id.tv_cod_indicator);
        rewardTitle = findViewById(R.id.reward_titile);
        rewardBody = findViewById(R.id.reward_body);
        productDeatailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDeatilsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRating = findViewById(R.id.total_ratings);
        ratingNumbersContainer = findViewById(R.id.ratings_numbers_containers);
        totalRatingFigure = findViewById(R.id.total_rating_figure);
        ratingsProgressbarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        coupenRedemptionLayout = findViewById(R.id.coupen_redemption_layout);
        initialRating = -1;

        //Loading Dialog
        loadingDialog = new Dialog(ProductDetailActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading Dialog

        ///////coupen dialog
        final Dialog checkCoupenPriceDialog = new Dialog(ProductDetailActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleReyclerView = checkCoupenPriceDialog.findViewById(R.id.toggal_recycler_view_btn);

        coupenReyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_reylerview);
        seletedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen_container);
        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.rs_coupen_title);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.rs_rewards_valididty);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.rs_rewards_body);


        originalPrice = checkCoupenPriceDialog.findViewById(R.id.tv_original_price);
        discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);



        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupenReyclerView.setLayoutManager(layoutManager);
        /////cut from here

        toggleReyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecyclerView();
            }
        });


        ////coupen dialog


        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    documentSnapshot = task.getResult();
                    /////paste
                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){


                                        for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                            productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                        }
                                        ProductImagesAdaptor productImagesAdaptor = new ProductImagesAdaptor(productImages);
                                        prductImagesViewPagers.setAdapter(productImagesAdaptor);

                                        produtTitle.setText(documentSnapshot.get("product_title").toString());
                                        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                                        tottalRatingMiniview.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                        productPrice.setText("Rs. " + documentSnapshot.get("product_price").toString() + "/-");
                                        productOriginalPrice = documentSnapshot.get("product_price").toString();

                                        ///for coupen dialog
                                        originalPrice.setText(productPrice.getText());
                                        MyRewardsAdaptor myRewardsAdaptor = new MyRewardsAdaptor(DBqueries.rewardModelList, true, coupenReyclerView, seletedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice);
                                        coupenReyclerView.setAdapter(myRewardsAdaptor);
                                        myRewardsAdaptor.notifyDataSetChanged();
                                        ////for coupen dialod

                                        cuttedPrice.setText(documentSnapshot.get("cutted_price").toString() + "/-");

                                        if ((boolean) documentSnapshot.get("COD")) {
                                            codIndiatior.setVisibility(View.VISIBLE);
                                            tvCODIndiator.setVisibility(View.VISIBLE);
                                        } else {
                                            codIndiatior.setVisibility(View.INVISIBLE);
                                            tvCODIndiator.setVisibility(View.INVISIBLE);
                                        }

                                        rewardTitle.setText((long) documentSnapshot.get("free_coupens") + documentSnapshot.get("free_coupen_title").toString());
                                        rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                                        if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                            productDeatailsTabsContainer.setVisibility(View.VISIBLE);
                                            productDeatilsOnlyContainer.setVisibility(View.GONE);
                                            productDescription = documentSnapshot.get("product_description").toString();
                                            // ProductSpeificationFragment.productSpecificationModleList = new ArrayList<>();
                                            productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                productSpecificationModleList.add(new ProductSpecificationModle(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                                    productSpecificationModleList.add(new ProductSpecificationModle(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                                                }
                                            }
                                        } else {
                                            productDeatailsTabsContainer.setVisibility(View.GONE);
                                            productDeatilsOnlyContainer.setVisibility(View.VISIBLE);
                                            productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                                        }
                                        totalRating.setText((long) documentSnapshot.get("total_ratings") + " ratings");

                                        for (int x = 0; x < 5; x++) {
                                            TextView rating = (TextView) ratingNumbersContainer.getChildAt(x);
                                            rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                            ///rating progressbar
                                            ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x);
                                            int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                            progressBar.setMax(maxProgress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                        }

                                        totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                        averageRating.setText(documentSnapshot.get("average_rating").toString());
                                        productDetailViewPager.setAdapter(new ProductDetailAdaptor(getSupportFragmentManager(), produtDeatilTabLayout.getTabCount(), productDescription,
                                                productOtherDetails, productSpecificationModleList));

                                        if (currentUser != null) {
                                            //load rating
                                            if (DBqueries.myRating.size() == 0) {
                                                DBqueries.loadRatingList(ProductDetailActivity.this);
                                            }
                                            //load Reating
                                            ////cartlist Load
                                            if (DBqueries.cartList.size() == 0) {
                                                DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailActivity.this));
                                            }
                                            //cartlist Load
                                            ////wishList Load
                                            if (DBqueries.wishList.size() == 0) {
                                                DBqueries.loadWishList(ProductDetailActivity.this, loadingDialog, false);

                                            }
                                            //wishList Load
                                            if (DBqueries.rewardModelList.size() == 0){
                                                DBqueries.loadReward(ProductDetailActivity.this, loadingDialog, false);
                                              //  loadingDialog.
                                            }
                                            if (DBqueries.cartList.size() == 0 && DBqueries.wishList.size() == 0 && DBqueries.rewardModelList.size() == 0){
                                                loadingDialog.dismiss();
                                            }

                                        } else {
                                            loadingDialog.dismiss();
                                        }
                                        //rating
                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            int index = DBqueries.myRatedIds.indexOf(productID);
                                            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                                            setRating(initialRating);
                                        }
                                        //check cartlist product exit or not
                                        if (DBqueries.cartList.contains(productID)) {
                                            ALREADY_ADDED_TO_CART = true;
                                        } else {
                                            ALREADY_ADDED_TO_CART = false;
                                        }


                                        //check wishlist product exit or not
                                        if (DBqueries.wishList.contains(productID)) {
                                            ALREADY_ADDED_TO_WISHlIST = true;
                                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                        } else {
                                            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                            ALREADY_ADDED_TO_WISHlIST = false;
                                        }
/////////////////////////////////////next paste is here
                                        if (task.getResult().getDocuments().size() < (long)documentSnapshot.get("stock_quantity")){

                                            inStock = true;
                                            buyNow.setVisibility(View.VISIBLE);
                                            ///here add to cart Button
                                            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (currentUser == null) {
                                                        signInDialog.show();
                                                    } else {
                                                        if (!running_cart_query) {
                                                            running_cart_query = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_query = false;
                                                                Toast.makeText(ProductDetailActivity.this, "Alredy Added To Cart", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Map<String, Object> addProduct = new HashMap<>();
                                                                addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                                //addProduct.put("list_size", DBqueries.wishList.size());
                                                                addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));
                                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            if (DBqueries.cartItemModelList.size() != 0) {
                                                                                DBqueries.cartItemModelList.add(0, new CartItemModel(documentSnapshot.getBoolean("COD"), CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                        , (long) 0
                                                                                        , inStock
                                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                                        , (long) documentSnapshot.get("stock_quantity")));
                                                                            }

                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            DBqueries.cartList.add(productID);
                                                                            Toast.makeText(ProductDetailActivity.this, "Added to Cart Successfully!", Toast.LENGTH_SHORT).show();
                                                                            // below mentioned single option for refresh cart notification
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;
                                                                        } else {
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                            ///here add to cart Button
///////////////////////////////////////////////////second paste end here

                                        }
                                        else {
                                            inStock = false;
                                            buyNow.setVisibility(View.GONE);
                                            TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                                            outOfStock.setText("Out Of Stock");
                                            outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                                            outOfStock.setCompoundDrawables(null, null, null, null);
                                        }

                                    }else {
                                        //error
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //////paste
                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewpagerIndiator.setupWithViewPager(prductImagesViewPagers, true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    //addToWishListBtn.setEnabled(false);
                    if (!running_wishList_query) {
                        running_wishList_query = true;
                        if (ALREADY_ADDED_TO_WISHlIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishList(index, ProductDetailActivity.this);
                            //ALREADY_ADDED_TO_WISHlIST = false;
                            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                        } else {
                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            //addProduct.put("list_size", DBqueries.wishList.size());
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));
                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.wishListModelList.size() != 0) {
                                            DBqueries.wishListModelList.add(new WishListModel(productID, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_full_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_rating")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")
                                                    , inStock));
                                        }

                                        ALREADY_ADDED_TO_WISHlIST = true;
                                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetailActivity.this, "Added to Wishlist Successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishList_query = false;
                                }
                            });

                        }
                    }
                }
            }
        });


        productDetailViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(produtDeatilTabLayout));

        produtDeatilTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tabPsition = tab.getPosition();

                prductImagesViewPagers.setCurrentItem(tab.getPosition());
                //productDetailViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ///////Rating Layout
        rateNowContainer = findViewById(R.id.rate_now_container);

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starposition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starposition != initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;
                                setRating(starposition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {
                                    // Map<String, Object> updateRating = new HashMap<>();
                               /* updateRating.put(starposition + 1 + "_star", (long) documentSnapshot.get(starposition + 1 + "_star") + 1);
                                updateRating.put("average_rating", calculateAveragreRating(starposition + 1));
                                updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);  */
                                    TextView oldRating = (TextView) ratingNumbersContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingNumbersContainer.getChildAt(5 - starposition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starposition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAveragreRating((long) starposition - initialRating, true));


                                } else {

                                    //set Rating on Product
                                    updateRating.put(starposition + 1 + "_star", (long) documentSnapshot.get(starposition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAveragreRating((long) starposition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                    //set Rating on Product
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            ///set rating with current user
                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBqueries.myRatedIds.contains(productID)) {
                                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starposition + 1);
                                            } else {

                                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starposition + 1);
                                            }

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBqueries.myRatedIds.contains(productID)) {
                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starposition + 1);

                                                            TextView oldRating = (TextView) ratingNumbersContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingNumbersContainer.getChildAt(5 - starposition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));
                                                        } else {

                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRating.add((long) starposition + 1);

                                                            TextView rating = (TextView) ratingNumbersContainer.getChildAt(5 - starposition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            tottalRatingMiniview.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRating.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));


                                                            Toast.makeText(ProductDetailActivity.this, "Thanks for Rating!", Toast.LENGTH_SHORT).show();
                                                        }


                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingFigures = (TextView) ratingNumbersContainer.getChildAt(x);
                                                            // ratingFigures.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                                            ///rating progressbar
                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                        }
                                                        initialRating = starposition;
                                                        averageRating.setText(calculateAveragreRating(0, true));
                                                        averageRatingMiniView.setText(calculateAveragreRating(0, true));

                                                        if (DBqueries.wishList.contains(productID) && DBqueries.wishListModelList.size() != 0) {
                                                            int index = DBqueries.wishList.indexOf(productID);
                                                            //WishListModel changeRatings = DBqueries.wishListModelList.get(index);
                                                            DBqueries.wishListModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBqueries.wishListModelList.get(index).setTotalRating(Long.parseLong(totalRatingFigure.getText().toString()));


                                                        }
                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });
                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }

                }
            });
        }
        ///////Rating Layout

        ////here buy now button
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailAtivity = ProductDetailActivity.this;
                    // DeliveryActivity.cartItemModelList.clear();
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(documentSnapshot.getBoolean("COD"), CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupens")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) documentSnapshot.get("offers_applied")
                            , (long) 0
                            , inStock
                            , (long) documentSnapshot.get("max-quantity")
                            , (long) documentSnapshot.get("stock_quantity")));

                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddress(ProductDetailActivity.this, loadingDialog, true);
                    } else {
                        Intent deilveryIntent = new Intent(ProductDetailActivity.this, DeliveryActivity.class);
                        startActivity(deilveryIntent);

                    }
                }
            }
        });
        ////here buy now button




        ////coupen redemption btn
        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkCoupenPriceDialog.show();
            }
        });

        ///for showing the signin dialog
        signInDialog = new Dialog(ProductDetailActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button signInbtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button signUpbtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(ProductDetailActivity.this, RegisterActivity.class);

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });


        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        ///for showing the signin dialog


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            coupenRedemptionLayout.setVisibility(View.GONE);
        } else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }
        if (currentUser != null) {
            //load rating
            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingList(ProductDetailActivity.this);
            }
            //load Reating
            ////wishList Load
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(ProductDetailActivity.this, loadingDialog, false);

            }
            //wishList Load
            if (DBqueries.rewardModelList.size() == 0){
                DBqueries.loadReward(ProductDetailActivity.this, loadingDialog, false);
                //  loadingDialog.
            }
            if (DBqueries.cartList.size() == 0 && DBqueries.wishList.size() == 0 && DBqueries.rewardModelList.size() == 0){
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }

        ///for rating
        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);
        }
        //check cartlist product exit or not
        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }
        //check wishlist product exit or not
        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHlIST = true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHlIST = false;
        }
        invalidateOptionsMenu();
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

    public static void setRating(int starPosition1) {

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starButtion = (ImageView) rateNowContainer.getChildAt(x);
            starButtion.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));

            if (x <= starPosition1) {
                starButtion.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }
    }


    private String calculateAveragreRating(long currentUserRating, boolean update) {
        Double totalStar = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingNumbersContainer.getChildAt(5 - x);
            // totalStar = totalStar + ((long) documentSnapshot.get(x+"_star")*x);
            totalStar = totalStar + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStar = totalStar + currentUserRating;
        if (update) {
            return String.valueOf(totalStar / Long.parseLong(totalRatingFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStar / (Long.parseLong(totalRatingFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.main_cart_icon);

        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcone = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcone.setImageResource(R.drawable.icon_cart_white);
        badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

        if (currentUser != null) {
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailActivity.this));
            } else {
                badgeCount.setVisibility(View.VISIBLE);

                if (DBqueries.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                } else {
                    badgeCount.setText("99");
                }
            }
        }
        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    showCart = true;
                    startActivity(intent);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            productDetailAtivity = null;
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            if (fromSearh){
                finish();
            }else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                showCart = true;
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailAtivity = null;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearh = false;
    }
}
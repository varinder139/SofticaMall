package com.webservice.softicamall;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdaptor extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int lastPosition = -1;

    public HomePageAdaptor(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;

            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;

            default:
                return -1;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannerSliderView);

            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdBannerViewHolder(stripAdView);

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewholder(horizontalProductView);

            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProdutViewHolder(gridProductView);


            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPagger(sliderModelList);
                break;

            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResoures();
                String color = homePageModelList.get(position).getBackgroundolor();
                ((StripAdBannerViewHolder) holder).setStripAd(resource, color);
                break;

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String layoutColor = homePageModelList.get(position).getBackgroundolor();
                String horizontalLayoutTitle = homePageModelList.get(position).getTitle();
                List<WishListModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalProductViewholder) holder).setHorizontalProductLayout(horizontalProductScrollModelList, horizontalLayoutTitle, layoutColor, viewAllProductList);
                break;

            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridLayoutColor = homePageModelList.get(position).getBackgroundolor();
                String gridLayoutTitle = homePageModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProdutViewHolder) holder).setGridProductLayout(gridProductScrollModelList, gridLayoutTitle, gridLayoutColor);
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
        return homePageModelList.size();
    }


    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPagger;
        private int currentPage ;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangeList;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPagger = itemView.findViewById(R.id.banner_slider_view_pager);
        }

        private void setBannerSliderViewPagger(final List<SliderModel> sliderModelList) {
            currentPage = 2;
            if (timer != null){
                timer.cancel();
            }
            arrangeList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++){
                arrangeList.add(x, sliderModelList.get(x));
            }
            arrangeList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangeList.add(1, sliderModelList.get(sliderModelList.size() - 1));
            arrangeList.add(sliderModelList.get(0));
            arrangeList.add(sliderModelList.get(1));

            SliderAdaptor sliderAdaptor = new SliderAdaptor(arrangeList);
            bannerSliderViewPagger.setAdapter(sliderAdaptor);
            bannerSliderViewPagger.setClipToPadding(false);
            bannerSliderViewPagger.setPageMargin(20);

            bannerSliderViewPagger.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangeList);
                    }
                }
            };
            bannerSliderViewPagger.addOnPageChangeListener(onPageChangeListener);

            startBannerSlideShow(arrangeList);

            bannerSliderViewPagger.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(arrangeList);
                    stopBannerSlideShow();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideShow(arrangeList);
                    }
                    return false;
                }
            });
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPagger.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPagger.setCurrentItem(currentPage, false);
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViewPagger.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopBannerSlideShow() {
            timer.cancel();
        }
    }

    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView stripAdImage;
        private ConstraintLayout stripAdontainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdontainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(String resoure, String color) {
            //stripAdImage.setImageResource(resoure);
            Glide.with(itemView.getContext()).load(resoure).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(stripAdImage);
            stripAdontainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewholder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView horizobtalLayoutTitle;
        private Button horizobtalLayoutViewAllBtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            horizobtalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizobtalLayoutViewAllBtn = itemView.findViewById(R.id.horizontal_scroll_viewall_btn);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);

        }

        private void setHorizontalProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color, final List<WishListModel> viewAllProductList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizobtalLayoutTitle.setText(title);

            for (final HorizontalProductScrollModel model:horizontalProductScrollModelList){
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()){

                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                model.setProductTitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductPrice(task.getResult().getString("product_price"));

                                WishListModel wishListModel = viewAllProductList
                                        .get(horizontalProductScrollModelList.indexOf(model));

                                wishListModel.setTotalRating(task.getResult().getLong("total_ratings"));
                                wishListModel.setRating(task.getResult().getString("average_rating"));
                                wishListModel.setProductTitle(task.getResult().getString("product_title"));
                                wishListModel.setProdutPrie(task.getResult().getString("product_price"));
                                wishListModel.setProductImage(task.getResult().getString("product_image_1"));
                                wishListModel.setFreeCoupen(task.getResult().getLong("free_coupens"));
                                wishListModel.setCuttedPrice(task.getResult().getString("cutted_price"));
                                wishListModel.setCOD(task.getResult().getBoolean("COD"));
                                wishListModel.setInStok(task.getResult().getLong("stock_quantity") > 0);

                                if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size()-1){
                                    if (horizontalRecyclerView.getAdapter()!=null){
                                        horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                }


                            }else {
                                ///do noting
                            }
                        }
                    });
                }
            }

            if (horizontalProductScrollModelList.size() > 8) {
                horizobtalLayoutViewAllBtn.setVisibility(View.VISIBLE);

                horizobtalLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.wishListModelList = viewAllProductList;
                        Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        intent.putExtra("layout_code", 0);
                        intent.putExtra("title", title);
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else {
                horizobtalLayoutViewAllBtn.setVisibility(View.INVISIBLE);
            }
            HorizontalProductScrollAdaptor horizontalProductScrollAdaptor = new HorizontalProductScrollAdaptor(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.setAdapter(horizontalProductScrollAdaptor);
            horizontalProductScrollAdaptor.notifyDataSetChanged();
        }
    }

    public class GridProdutViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        TextView gridLayoutTitle;
        Button gridLayoutViewAllbtn;
        private androidx.gridlayout.widget.GridLayout gridProductLayout;

        public GridProdutViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container_grid);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutViewAllbtn = itemView.findViewById(R.id.grid_product_layout_viewall_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);

        }

        private void setGridProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color) {

            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);

            for (final HorizontalProductScrollModel model : horizontalProductScrollModelList){
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()){

                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                model.setProductTitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductPrice(task.getResult().getString("product_price"));


                                if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size()-1){
                                    setGridData(title, horizontalProductScrollModelList);

                                    if(!title.equals("")) {
                                        gridLayoutViewAllbtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                                                Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                                                intent.putExtra("layout_code", 1);
                                                intent.putExtra("title", title);
                                                itemView.getContext().startActivity(intent);
                                            }
                                        });
                                    }
                                }


                            }else {
                                ///do noting
                            }
                        }
                    });
                }
            }

            setGridData(title, horizontalProductScrollModelList);

        }
        private void setGridData(String title, final List<HorizontalProductScrollModel> horizontalProductScrollModelList){

            for (int x = 0; x < 4; x++) {
                ImageView produtImg = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_title);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_description);
                TextView productPrie = gridProductLayout.getChildAt(x).findViewById(R.id.h_s_product_prie);

                // produtImg.setImageResource(horizontalProductScrollModelList.get(x).getProductImage());
                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(produtImg);
                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDescription.setText(horizontalProductScrollModelList.get(x).getProductDscription());
                productPrie.setText("Rs. "+horizontalProductScrollModelList.get(x).getProductPrice()+"/-");
                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#FFFFFF"));

                if(!title.equals("")) {
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                            intent.putExtra("PRODUCT_ID", horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(intent);
                        }
                    });
                }
            }


        }
    }
}

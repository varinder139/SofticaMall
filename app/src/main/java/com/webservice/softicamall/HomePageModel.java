package com.webservice.softicamall;

import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD_BANNER = 1;
    public static final int HORIZONTAL_PRODUCT_VIEW = 2;
    public static final int GRID_PRODUCT_VIEW = 3;

    private int type;
    private String backgroundolor;

    /////////////////////// banner sliderpri
    private List<SliderModel> sliderModelList;

    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    /////////////////////// banner sliderpri

    /////////Strip Ad layout
    private String resoures;

    public HomePageModel(int type, String resoures, String backgroundolor) {
        this.type = type;
        this.resoures = resoures;
        this.backgroundolor = backgroundolor;
    }

    public String getResoures() {
        return resoures;
    }

    public void setResoures(String resoures) {
        this.resoures = resoures;
    }

    public String getBackgroundolor() {
        return backgroundolor;
    }

    public void setBackgroundolor(String backgroundolor) {
        this.backgroundolor = backgroundolor;
    }
    /////////Strip Ad layout


    private String title;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    ///////////// Horizontal Product Layout && Grid Product Layout
    private List<WishListModel> viewAllProductList;

    public HomePageModel(int type, String title, String backgroundolor, List<HorizontalProductScrollModel> horizontalProductScrollModelList
            , List<WishListModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundolor = backgroundolor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<WishListModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishListModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    ///////////// Horizontal Product Layout

    ///////Grid Product Layout layout
    public HomePageModel(int type, String title, String backgroundolor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundolor = backgroundolor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

   //////// Grid Product Layout

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }

    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

}

package com.webservice.softicamall;

import java.util.ArrayList;

public class WishListModel {
    private String productId;
    private  String productImage;
    private String productTitle;
    private long freeCoupen;
    private String rating;
    private long totalRating;
    private String produtPrie, cuttedPrice;
    private boolean COD;
    private boolean inStok;
    private ArrayList<String> tags;

    public WishListModel(String productId, String productImage, String productTitle, long freeCoupen, String rating, long totalRating, String produtPrie, String cuttedPrice, boolean COD, boolean inStok) {
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupen = freeCoupen;
        this.rating = rating;
        this.totalRating = totalRating;
        this.produtPrie = produtPrie;
        this.cuttedPrice = cuttedPrice;
        this.COD = COD;
        this.inStok = inStok;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public boolean isInStok() {
        return inStok;
    }

    public void setInStok(boolean inStok) {
        this.inStok = inStok;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getFreeCoupen() {
        return freeCoupen;
    }

    public void setFreeCoupen(long freeCoupen) {
        this.freeCoupen = freeCoupen;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }

    public String getProdutPrie() {
        return produtPrie;
    }

    public void setProdutPrie(String produtPrie) {
        this.produtPrie = produtPrie;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }
}

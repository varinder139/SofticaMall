package com.webservice.softicamall;

public class HorizontalProductScrollModel {

    private String productID;
    private String productImage;
    private String productTitle, productDscription, productPrice;

    public HorizontalProductScrollModel(String productID, String productImage, String productTitle, String productDscription, String productPrice) {
       this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDscription = productDscription;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getProductDscription() {
        return productDscription;
    }

    public void setProductDscription(String productDscription) {
        this.productDscription = productDscription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}

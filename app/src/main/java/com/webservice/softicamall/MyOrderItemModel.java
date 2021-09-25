package com.webservice.softicamall;

import java.util.Date;

public class MyOrderItemModel {

    private String productID;
    private String productTitle;
    private String produtImage;

    private String orderStatus;
    private String address;
    private String coupenId;
    private String cuttedPrice;
    private Date orderDate;
    private Date packedDate;
    private Date shifftDate;
    private Date deliverDate;
    private Date cancelledDate;
    private String disountedPrice;
    private Long freeCoupens;
    private String fullName;
    private String orderId;
    private String paymentMehtod;
    private String pincode;
    private String produtPrice;
    private Long productQuantity;
    private String userID;
    private String deliveryPrice;
    private boolean cancellationRequested;

    private int rating = 0;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public MyOrderItemModel(String productID, String orderStatus, String address, String coupenId, String cuttedPrice, Date orderDate, Date packedDate, Date shifftDate, Date deliverDate, Date cancelledDate, String disountedPrice, Long freeCoupens, String fullName, String orderId, String paymentMehtod, String pincode, String produtPrice, Long productQuantity, String userID, String produtImage, String productTitle, String deliveryPrice, boolean cancellationRequested) {
        this.productID = productID;
        this.orderStatus = orderStatus;
        this.address = address;
        this.coupenId = coupenId;
        this.cuttedPrice = cuttedPrice;
        this.orderDate = orderDate;
        this.packedDate = packedDate;
        this.shifftDate = shifftDate;
        this.deliverDate = deliverDate;
        this.cancelledDate = cancelledDate;
        this.disountedPrice = disountedPrice;
        this.freeCoupens = freeCoupens;
        this.fullName = fullName;
        this.orderId = orderId;
        this.paymentMehtod = paymentMehtod;
        this.pincode = pincode;
        this.produtPrice = produtPrice;
        this.productQuantity = productQuantity;
        this.userID = userID;
        this.produtImage = produtImage;
        this.productTitle = productTitle;
        this.deliveryPrice = deliveryPrice;
        this.cancellationRequested = cancellationRequested;
    }

    public boolean isCancellationRequested() {
        return cancellationRequested;
    }

    public void setCancellationRequested(boolean cancellationRequested) {
        this.cancellationRequested = cancellationRequested;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProdutImage() {
        return produtImage;
    }

    public void setProdutImage(String produtImage) {
        this.produtImage = produtImage;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoupenId() {
        return coupenId;
    }

    public void setCoupenId(String coupenId) {
        this.coupenId = coupenId;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShifftDate() {
        return shifftDate;
    }

    public void setShifftDate(Date shifftDate) {
        this.shifftDate = shifftDate;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getDisountedPrice() {
        return disountedPrice;
    }

    public void setDisountedPrice(String disountedPrice) {
        this.disountedPrice = disountedPrice;
    }

    public Long getFreeCoupens() {
        return freeCoupens;
    }

    public void setFreeCoupens(Long freeCoupens) {
        this.freeCoupens = freeCoupens;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMehtod() {
        return paymentMehtod;
    }

    public void setPaymentMehtod(String paymentMehtod) {
        this.paymentMehtod = paymentMehtod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProdutPrice() {
        return produtPrice;
    }

    public void setProdutPrice(String produtPrice) {
        this.produtPrice = produtPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

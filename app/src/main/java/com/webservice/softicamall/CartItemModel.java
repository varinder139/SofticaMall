package com.webservice.softicamall;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {

    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ///////////cart item
    private String productID;
    private String produtImage;
    private String produtTitle;
    private Long freeCoupn;
    private String productPrie;
    private String cuttedPrice;
    private Long produtQty;
    private Long maxQty;
    private Long stockQty;
    private Long offerApplied;
    private Long coupnApplied;
    private boolean inStock;
    private List<String> qtyIDs;
    private boolean qtyError;
    private String selectedCoupenId;
    private String discountedPrice;
    private boolean COD;



    public CartItemModel(boolean COD, int type, String productID, String produtImage, String produtTitle,
                         Long freeCoupn, String productPrie, String cuttedPrice, Long produtQty, Long offerApplied, Long coupnApplied, boolean inStock, Long maxQty, Long stockQty) {
        this.COD = COD;
        this.type = type;
        this.productID = productID;
        this.produtImage = produtImage;
        this.produtTitle = produtTitle;
        this.freeCoupn = freeCoupn;
        this.productPrie = productPrie;
        this.cuttedPrice = cuttedPrice;
        this.produtQty = produtQty;
        this.offerApplied = offerApplied;
        this.coupnApplied = coupnApplied;
        this.inStock = inStock;
        this.maxQty = maxQty;
        this.stockQty = stockQty;
        qtyIDs = new ArrayList<>();
        qtyError = false;
    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCoupenId() {
        return selectedCoupenId;
    }

    public void setSelectedCoupenId(String selectedCoupenId) {
        this.selectedCoupenId = selectedCoupenId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQty() {
        return stockQty;
    }

    public void setStockQty(Long stockQty) {
        this.stockQty = stockQty;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public Long getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Long maxQty) {
        this.maxQty = maxQty;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProdutImage() {
        return produtImage;
    }

    public void setProdutImage(String produtImage) {
        this.produtImage = produtImage;
    }

    public String getProdutTitle() {
        return produtTitle;
    }

    public void setProdutTitle(String produtTitle) {
        this.produtTitle = produtTitle;
    }

    public Long getFreeCoupn() {
        return freeCoupn;
    }

    public void setFreeCoupn(Long freeCoupn) {
        this.freeCoupn = freeCoupn;
    }

    public String getProductPrie() {
        return productPrie;
    }

    public void setProductPrie(String productPrie) {
        this.productPrie = productPrie;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Long getProdutQty() {
        return produtQty;
    }

    public void setProdutQty(Long produtQty) {
        this.produtQty = produtQty;
    }

    public Long getOfferApplied() {
        return offerApplied;
    }

    public void setOfferApplied(Long offerApplied) {
        this.offerApplied = offerApplied;
    }

    public Long getCoupnApplied() {
        return coupnApplied;
    }

    public void setCoupnApplied(Long coupnApplied) {
        this.coupnApplied = coupnApplied;
    }
    ////////////////cart item


    ///////////Cart Total

    private int totalItem, totalItemsPrice, totalAmount, savedAmount;
    private String deliveryPrie;

    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrie() {
        return deliveryPrie;
    }

    public void setDeliveryPrie(String deliveryPrie) {
        this.deliveryPrie = deliveryPrie;
    }
    ///////////Cart Total
}

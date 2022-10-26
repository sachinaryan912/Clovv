package com.company.clovv.modal;

public class SqLiteModalCart {

    String pName,pricee,pQuantity,userWt,productType,unitPrice,shopPerWeight,productId,productStockWtType,totalStock;

    public SqLiteModalCart() {
    }

    public SqLiteModalCart(String pName, String pricee,
                           String pQuantity, String userWt,
                           String productType,
                           String unitPrice,
                           String shopPerWeight,
                           String productId,
                           String productStockWtType,
                           String totalStock) {
        this.pName = pName;
        this.pricee = pricee;
        this.pQuantity = pQuantity;
        this.userWt = userWt;
        this.productType = productType;
        this.unitPrice = unitPrice;
        this.shopPerWeight = shopPerWeight;
        this.productId = productId;
        this.productStockWtType = productStockWtType;
        this.totalStock = totalStock;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPricee() {
        return pricee;
    }

    public void setPricee(String pricee) {
        this.pricee = pricee;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getUserWt() {
        return userWt;
    }

    public void setUserWt(String userWt) {
        this.userWt = userWt;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(String totalStock) {
        this.totalStock = totalStock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductStockWtType() {
        return productStockWtType;
    }

    public void setProductStockWtType(String productStockWtType) {
        this.productStockWtType = productStockWtType;
    }

    public String getShopPerWeight() {
        return shopPerWeight;
    }

    public void setShopPerWeight(String shopPerWeight) {
        this.shopPerWeight = shopPerWeight;
    }
}

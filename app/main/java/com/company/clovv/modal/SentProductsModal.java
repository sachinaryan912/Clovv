package com.company.clovv.modal;

public class SentProductsModal {

    String productId,productName,productQuantity,productUnitPrice,productAmount,productType,productUserWtType;

    public SentProductsModal() {
    }

    public SentProductsModal(String productId,
                             String productName,
                             String productQuantity,
                             String productUnitPrice,
                             String productAmount,
                             String productType,
                             String productUserWtType) {
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productUnitPrice = productUnitPrice;
        this.productAmount = productAmount;
        this.productType = productType;
        this.productUserWtType = productUserWtType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(String productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductUserWtType() {
        return productUserWtType;
    }

    public void setProductUserWtType(String productUserWtType) {
        this.productUserWtType = productUserWtType;
    }
}

package com.company.clovv.modal;

public class SearchProductModal {

    String productName,productOwnerId,ProductImageUrl;
    double productPrice;

    public SearchProductModal() {
    }

    public SearchProductModal(String productName, String productOwnerId, String productImageUrl, double productPrice) {
        this.productName = productName;
        this.productOwnerId = productOwnerId;
        this.ProductImageUrl = productImageUrl;
        this.productPrice = productPrice;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductOwnerId() {
        return productOwnerId;
    }

    public void setProductOwnerId(String productOwnerId) {
        this.productOwnerId = productOwnerId;
    }
}

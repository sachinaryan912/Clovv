package com.company.clovv.modal;

public class ProductListModal {

    String ProductImageUrl,productName,productOwnerId,productPackageType,productPackageWeight,
            productWtType,shopType,productWeightPerSym,productDescription,productId,productItemAvailable;

    double productPrice;


    public ProductListModal() {
    }

    public ProductListModal(String productImageUrl, String productName, String productOwnerId, String productPackageType, String productPackageWeight, String productWtType,
                            String shopType,
                            String productWeightPerSym,
                            String productDescription,
                            String productId,
                            double productPrice,
                            String productItemAvailable) {
        ProductImageUrl = productImageUrl;
        this.productName = productName;
        this.productOwnerId = productOwnerId;
        this.productPackageType = productPackageType;
        this.productPackageWeight = productPackageWeight;
        this.productWtType = productWtType;
        this.shopType = shopType;
        this.productWeightPerSym = productWeightPerSym;
        this.productDescription = productDescription;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productItemAvailable = productItemAvailable;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
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

    public String getProductPackageType() {
        return productPackageType;
    }

    public void setProductPackageType(String productPackageType) {
        this.productPackageType = productPackageType;
    }

    public String getProductPackageWeight() {
        return productPackageWeight;
    }

    public void setProductPackageWeight(String productPackageWeight) {
        this.productPackageWeight = productPackageWeight;
    }

    public String getProductWtType() {
        return productWtType;
    }

    public void setProductWtType(String productWtType) {
        this.productWtType = productWtType;
    }


    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getProductWeightPerSym() {
        return productWeightPerSym;
    }

    public void setProductWeightPerSym(String productWeightPerSym) {
        this.productWeightPerSym = productWeightPerSym;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }


    public String getProductItemAvailable() {
        return productItemAvailable;
    }

    public void setProductItemAvailable(String productItemAvailable) {
        this.productItemAvailable = productItemAvailable;
    }
}

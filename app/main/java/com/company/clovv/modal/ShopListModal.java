package com.company.clovv.modal;

public class ShopListModal {

    String uid,shop_name,locality_shop,latitude_shop,longitude_shop;
    boolean isShopOpen;

    public ShopListModal() {
    }

    public ShopListModal(String uid, String shop_name,
                         String locality_shop, String latitude_shop, String longitude_shop, boolean isShopOpen) {
        this.uid = uid;
        this.shop_name = shop_name;
        this.locality_shop = locality_shop;
        this.latitude_shop = latitude_shop;
        this.longitude_shop = longitude_shop;
        this.isShopOpen = isShopOpen;
    }

    public boolean isShopOpen() {
        return isShopOpen;
    }

    public void setShopOpen(boolean shopOpen) {
        isShopOpen = shopOpen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLocality_shop() {
        return locality_shop;
    }

    public void setLocality_shop(String locality_shop) {
        this.locality_shop = locality_shop;
    }

    public String getLatitude_shop() {
        return latitude_shop;
    }

    public void setLatitude_shop(String latitude_shop) {
        this.latitude_shop = latitude_shop;
    }

    public String getLongitude_shop() {
        return longitude_shop;
    }

    public void setLongitude_shop(String longitude_shop) {
        this.longitude_shop = longitude_shop;
    }
}

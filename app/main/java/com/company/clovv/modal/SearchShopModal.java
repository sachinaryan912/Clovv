package com.company.clovv.modal;

public class SearchShopModal {


    String uid,shop_name;

    public SearchShopModal() {
    }

    public SearchShopModal(String uid, String shop_name) {
        this.uid = uid;
        this.shop_name = shop_name;
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
}

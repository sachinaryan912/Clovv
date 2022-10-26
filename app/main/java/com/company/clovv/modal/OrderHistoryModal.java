package com.company.clovv.modal;

public class OrderHistoryModal {
    String orderId,orderTime,orderDate,priceOrder,customerId,shopId,itemOrder,cancelledBy;
    boolean isAccepted,isPaymentCompleted,isCanceled,isOutForDelivery,isOrderExpired,isOrderSuccess,isOngoingOrderCancelled;


    public OrderHistoryModal() {
    }

    public OrderHistoryModal(String orderId, String orderTime, String orderDate,
                             String priceOrder, String customerId,
                             String shopId,
                             String itemOrder,
                             String cancelledBy,
                             boolean isAccepted,
                             boolean isPaymentCompleted,
                             boolean isCanceled, boolean isOutForDelivery,
                             boolean isOrderExpired, boolean isOrderSuccess,
                             boolean isOngoingOrderCancelled) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.priceOrder = priceOrder;
        this.customerId = customerId;
        this.shopId = shopId;
        this.itemOrder = itemOrder;
        this.cancelledBy = cancelledBy;
        this.isAccepted = isAccepted;
        this.isPaymentCompleted = isPaymentCompleted;
        this.isCanceled = isCanceled;
        this.isOutForDelivery = isOutForDelivery;
        this.isOrderExpired = isOrderExpired;
        this.isOrderSuccess = isOrderSuccess;
        this.isOngoingOrderCancelled = isOngoingOrderCancelled;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isPaymentCompleted() {
        return isPaymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        isPaymentCompleted = paymentCompleted;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isOutForDelivery() {
        return isOutForDelivery;
    }

    public void setOutForDelivery(boolean outForDelivery) {
        isOutForDelivery = outForDelivery;
    }

    public boolean isOrderExpired() {
        return isOrderExpired;
    }

    public void setOrderExpired(boolean orderExpired) {
        isOrderExpired = orderExpired;
    }

    public boolean isOrderSuccess() {
        return isOrderSuccess;
    }

    public void setOrderSuccess(boolean orderSuccess) {
        isOrderSuccess = orderSuccess;
    }

    public boolean isOngoingOrderCancelled() {
        return isOngoingOrderCancelled;
    }

    public void setOngoingOrderCancelled(boolean ongoingOrderCancelled) {
        isOngoingOrderCancelled = ongoingOrderCancelled;
    }

    public String getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(String itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(String priceOrder) {
        this.priceOrder = priceOrder;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }


}

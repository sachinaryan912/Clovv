package com.company.clovv.modal;

public class SendOrderDetailsModal {

    String customerName,customerPhn,customerAdd,distance,
            modePay,orderId,priceOrder,ItemOrder,orderDate,
            orderTime,cancelledBy,shopId,customerId,date;
    boolean isAccepted,isPaymentCompleted,isCanceled,isOutForDelivery,isOrderExpired,isOrderSuccess,isOngoingOrderCancelled;

    public SendOrderDetailsModal() {
    }


    public SendOrderDetailsModal(String customerName, String customerPhn, String customerAdd,
                                 String distance, String modePay,
                                 String orderId, String priceOrder, String itemOrder, String orderDate, String orderTime,
                                 String cancelledBy,
                                 String shopId,
                                 String customerId,
                                 String date,
                                 boolean isAccepted,
                                 boolean isPaymentCompleted,
                                 boolean isCanceled,
                                 boolean isOutForDelivery, boolean isOrderExpired,boolean isOrderSuccess
            ,boolean isOngoingOrderCancelled) {
        this.customerName = customerName;
        this.customerPhn = customerPhn;
        this.customerAdd = customerAdd;
        this.distance = distance;
        this.modePay = modePay;
        this.orderId = orderId;
        this.priceOrder = priceOrder;
        ItemOrder = itemOrder;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.cancelledBy = cancelledBy;
        this.shopId = shopId;
        this.customerId = customerId;
        this.date = date;
        this.isAccepted = isAccepted;
        this.isPaymentCompleted = isPaymentCompleted;
        this.isCanceled = isCanceled;
        this.isOutForDelivery = isOutForDelivery;
        this.isOrderExpired = isOrderExpired;
        this.isOrderSuccess = isOrderSuccess;
        this.isOngoingOrderCancelled = isOngoingOrderCancelled;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhn() {
        return customerPhn;
    }

    public void setCustomerPhn(String customerPhn) {
        this.customerPhn = customerPhn;
    }

    public String getCustomerAdd() {
        return customerAdd;
    }

    public void setCustomerAdd(String customerAdd) {
        this.customerAdd = customerAdd;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getModePay() {
        return modePay;
    }

    public void setModePay(String modePay) {
        this.modePay = modePay;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(String priceOrder) {
        this.priceOrder = priceOrder;
    }

    public String getItemOrder() {
        return ItemOrder;
    }

    public void setItemOrder(String itemOrder) {
        ItemOrder = itemOrder;
    }


    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
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

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public boolean isOngoingOrderCancelled() {
        return isOngoingOrderCancelled;
    }

    public void setOngoingOrderCancelled(boolean ongoingOrderCancelled) {
        isOngoingOrderCancelled = ongoingOrderCancelled;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}

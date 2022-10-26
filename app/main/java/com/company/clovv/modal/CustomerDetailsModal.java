package com.company.clovv.modal;

public class CustomerDetailsModal {

    String name,phone,customerId,blockReason;
    boolean isActive;

    public CustomerDetailsModal() {
    }

    public CustomerDetailsModal(String name, String phone, String customerId, String blockReason, boolean isActive) {
        this.name = name;
        this.phone = phone;
        this.customerId = customerId;
        this.blockReason = blockReason;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

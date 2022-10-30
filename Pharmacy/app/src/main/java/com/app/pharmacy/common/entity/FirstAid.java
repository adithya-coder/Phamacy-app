package com.app.pharmacy.common.entity;

public class FirstAid {
    private String id;

    private String firstItemName;

    private String firstPrice;

    private String email;

    private String manfacture;

    private String image;

    private String description;

    private String userId;

    private String pharmacyId;

    private boolean isCosmatic;

    private boolean isMedicine;

    private boolean isFirst;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstItemName() {
        return firstItemName;
    }

    public void setFirstItemName(String firstItemName) {
        this.firstItemName = firstItemName;
    }

    public String getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(String firstPrice) {
        this.firstPrice = firstPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getManfacture() {
        return manfacture;
    }

    public void setManfacture(String manfacture) {
        this.manfacture = manfacture;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public boolean isCosmatic() {
        return isCosmatic;
    }

    public void setCosmatic(boolean cosmatic) {
        isCosmatic = cosmatic;
    }

    public boolean isMedicine() {
        return isMedicine;
    }

    public void setMedicine(boolean medicine) {
        isMedicine = medicine;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}

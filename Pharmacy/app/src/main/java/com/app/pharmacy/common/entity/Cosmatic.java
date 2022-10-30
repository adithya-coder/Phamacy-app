package com.app.pharmacy.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cosmatic implements Serializable {

    private String id;

    private String cosName;

    private String cosPrice;

    private String email;

    private String manfacture;

    private String image;

    private String description;

    private String userId;

    private String pharmacyId;

    private boolean isCosmatic;

    private boolean isMedicine;

    private boolean isFirst;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCosName() {
        return cosName;
    }

    public void setCosName(String cosName) {
        this.cosName = cosName;
    }

    public String getCosPrice() {
        return cosPrice;
    }

    public void setCosPrice(String cosPrice) {
        this.cosPrice = cosPrice;
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

}

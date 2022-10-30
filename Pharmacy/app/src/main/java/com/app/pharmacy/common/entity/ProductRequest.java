package com.app.pharmacy.common.entity;

import java.io.Serializable;

public class ProductRequest implements Serializable {
    private String id;

    private String pharmacyId;

    private String productId;

    private String requestBy;

    private boolean isClame;

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public boolean isClame() {
        return isClame;
    }

    public void setClame(boolean clame) {
        isClame = clame;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

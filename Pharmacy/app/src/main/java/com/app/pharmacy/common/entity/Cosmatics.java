package com.app.pharmacy.common.entity;

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

public class Cosmatics {
    private String id;
    private String CosmaticName;
    private String ManufactureName;
    private String Description;
    private String image;
    private String price;
    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCosmaticName() {
        return CosmaticName;
    }

    public void setCosmaticName(String cosmaticName) {
        CosmaticName = cosmaticName;
    }

    public String getManufactureName() {
        return ManufactureName;
    }

    public void setManufactureName(String manufactureName) {
        ManufactureName = manufactureName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


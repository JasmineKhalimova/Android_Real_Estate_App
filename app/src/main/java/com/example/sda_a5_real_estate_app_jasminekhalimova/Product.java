package com.example.sda_a5_real_estate_app_jasminekhalimova;

import java.util.List;

public class Product {
    private String Title;
    private String Price;
    private String City;
    private String Area;
    private String Description;
    private String AdvertiserName;
    private String AdvertiserEmail;
    private String AdvertiserUserId;

    public Product() {
        // Must have a public no-argument constructor
    }
    // Initialize all fields of a product
    public Product(String Title, String Price, String City, String Area,String Description,String AdvertiserName,String AdvertiserEmail,String AdvertiserUserId) {
        this.Title = Title;
        this.Price = Price;
        this.City = City;
        this.Area = Area;
        this.Description = Description;
        this.AdvertiserName = AdvertiserName;
        this.AdvertiserEmail = AdvertiserEmail;
        this.AdvertiserUserId = AdvertiserUserId;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }
    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }
    public String getArea() {
        return Area;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
    public String getAdvertiserName() {
        return AdvertiserName;
    }

    public void setAdvertiserName(String AdvertiserName) {
        this.AdvertiserName = AdvertiserName;
    }
    public String getAdvertiserEmail() {
        return AdvertiserEmail;
    }

    public void setAdvertiserEmail(String AdvertiserEmail) {
        this.AdvertiserEmail = AdvertiserEmail;
    }
    public String getAdvertiserUserId() {
        return AdvertiserUserId;
    }

    public void setAdvertiserUserId(String AdvertiserUserId) {
        this.AdvertiserUserId = AdvertiserUserId;
    }
}

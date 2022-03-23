package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private String id;
    private String date;
    private String time;
    private String amount;
    private String status;
    private String total_price;
    private String address;
    private String latitude;
    private String longitude;
    private ProductModel.Product product;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public ProductModel.Product getProduct() {
        return product;
    }

}

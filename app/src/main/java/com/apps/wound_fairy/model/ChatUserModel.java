package com.apps.wound_fairy.model;

import java.io.Serializable;

public class ChatUserModel implements Serializable {
    private String user_id;
    private String user_name;
    private String user_phone;
    private String user_image;


    // user_id of caterer لو حصل فيها مشكلة علي الي قايل كده انتوا حرين بقا مليش فيه
    private String caterer_id;
    private String caterer_name;
    private String caterer_phone;
    private String caterer_image;
    private String address;
    private String lat;
    private String lng;

    private String order_id;
    private String total;


    public ChatUserModel(String user_id, String user_name, String user_phone, String user_image, String caterer_id, String caterer_name, String caterer_phone, String caterer_image, String address, String lat, String lng, String order_id, String total) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_image = user_image;
        this.caterer_id = caterer_id;
        this.caterer_name = caterer_name;
        this.caterer_phone = caterer_phone;
        this.caterer_image = caterer_image;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.order_id = order_id;
        this.total = total;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getCaterer_id() {
        return caterer_id;
    }

    public String getCaterer_name() {
        return caterer_name;
    }

    public String getCaterer_phone() {
        return caterer_phone;
    }

    public String getCaterer_image() {
        return caterer_image;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getTotal() {
        return total;
    }
}

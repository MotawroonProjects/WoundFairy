package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class ReservationModel implements Serializable {
    private String id;
    private String complaint;
    private List<String> images;
    private String date;
    private String time;
    private String total_price;
    private String latitude;
    private String longitude;
    private String status;
    private String address;
    private ServiceDepartmentModel.Department service;

    public String getId() {
        return id;
    }

    public String getComplaint() {
        return complaint;
    }

    public List<String> getImages() {
        return images;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public ServiceDepartmentModel.Department getService() {
        return service;
    }
}

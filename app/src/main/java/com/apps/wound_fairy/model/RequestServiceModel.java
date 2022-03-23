package com.apps.wound_fairy.model;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.apps.wound_fairy.BR;
import com.apps.wound_fairy.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequestServiceModel extends BaseObservable implements Serializable {
    private int service_id;
    private String complaint;
    private List<String> images;
    private String date;
    private String time;
    private String total_price;
    private String latitude;
    private String longitude;
    private String address;
    private String selected_department;

    public ObservableField<String> error_complaint = new ObservableField<>();
    public ObservableField<String> error_date = new ObservableField<>();
    public ObservableField<String> error_time = new ObservableField<>();

    public boolean isDataValid(Context context) {
        if (service_id != 0 &&
                !complaint.isEmpty() &&
                !date.isEmpty() &&
                !time.isEmpty())
        {
            error_complaint.set(null);
            error_date.set(null);
            error_time.set(null);

            return true;
        }else {
            if (service_id == 0) {
                Toast.makeText(context, R.string.choose_service, Toast.LENGTH_SHORT).show();
            }
            if (complaint.isEmpty()){
                error_complaint.set(context.getString(R.string.field_req));
            }else {
                error_complaint.set(null);
            }
            if (date.isEmpty()){
                error_date.set(context.getString(R.string.field_req));
            }else {
                error_date.set(null);
            }
            if (time.isEmpty()){
                error_time.set(context.getString(R.string.field_req));
            }else {
                error_time.set(null);
            }

            return false;
        }
    }

    public RequestServiceModel() {
        this.service_id = 0;

        this.complaint = "";
        notifyPropertyChanged(BR.complaint);
        this.date = "";
        notifyPropertyChanged(BR.date);
        this.time = "";
        notifyPropertyChanged(BR.time);
        this.total_price = "";
        notifyPropertyChanged(BR.total_price);
        this.latitude = "";
        notifyPropertyChanged(BR.latitude);
        this.longitude = "";
        notifyPropertyChanged(BR.longitude);
        this.address = "";
        notifyPropertyChanged(BR.address);
        this.images = new ArrayList<>();
        notifyPropertyChanged(BR.images);
        this.selected_department="";
        notifyPropertyChanged(BR.selected_department);
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    @Bindable
    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
        notifyPropertyChanged(BR.complaint);
    }

    @Bindable
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
        notifyPropertyChanged(BR.images);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
        notifyPropertyChanged(BR.total_price);
    }

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getSelected_department() {
        return selected_department;
    }

    public void setSelected_department(String selected_department) {
        this.selected_department = selected_department;
        notifyPropertyChanged(BR.address);
    }
}

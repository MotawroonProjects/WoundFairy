package com.apps.wound_fairy.model;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.apps.wound_fairy.R;

public class SendOrderModel extends BaseObservable {
    private String latitude;
    private String longitude;
    private String address;
    private String note;
    public ObservableField<String> error_address = new ObservableField<>();


    public SendOrderModel() {
        this.latitude="";
        notifyPropertyChanged(BR.latitude);
        this.longitude="";
        notifyPropertyChanged(BR.longitude);
        this.address="";
        notifyPropertyChanged(BR.address);
        this.note="";
        notifyPropertyChanged(BR.note);
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
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        notifyPropertyChanged(BR.note);
    }
    public boolean isDataValid(Context context) {
        if (
                !address.isEmpty()

        ) {

            error_address.set(null);

            return true;
        } else {


            if (address.isEmpty()) {
                error_address.set(context.getString(R.string.field_required));

            } else {
                error_address.set(null);

            }


            return false;
        }
    }

}

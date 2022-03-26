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

public class RequestChatModel extends BaseObservable implements Serializable {
    private String complaint;
    private List<String> images;


    public ObservableField<String> error_complaint = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!complaint.isEmpty())
        {
            error_complaint.set(null);


            return true;
        }else {

//            if (complaint.isEmpty()){
//                error_complaint.set(context.getString(R.string.field_req));
//            }else {
//                error_complaint.set(null);
//            }


            return false;
        }
    }

    public RequestChatModel() {


        this.complaint = "";
        notifyPropertyChanged(BR.complaint);

        this.images = new ArrayList<>();
        notifyPropertyChanged(BR.images);

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


}

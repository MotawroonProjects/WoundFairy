package com.apps.wound_fairy.model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.apps.wound_fairy.BR;
import com.apps.wound_fairy.R;


public class SignUpModel extends BaseObservable {
    private String image;
    private String firstName;
    private String lastName;
    private String phone_code;
    private String phone;
    private String email;

    public ObservableField<String> error_firstName = new ObservableField<>();
    public ObservableField<String> error_lastName = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();



    public boolean isDataValid(Context context) {
        if (!firstName.trim().isEmpty()&&
                !lastName.trim().isEmpty()&&
                !phone.trim().isEmpty()
        ) {
            error_firstName.set(null);
            error_lastName.set(null);
            error_phone.set(null);
            return true;
        } else {
            if (firstName.trim().isEmpty()){
                error_firstName.set(context.getString(R.string.field_required));
            }else {
                error_firstName.set(null);
            }
            if (lastName.trim().isEmpty()){
                error_lastName.set(context.getString(R.string.field_required));
            }else {
                error_lastName.set(null);
            }
            if (phone.trim().isEmpty()){
                error_phone.set(context.getString(R.string.field_required));
            }else {
                error_phone.set(null);
            }

            return false;
        }


    }


    public SignUpModel(String phone_code, String phone) {
        this.image = "";
        this.firstName = "";
        this.lastName="";
        this.phone_code =phone_code;
        this.phone = phone;
        this.email="";

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

}
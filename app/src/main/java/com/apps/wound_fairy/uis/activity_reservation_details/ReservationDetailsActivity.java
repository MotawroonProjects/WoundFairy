package com.apps.wound_fairy.uis.activity_reservation_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityReservationDetailsBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class ReservationDetailsActivity extends BaseActivity {
    private ActivityReservationDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_reservation_details);
        initView();
    }

    private void initView() {

    }
}
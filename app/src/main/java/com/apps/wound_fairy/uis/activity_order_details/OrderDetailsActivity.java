package com.apps.wound_fairy.uis.activity_order_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityOrderDetailsBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class OrderDetailsActivity extends BaseActivity {

    private ActivityOrderDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_order_details);
        initView();
    }

    private void initView() {
    }
}
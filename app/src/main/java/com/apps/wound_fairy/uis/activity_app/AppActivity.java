package com.apps.wound_fairy.uis.activity_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityAppBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class AppActivity extends BaseActivity {
    private ActivityAppBinding binding;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_app);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent(){
        Intent intent=getIntent();
        type = intent.getStringExtra("data");

    }

    private void initView() {
    }
}
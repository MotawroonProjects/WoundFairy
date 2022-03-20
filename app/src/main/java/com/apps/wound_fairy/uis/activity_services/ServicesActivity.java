package com.apps.wound_fairy.uis.activity_services;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityServicesBinding;
import com.apps.wound_fairy.model.ServiceModel;
import com.apps.wound_fairy.mvvm.ActivityServicesMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class ServicesActivity extends BaseActivity {
    private ActivityServicesBinding binding;
    private ActivityServicesMvvm mvvm;
    private String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_services);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

    }

    private void initView() {
        binding.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(ActivityServicesMvvm.class);

        mvvm.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.progBar.setVisibility(View.VISIBLE);
            } else {
                binding.progBar.setVisibility(View.GONE);
            }
        });

        if (type.equals("visit")){
            binding.llReqService.setVisibility(View.VISIBLE);
        }
        mvvm.getMutableLiveData().observe(this, serviceModel -> {
            if (serviceModel !=null){
                binding.setModel(serviceModel.getData());
            }
        });
        mvvm.getService(getLang(),type);
        binding.llBack.setOnClickListener(view -> finish());
    }
}
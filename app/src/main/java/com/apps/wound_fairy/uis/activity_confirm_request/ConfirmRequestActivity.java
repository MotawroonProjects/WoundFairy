package com.apps.wound_fairy.uis.activity_confirm_request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityConfirmRequestBinding;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class ConfirmRequestActivity extends BaseActivity {
    private ActivityConfirmRequestBinding binding;
    private RequestServiceModel requestServiceModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_request);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        requestServiceModel= (RequestServiceModel) intent.getSerializableExtra("data");

    }
    private void initView() {
        binding.setLang(getLang());
        setUpToolbar(binding.toolbar, getString(R.string.confirm_request), R.color.white, R.color.black);

    }
}
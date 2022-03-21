package com.apps.wound_fairy.uis.activity_confirm_request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityConfirmRequestBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class ConfirmRequestActivity extends BaseActivity {
    private ActivityConfirmRequestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_confirm_request);
        initView();
    }

    private void initView() {

    }
}
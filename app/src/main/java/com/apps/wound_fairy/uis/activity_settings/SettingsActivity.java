package com.apps.wound_fairy.uis.activity_settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivitySettingsBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_settings);

        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.settings), R.color.white, R.color.black);
    }
}
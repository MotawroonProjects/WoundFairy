package com.apps.wound_fairy.uis.activity_services;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityServicesBinding;
import com.apps.wound_fairy.model.MessageModel;
import com.apps.wound_fairy.mvvm.ActivityServicesMvvm;
import com.apps.wound_fairy.tags.Tags;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_chat.ChatActivity;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;
import com.apps.wound_fairy.uis.activity_login.LoginActivity;
import com.apps.wound_fairy.uis.activity_request_chat.RequesChatActivity;
import com.apps.wound_fairy.uis.activity_request_service.RequestServiceActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServicesActivity extends BaseActivity {
    private ActivityServicesBinding binding;
    private ActivityServicesMvvm mvvm;
    private String type = "";
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_services);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

    }

    private void initView() {
        binding.setLang(getLang());
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {

            }

//
        });
        mvvm = ViewModelProviders.of(this).get(ActivityServicesMvvm.class);
        mvvm.onDataSuccess().observe(this, new androidx.lifecycle.Observer<List<MessageModel>>() {
            @Override
            public void onChanged(List<MessageModel> messageModels) {
                if (messageModels.size() > 0) {
                    Intent intent = new Intent(ServicesActivity.this, ChatActivity.class);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(ServicesActivity.this, RequesChatActivity.class);
                    intent.putExtra("type", type);
                    startActivity(intent);
                }
            }
        });
        mvvm.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.progBar.setVisibility(View.VISIBLE);
            } else {
                binding.progBar.setVisibility(View.GONE);
            }
        });

        if (type.equals("visit")) {
            binding.llReqService.setVisibility(View.VISIBLE);
        }
        mvvm.getMutableLiveData().observe(this, visitOnlineModel -> {
            if (visitOnlineModel != null) {
                binding.setModel(visitOnlineModel.getData());
            }
        });
        mvvm.getService(getLang(), type);
        binding.llBack.setOnClickListener(view -> finish());

        binding.llReqService.setOnClickListener(view -> {
            if (getUserModel() != null) {
                Intent intent = new Intent(ServicesActivity.this, RequestServiceActivity.class);
                startActivity(intent);
            } else {
                navigationToLoginActivity();
            }

        });
        binding.llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUserModel() != null) {
                    mvvm.getChatData(getUserModel(), ServicesActivity.this);
                } else {
                    navigationToLoginActivity();
                }
            }
        });

    }

    private void navigationToLoginActivity() {
        req = 1;
        Intent intent = new Intent(ServicesActivity.this, LoginActivity.class);
        launcher.launch(intent);
    }
}
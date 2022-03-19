package com.apps.wound_fairy.uis.activity_contact_us;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityContactUsBinding;
import com.apps.wound_fairy.model.ContactUsModel;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.mvvm.ContactusActivityMvvm;
import com.apps.wound_fairy.preferences.Preferences;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class ContactUsActivity extends BaseActivity {
    private ActivityContactUsBinding binding;
    private ContactUsModel contactUsModel;
    private ContactusActivityMvvm contactusActivityMvvm;
    private UserModel userModel;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        contactusActivityMvvm = ViewModelProviders.of(this).get(ContactusActivityMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.contact_us), R.color.white, R.color.black);

        contactUsModel = new ContactUsModel();
        if (userModel != null) {
            contactUsModel.setName(userModel.getData().getUser().getName());

        }

        binding.setContactModel(contactUsModel);
        binding.btnSend.setOnClickListener(view -> {
            if (contactUsModel.isDataValid(this)) {
                contactusActivityMvvm.contactus(this, contactUsModel);
            }
        });
        contactusActivityMvvm.send.observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


}
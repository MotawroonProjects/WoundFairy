package com.apps.wound_fairy.uis.activity_login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.CountryAdapter;
import com.apps.wound_fairy.databinding.ActivityLoginBinding;
import com.apps.wound_fairy.databinding.DialogCountriesBinding;
import com.apps.wound_fairy.model.CountryModel;
import com.apps.wound_fairy.model.LoginModel;
import com.apps.wound_fairy.mvvm.ActivityLoginMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_sign_up.SignUpActivity;
import com.apps.wound_fairy.uis.activity_verification_code.VerificationCodeActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private ActivityLoginMvvm activityLoginMvvm;
    private ActivityResultLauncher<Intent> launcher;
    private List<CountryModel> countryModelList = new ArrayList<>();
    private CountryAdapter countriesAdapter;
    private AlertDialog dialog;
    private String lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }


    private void initView() {

        setUpToolbar(binding.toolbar, getString(R.string.login), R.color.white, R.color.black);

        activityLoginMvvm = ViewModelProviders.of(this).get(ActivityLoginMvvm.class);
        model = new LoginModel();
        binding.setModel(model);
        activityLoginMvvm.smscode.observe(this, smsCode -> {
            binding.edtCode.setText(smsCode);
        });
        activityLoginMvvm.canresnd.observe(this, canResend -> {
            if (canResend) {
                binding.tvResend.setVisibility(View.VISIBLE);
                binding.tvResend.setEnabled(true);
            } else {
                binding.tvResend.setVisibility(View.GONE);
                binding.tvResend.setEnabled(false);
            }
        });
        activityLoginMvvm.timereturn.observe(this, time -> {
            binding.tvCounter.setText(time);
        });
        activityLoginMvvm.userModelMutableLiveData.observe(this, userModel -> {
            binding.flVerification.setVisibility(View.GONE);
            setUserModel(userModel);
            setResult(RESULT_OK);
            finish();
        });
        activityLoginMvvm.found.observe(this, s -> {
            if (s != null) {
                binding.flVerification.setVisibility(View.GONE);
                navigateToSignUpActivity();
            }
        });
        binding.tvResend.setOnClickListener(view -> activityLoginMvvm.sendSmsCode(getLang(), model.getPhone_code(), model.getPhone(), this));
        binding.btnConfirm.setOnClickListener(view -> {
            String smscode = binding.edtCode.getText().toString();
            if (!smscode.isEmpty()) {
                activityLoginMvvm.checkValidCode(smscode, this);
            } else {
                binding.edtCode.setError(getResources().getString(R.string.field_required));
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });
        activityLoginMvvm.getCoListMutableLiveData().observe(this, new Observer<List<CountryModel>>() {
            @Override
            public void onChanged(List<CountryModel> countryModels) {
                if (countryModels != null && countryModels.size() > 0) {
                    countryModelList.clear();
                    countryModelList.addAll(countryModels);
                }
            }
        });
        activityLoginMvvm.setCountry();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.btnLogin.setOnClickListener(v -> {
            if (model.isDataValid(this)) {
                binding.flVerification.setVisibility(View.VISIBLE);
                activityLoginMvvm.sendSmsCode(getLang(), model.getPhone_code(), model.getPhone(), this);
            }
        });
        binding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        sortCountries();
        createCountriesDialog();
    }


    private void createCountriesDialog() {

        dialog = new AlertDialog.Builder(this)
                .create();
        countriesAdapter = new CountryAdapter(this);
        countriesAdapter.updateList(countryModelList);
        DialogCountriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_countries, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(countriesAdapter);

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
    }

    private void sortCountries() {
        Collections.sort(countryModelList, (country1, country2) -> {
            return country1.getName().trim().compareToIgnoreCase(country2.getName().trim());
        });
    }

    private void navigateToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code", model.getPhone_code());
        intent.putExtra("phone", model.getPhone());
        launcher.launch(intent);
    }

    public void setItemData(CountryModel countryModel) {
        dialog.dismiss();
        model.setPhone_code(countryModel.getDialCode());
        binding.setModel(model);
    }


}
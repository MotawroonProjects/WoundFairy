package com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.FragmentAboutUsBinding;
import com.apps.wound_fairy.model.AboutAusModel;
import com.apps.wound_fairy.mvvm.FragmentAboutUsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.adapter.MyPagerAdapter;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;
import com.apps.wound_fairy.uis.activity_login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentAboutUs extends BaseFragment {
    private FragmentAboutUsBinding binding;

    private HomeActivity activity;
    private FragmentAboutUsMvvm fragmentAboutUsMvvm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentAboutUsMvvm = ViewModelProviders.of(this).get(FragmentAboutUsMvvm.class);
        fragmentAboutUsMvvm.getIsLoading().observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.progBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progBar.setVisibility(View.GONE);
                }
            }
        });
        fragmentAboutUsMvvm.getMutableLiveData().observe(activity, new Observer<AboutAusModel>() {
            @Override
            public void onChanged(AboutAusModel aboutAusModel) {
                if (aboutAusModel != null) {
                    binding.setModel(aboutAusModel.getData());
                }
            }
        });
        fragmentAboutUsMvvm.getAboutUs(getLang());

    }
}
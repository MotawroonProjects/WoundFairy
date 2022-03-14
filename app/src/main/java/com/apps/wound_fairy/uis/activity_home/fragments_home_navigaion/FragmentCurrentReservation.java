package com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.mvvm.FragmentCurrentReservisonMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.databinding.FragmentCurrentReservationBinding;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;


public class FragmentCurrentReservation extends BaseFragment {
    private FragmentCurrentReservationBinding binding;
    private FragmentCurrentReservisonMvvm fragmentCurrentReservisonMvvm;
    private HomeActivity activity;

    public static FragmentCurrentReservation newInstance() {
        FragmentCurrentReservation fragment = new FragmentCurrentReservation();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_reservation, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentCurrentReservisonMvvm = ViewModelProviders.of(this).get(FragmentCurrentReservisonMvvm.class);
        fragmentCurrentReservisonMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.cardNoData.setVisibility(View.GONE);


            }
            binding.swipeRefresh.setRefreshing(isLoading);
        });




        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));

    }


}
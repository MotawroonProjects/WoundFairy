package com.apps.wound_fairy.uis.activity_my_reservations.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ProductAdapter;
import com.apps.wound_fairy.adapter.ReservationAdapter;
import com.apps.wound_fairy.databinding.FragmentCurrentReservationsBinding;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.mvvm.FragmentCurrentReservisonMvvm;
import com.apps.wound_fairy.mvvm.FragmentMarketMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.uis.activity_my_reservations.MyReservationsActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentCurrentReservations extends BaseFragment {
    private FragmentCurrentReservationsBinding binding;
    private MyReservationsActivity activity;
    private FragmentCurrentReservisonMvvm mvvm;
    private ReservationAdapter reservationAdapter;
    private List<ReservationModel> reservationModelList;

    public static FragmentCurrentReservations newInstance() {
        FragmentCurrentReservations fragment = new FragmentCurrentReservations();
        Bundle args = new Bundle();
//        args.putSerializable("data", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MyReservationsActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_reservations, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        reservationModelList = new ArrayList<>();
        mvvm = ViewModelProviders.of(this).get(FragmentCurrentReservisonMvvm.class);

        mvvm.getIsLoading().observe(activity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.tvNoSearchData.setVisibility(View.GONE);
                }
                binding.swipeRefresh.setRefreshing(aBoolean);
            }
        });

        mvvm.getMutableLiveData().observe(activity, new Observer<List<ReservationModel>>() {
            @Override
            public void onChanged(List<ReservationModel> reservationModels) {
                reservationAdapter.updateList(new ArrayList<>());
                if (reservationModels!=null && reservationModels.size()>0){
                    reservationAdapter.updateList(reservationModels);
                    binding.tvNoSearchData.setVisibility(View.GONE);
                }else {
                    binding.tvNoSearchData.setVisibility(View.VISIBLE);
                }
            }
        });

        reservationAdapter = new ReservationAdapter(reservationModelList, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(reservationAdapter);

        mvvm.getCurrentReservations(getUserModel(),getLang());
        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getCurrentReservations(getUserModel(),getLang()));
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


    }
}
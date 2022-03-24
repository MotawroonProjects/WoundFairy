package com.apps.wound_fairy.uis.activity_my_reservations.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ReservationAdapter;
import com.apps.wound_fairy.databinding.FragmentPreviousReservationsBinding;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.mvvm.FragmentReservationsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.uis.activity_my_reservations.MyReservationsActivity;
import com.apps.wound_fairy.uis.activity_reservation_details.ReservationDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentPreviousReservations extends BaseFragment {
    private FragmentPreviousReservationsBinding binding;
    private MyReservationsActivity activity;
    private FragmentReservationsMvvm mvvm;
    private ReservationAdapter reservationAdapter;
    private List<ReservationModel> reservationModelList;

    public static FragmentPreviousReservations newInstance() {
        FragmentPreviousReservations fragment = new FragmentPreviousReservations();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_previous_reservations, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        reservationModelList = new ArrayList<>();
        mvvm = ViewModelProviders.of(this).get(FragmentReservationsMvvm.class);

        mvvm.getIsLoading().observe(activity, aBoolean -> {
            if (aBoolean){
                binding.tvNoSearchData.setVisibility(View.GONE);
            }
            binding.swipeRefresh.setRefreshing(aBoolean);
        });
        mvvm.getMutableLiveData().observe(activity, reservationModels -> {
            reservationAdapter.updateList(new ArrayList<>());
            if (reservationModels!=null && reservationModels.size()>0){
                reservationAdapter.updateList(reservationModels);
                binding.tvNoSearchData.setVisibility(View.GONE);
            }else {
                binding.tvNoSearchData.setVisibility(View.VISIBLE);
            }
        });

        reservationAdapter = new ReservationAdapter(reservationModelList, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(reservationAdapter);

        mvvm.getPreviousReservation(getUserModel(),getLang());
        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getPreviousReservation(getUserModel(),getLang()));
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

    }

    public void setItemPos(ReservationModel reservationModel, int adapterPosition) {
        Intent intent=new Intent(activity, ReservationDetailsActivity.class);
        intent.putExtra("data",reservationModel);
        startActivity(intent);
    }
}
package com.apps.wound_fairy.uis.activity_my_orders.fragments;

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
import com.apps.wound_fairy.adapter.OrderAdapter;
import com.apps.wound_fairy.databinding.FragmentPreviousOrdersBinding;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.mvvm.FragmentOrdersMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.uis.activity_my_orders.MyOrdersActivity;
import com.apps.wound_fairy.uis.activity_order_details.OrderDetailsActivity;


import java.util.ArrayList;
import java.util.List;


public class FragmentPreviousOrders extends BaseFragment {
    private FragmentPreviousOrdersBinding binding;
    private MyOrdersActivity activity;
    private FragmentOrdersMvvm mvvm;
    private OrderAdapter orderAdapter;
    private List<OrderModel> orderModelList;

    public static FragmentPreviousOrders newInstance() {
        FragmentPreviousOrders fragment = new FragmentPreviousOrders();
        Bundle args = new Bundle();
//        args.putSerializable("data", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MyOrdersActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_previous_orders, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        orderModelList = new ArrayList<>();
        mvvm = ViewModelProviders.of(this).get(FragmentOrdersMvvm.class);

        mvvm.getIsLoading().observe(activity, aBoolean -> {
            if (aBoolean){
                binding.tvNoSearchData.setVisibility(View.GONE);
            }
            binding.swipeRefresh.setRefreshing(aBoolean);
        });
        mvvm.getMutableLiveData().observe(activity, orderModels -> {
            orderAdapter.updateList(new ArrayList<>());
        if (orderModels!=null && orderModels.size()>0){
            orderAdapter.updateList(orderModels);
            binding.tvNoSearchData.setVisibility(View.GONE);
        }else {
            binding.tvNoSearchData.setVisibility(View.VISIBLE);
        }
        });

        orderAdapter = new OrderAdapter(orderModelList, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(orderAdapter);

        mvvm.getPreviousOrder(getUserModel(),getLang());
        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getPreviousOrder(getUserModel(),getLang()));
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

    }

    public void setItemPos(OrderModel orderModel, int adapterPosition) {
        Intent intent =new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra("data",orderModel);
        startActivity(intent);
    }
}
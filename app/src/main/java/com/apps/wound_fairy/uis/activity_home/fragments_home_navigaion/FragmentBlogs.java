package com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.BlogAdapter;
import com.apps.wound_fairy.databinding.FragmentBlogsBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.mvvm.FragmentBlogsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.uis.activity_blogs_details.BlogDetailsActivity;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentBlogs extends BaseFragment {
    private FragmentBlogsBinding binding;
    private FragmentBlogsMvvm mvvm;
    private HomeActivity activity;
    private BlogAdapter adapter;
    private List<BlogModel> blogModelList;

    public static FragmentBlogs newInstance() {
        FragmentBlogs fragment = new FragmentBlogs();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blogs, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        blogModelList=new ArrayList<>();
        adapter=new BlogAdapter(blogModelList,activity,this);
        mvvm = ViewModelProviders.of(this).get(FragmentBlogsMvvm.class);

        mvvm.getIsDataLoading().observe(activity, isLoading -> {
            if (isLoading){

                binding.cardNoData.setVisibility(View.GONE);
            }
                binding.swipeRefresh.setRefreshing(isLoading);

        });
        mvvm.getOnDataSuccess().observe(activity, list -> {
            adapter.updateList(new ArrayList<>());

            if (list!=null && list.size()>0){
                adapter.updateList(list);
                binding.cardNoData.setVisibility(View.GONE);
            }else{
                binding.cardNoData.setVisibility(View.VISIBLE);
            }
        });

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getBlogs(getLang()));
        mvvm.getBlogs(getLang());
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


    }


    public void navigateToDetails(String id) {
        Intent intent=new Intent(activity, BlogDetailsActivity.class);
        intent.putExtra("data",id);
        startActivity(intent);
    }
}
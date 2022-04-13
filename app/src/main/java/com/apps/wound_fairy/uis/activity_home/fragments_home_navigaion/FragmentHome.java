package com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;

import com.apps.wound_fairy.adapter.BlogAdapter;
import com.apps.wound_fairy.adapter.BlogSliderAdapter;
import com.apps.wound_fairy.adapter.SliderAdapter;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.model.MessageModel;
import com.apps.wound_fairy.model.SliderDataModel;
import com.apps.wound_fairy.mvvm.FragmentHomeMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.databinding.FragmentHomeBinding;
import com.apps.wound_fairy.uis.activity_blogs_details.BlogDetailsActivity;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;
import com.apps.wound_fairy.uis.activity_services.ServicesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentHome extends BaseFragment {
    private static final String TAG = FragmentHome.class.getName();
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private FragmentHomeMvvm fragmentHomeMvvm;
    private SliderAdapter sliderAdapter;
    private BlogSliderAdapter blogSliderAdapter;
    private List<BlogModel> blogModelList;
    private List<SliderDataModel.SliderModel> sliderModelList;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Timer timer;
    private Timer timer2;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;
    private String type="";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Observable.timer(130, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        initView();

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView() {
        binding.setLang(getLang());
        sliderModelList = new ArrayList<>();
        blogModelList=new ArrayList<>();
        fragmentHomeMvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        fragmentHomeMvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.progBarSlider.setVisibility(View.VISIBLE);

            }
            // binding.swipeRefresh.setRefreshing(isLoading);
        });

        fragmentHomeMvvm.getSliderDataModelMutableLiveData().observe(activity, new androidx.lifecycle.Observer<SliderDataModel>() {
            @Override
            public void onChanged(SliderDataModel sliderDataModel) {

                if (sliderDataModel.getData() != null) {
                    binding.progBarSlider.setVisibility(View.GONE);
                    sliderModelList.clear();
                    sliderModelList.addAll(sliderDataModel.getData());
                    sliderAdapter.notifyDataSetChanged();
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);
                }

            }
        });
        fragmentHomeMvvm.getBlogDataModelMutableLiveData().observe(activity, new androidx.lifecycle.Observer<List<BlogModel>>() {
            @Override
            public void onChanged(List<BlogModel> list) {
                if (list!=null && list.size()>0){
                    binding.progBarBlog.setVisibility(View.GONE);
                    blogModelList.clear();
                    blogModelList.addAll(list);
                    blogSliderAdapter.notifyDataSetChanged();
                    timer2=new Timer();
                    timer2.scheduleAtFixedRate(new MyTask2(),3000,3000);
                }
            }
        });

        sliderAdapter = new SliderAdapter(sliderModelList, activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(80, 0, 80, 0);
        binding.pager.setPageMargin(20);
        fragmentHomeMvvm.getSlider();

        blogSliderAdapter=new BlogSliderAdapter(blogModelList,activity,this);
        binding.pagerBlog.setAdapter(blogSliderAdapter);
        binding.pagerBlog.setClipToPadding(false);
         binding.pagerBlog.setPadding(20, 0, 20, 20);
        binding.pagerBlog.setPageMargin(10);
        binding.tab.setViewPager(binding.pagerBlog);
        fragmentHomeMvvm.getBlogs(getLang());

        binding.llOnline.setOnClickListener(view -> {
            type="online";
            Intent intent=new Intent(activity, ServicesActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        });
        binding.llHomeVisit.setOnClickListener(view -> {
            type="visit";
            Intent intent=new Intent(activity,ServicesActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        });

        binding.llSeeAll.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.blogs));
        binding.cardMarket.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.market));
    }

    public void navigateToDetails(String id) {
        Intent intent=new Intent(activity, BlogDetailsActivity.class);
        intent.putExtra("data",id);
        startActivity(intent);
    }


    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }

    }
    public class MyTask2 extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pagerBlog.getCurrentItem();
                if (current_page < blogSliderAdapter.getCount() - 1) {
                    binding.pagerBlog.setCurrentItem(binding.pagerBlog.getCurrentItem() + 1);
                } else {
                    binding.pagerBlog.setCurrentItem(0);

                }
            });

        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }


}
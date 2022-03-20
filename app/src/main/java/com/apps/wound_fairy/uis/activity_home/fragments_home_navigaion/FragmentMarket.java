package com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ProductAdapter;
import com.apps.wound_fairy.databinding.FragmentMarketBinding;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.mvvm.FragmentBlogsMvvm;
import com.apps.wound_fairy.mvvm.FragmentMarketMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;
import com.apps.wound_fairy.uis.activity_login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;


public class FragmentMarket extends BaseFragment {
    private static final String TAG = FragmentMarket.class.getName();
    private HomeActivity activity;
    private FragmentMarketBinding binding;
    private FragmentMarketMvvm mvvm;
    private ProductAdapter productAdapter;
    private List<ProductModel.Product> productList;
    private boolean login;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        Log.e(TAG, "onViewCreated: ");

    }

    private void initView() {
        productList=new ArrayList<>();
        mvvm = ViewModelProviders.of(this).get(FragmentMarketMvvm.class);

        mvvm.getIsDataLoading().observe(activity, aBoolean -> {
            if (aBoolean){
                binding.tvNoSearchData.setVisibility(View.GONE);
            }
            binding.swipeRefresh.setRefreshing(aBoolean);
        });
        mvvm.getOnDataSuccess().observe(activity, products -> {
            productAdapter.updateList(new ArrayList<>());
            if (products!=null && products.size()>0){
           productAdapter.updateList(products);
           binding.tvNoSearchData.setVisibility(View.GONE);
       }else{
           binding.tvNoSearchData.setVisibility(View.VISIBLE);
       }
        });

        productAdapter=new ProductAdapter(productList,activity);
        binding.recView.setLayoutManager(new GridLayoutManager(activity,2));
        binding.recView.setAdapter(productAdapter);
        mvvm.getProducts(getLang(),binding.edtSearch.getText().toString());
        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getProducts(getLang(),binding.edtSearch.getText().toString()));

        Observable.create(emitter -> {
            binding.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emitter.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        })
                .debounce(2, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribe(query -> {
                    mvvm.getProducts(getLang(),query.toString());

                });


        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


    }

    private void navigateToLoginActivity() {
        req = 1;
        Intent intent = new Intent(activity, LoginActivity.class);
        launcher.launch(intent);

    }



    private void rateApp() {
        String appId = activity.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = activity.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            startActivity(webIntent);
        }

    }

    private void navigateToFragmentApp(View v, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("data", type);
      //  Navigation.findNavController(v).navigate(R.id.appFragment, bundle);

    }


}
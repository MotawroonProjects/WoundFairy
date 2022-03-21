package com.apps.wound_fairy.uis.activity_product_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ImagesSliderAdapter;
import com.apps.wound_fairy.adapter.SliderAdapter;
import com.apps.wound_fairy.databinding.ActivityProductDetailsBinding;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.SliderProductModel;
import com.apps.wound_fairy.mvvm.ActivityBlogDetailsMvvm;
import com.apps.wound_fairy.mvvm.ActivityProductDetailsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion.FragmentHome;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailsActivity extends BaseActivity {
    private ActivityProductDetailsBinding binding;
    private ActivityProductDetailsMvvm mvvm;
//    private ImagesSliderAdapter sliderAdapter;
//    private Timer timer;
//    private List<ProductModel.Product.Images> imagesList;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_product_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }
    private void initView() {

//        imagesList=new ArrayList<>();
        binding.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(ActivityProductDetailsMvvm.class);

        mvvm.getIsDataLoading().observe(this, aBoolean -> {
            if (aBoolean){
                binding.progBar.setVisibility(View.VISIBLE);
//                binding.progBarSlider.setVisibility(View.VISIBLE);
            }else {
                binding.progBar.setVisibility(View.GONE);
//                binding.progBarSlider.setVisibility(View.GONE);
            }
        });
        mvvm.getOnDataSuccess().observe(this, product -> {
            if (product!=null){
                binding.setModel(product);
            }
        });
        mvvm.getSingleProduct(id,getLang());
        binding.llBack.setOnClickListener(view -> finish());
    }
//        mvvm.getSliderDataModelMutableLiveData().observe(this, new Observer<SliderProductModel>() {
//            @Override
//            public void onChanged(SliderProductModel sliderProductModel) {
//                if (sliderProductModel.getData() != null) {
//                    binding.progBarSlider.setVisibility(View.GONE);
//                    imagesList.clear();
//                    imagesList.addAll(sliderProductModel.getData());
//                    sliderAdapter.notifyDataSetChanged();
//                    timer = new Timer();
//                    timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);
//                }
//            }
//        });
//        sliderAdapter = new ImagesSliderAdapter(imagesList, this);
//        binding.pagerProduct.setAdapter(sliderAdapter);
//        binding.pagerProduct.setClipToPadding(false);
//        binding.pagerProduct.setPadding(80, 0, 80, 0);
//        binding.pagerProduct.setPageMargin(20);
//        mvvm.getSlider();


//    public class MyTask extends TimerTask {
//        @Override
//        public void run() {
//            runOnUiThread(() -> {
//                int current_page = binding.pagerProduct.getCurrentItem();
//                if (current_page < sliderAdapter.getCount() - 1) {
//                    binding.pagerProduct.setCurrentItem(binding.pagerProduct.getCurrentItem() + 1);
//                } else {
//                    binding.pagerProduct.setCurrentItem(0);
//
//                }
//            });
//
//        }
//
//    }
}
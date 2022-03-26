package com.apps.wound_fairy.uis.activity_product_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.apps.wound_fairy.uis.activity_send_order.SendOrderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailsActivity extends BaseActivity {
    private ActivityProductDetailsBinding binding;
    private ActivityProductDetailsMvvm mvvm;
    private ImagesSliderAdapter sliderAdapter;
    private Timer timer;
    private List<ProductModel.Product.Images> imagesList;
    private String id;
    private int amount = 1;
    private ProductModel.Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    private void initView() {

        imagesList = new ArrayList<>();
        binding.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(ActivityProductDetailsMvvm.class);

        mvvm.getIsDataLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.progBar.setVisibility(View.VISIBLE);
                // binding.progBarSlider.setVisibility(View.VISIBLE);
            } else {
                binding.progBar.setVisibility(View.GONE);
//                binding.progBarSlider.setVisibility(View.GONE);
            }
        });
        mvvm.getOnDataSuccess().observe(this, product -> {
            if (product != null) {
                this.product = product;
                binding.scrollView.setVisibility(View.VISIBLE);
                binding.cardBottom.setVisibility(View.VISIBLE);
                binding.tvCount.setText(amount + "");
                binding.setModel(product);
                Spannable word = new SpannableString(product.getPrice());

                word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvPrice.setText(word);
                Spannable wordTwo = new SpannableString(getResources().getString(R.string.egp));

                wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.tvPrice.append(wordTwo);
                imagesList.clear();
                if (product.getImages().size() > 0) {
                    imagesList.addAll(product.getImages());
                    sliderAdapter.notifyDataSetChanged();
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);
                    binding.fl.setVisibility(View.VISIBLE);
                    binding.image.setVisibility(View.GONE);
                    binding.pagerProduct.setVisibility(View.VISIBLE);
                } else {
                    binding.fl.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                    binding.pagerProduct.setVisibility(View.GONE);
                }
            }
        });
        mvvm.getSingleProduct(id, getLang());
        binding.llBack.setOnClickListener(view -> finish());
        sliderAdapter = new ImagesSliderAdapter(imagesList, this);
        binding.pagerProduct.setAdapter(sliderAdapter);
        binding.pagerProduct.setClipToPadding(false);
        // binding.pagerProduct.setPadding(80, 0, 80, 0);
        //binding.pagerProduct.setPageMargin(20);
        binding.tab.setViewPager(binding.pagerProduct);
        binding.imIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount += 1;
                binding.tvCount.setText(amount + "");
            }
        });
        binding.imDecreese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 1) {
                    amount -= 1;
                    binding.tvCount.setText(amount + "");
                }
            }
        });
        binding.llResuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailsActivity.this, SendOrderActivity.class);
                intent.putExtra("data", product);
                intent.putExtra("amount", amount);
                startActivity(intent);
                finish();
            }
        });
    }


    public class MyTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(() -> {
                int current_page = binding.pagerProduct.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pagerProduct.setCurrentItem(binding.pagerProduct.getCurrentItem() + 1);
                } else {
                    binding.pagerProduct.setCurrentItem(0);

                }
            });

        }

    }
}
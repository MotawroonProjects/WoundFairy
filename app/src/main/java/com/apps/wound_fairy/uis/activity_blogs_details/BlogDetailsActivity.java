package com.apps.wound_fairy.uis.activity_blogs_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityBlogDetailsBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.mvvm.ActivityBlogDetailsMvvm;
import com.apps.wound_fairy.mvvm.FragmentBlogsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

import java.util.ArrayList;

public class BlogDetailsActivity extends BaseActivity {
    private ActivityBlogDetailsBinding binding;
    private ActivityBlogDetailsMvvm mvvm;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_blog_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        id =  intent.getStringExtra("data");

    }
    private void initView() {

        setUpToolbar(binding.toolbar, getString(R.string.blog_details), R.color.white, R.color.black);
        binding.setLang(getLang());


        mvvm = ViewModelProviders.of(this).get(ActivityBlogDetailsMvvm.class);

        mvvm.getIsDataLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progBar.setVisibility(View.VISIBLE);
            } else {
                binding.progBar.setVisibility(View.GONE);
            }
        });

        mvvm.getOnDataSuccess().observe(this, blogModel -> {
            if (blogModel!=null){
                binding.setModel(blogModel);
            }
        });
        mvvm.getSingleBlog(id,getLang());
    }
}
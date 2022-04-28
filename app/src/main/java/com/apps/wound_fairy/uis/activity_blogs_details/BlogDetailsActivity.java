package com.apps.wound_fairy.uis.activity_blogs_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityBlogDetailsBinding;
import com.apps.wound_fairy.databinding.BlogRowBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.mvvm.ActivityBlogDetailsMvvm;
import com.apps.wound_fairy.mvvm.FragmentBlogsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;

public class BlogDetailsActivity extends BaseActivity {
    private ActivityBlogDetailsBinding binding;
    private ActivityBlogDetailsMvvm mvvm;
    private String id;
    private boolean isInFullScreen = false;
    private BlogModel blogModel;
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
                this.blogModel=blogModel;
                if (blogModel.getVideo() != null) {
                    binding.image.setVisibility(View.GONE);
                    setUpYoutube( blogModel.getVideo());



                }else{
                    binding.flvideo.setVisibility(View.GONE);
                }
            }
        });

        mvvm.getSingleBlog(id,getLang());
    }
    private void setUpYoutube( String url) {
        String videoId = extractYTId(url);
        if (videoId != null) {

            binding.youtubePlayerView.setEnableAutomaticInitialization(false);
            binding.youtubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlayer.cueVideo(videoId, 0);

                }
            }, true);


        } else {
        }


    }

    private String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile("^https?://.*(?:www\\.youtube\\.com/|v/|u/\\w/|embed/|watch\\?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

}
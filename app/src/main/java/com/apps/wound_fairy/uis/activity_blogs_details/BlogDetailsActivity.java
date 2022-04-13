package com.apps.wound_fairy.uis.activity_blogs_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityBlogDetailsBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.mvvm.ActivityBlogDetailsMvvm;
import com.apps.wound_fairy.mvvm.FragmentBlogsMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class BlogDetailsActivity extends BaseActivity {
    private ActivityBlogDetailsBinding binding;
    private ActivityBlogDetailsMvvm mvvm;
    private String id;
    private ExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private DefaultTrackSelector trackSelector;
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
                    getVideoImage();
                    setupPlayer();

                }else{
                    binding.exoPlayer.setVisibility(View.GONE);
                    binding.flVideo.setVisibility(View.GONE);
                }
            }
        });
        binding.flVideo.setOnClickListener(v -> {
            isInFullScreen = true;

            if (player != null) {
                player.setPlayWhenReady(true);
            }


        });
        mvvm.getSingleBlog(id,getLang());
    }
    private void getVideoImage() {

        int microSecond = 6000000;// 6th second as an example
        Uri uri = Uri.parse(blogModel.getVideo());
        RequestOptions options = new RequestOptions().frame(microSecond).override(binding.imageVideo.getWidth(), binding.imageVideo.getHeight());
        Glide.with(this).asBitmap()
                .load(uri)
                .centerCrop()
                .apply(options)
                .into(binding.imageVideo);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPlayer() {

        if (blogModel.getVideo() != null) {
            trackSelector = new DefaultTrackSelector(this);
            dataSourceFactory = new DefaultDataSource.Factory(this);
            MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(blogModel.getVideo()));

            player = new ExoPlayer.Builder(this)
                    .setTrackSelector(trackSelector)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .build();

            player.setMediaItem(mediaItem);
            player.setPlayWhenReady(false);
            player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
            binding.exoPlayer.setPlayer(player);
            player.prepare();

            binding.exoPlayer.setOnTouchListener((v, event) -> {
                if (player != null && player.isPlaying()) {
                    player.setPlayWhenReady(false);
                } else if (player != null && !player.isPlaying()) {

                    player.setPlayWhenReady(true);

                }
                return false;
            });


        }


    }

    public boolean isFullScreen() {
        return isInFullScreen;
    }

    public void setToNormalScreen() {

        isInFullScreen = false;
        if (player != null) {
            player.setPlayWhenReady(false);
        }


    }

    @Override
    public void onResume() {
        if ((Util.SDK_INT <= 23 || player == null) && blogModel != null) {
            setupPlayer();
        }
        super.onResume();


    }

    @Override
    public void onStart() {
        if (Util.SDK_INT > 23) {
            if (player == null && blogModel != null) {
                setupPlayer();
                binding.exoPlayer.onResume();
            }


        }
        super.onStart();


    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            if (player != null) {
                player.setPlayWhenReady(false);
            }
        }
        super.onPause();


    }

}
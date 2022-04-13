package com.apps.wound_fairy.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.ExoPlayer;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.BlogRowBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion.FragmentHome;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogSliderAdapter extends PagerAdapter {
    private List<BlogModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private ExoPlayer player;
    private int currentWindow = 0;
    private long currentPosition = 0;
    private boolean playWhenReady = false;
    private PlayerView playerView;
    private ProgressBar progressBar;

    public BlogSliderAdapter(List<BlogModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;
        BlogModel model = list.get(position);

        if (model.getVideo() != null) {
            view = inflater.inflate(R.layout.slider_video, container, false);
            playerView = view.findViewById(R.id.player);
            progressBar = view.findViewById(R.id.progBarBuffering);
            TextView tvtime=view.findViewById(R.id.tvtime);
            TextView tvtitle=view.findViewById(R.id.tvTitle);
            TextView tvDetials=view.findViewById(R.id.tvDetials);
tvtime.setText(model.getDate_time());
tvtitle.setText(model.getTitle());
tvDetials.setText(Html.fromHtml(model.getDetails()).toString());
            Uri uri = Uri.parse(model.getVideo());

            initPlayer(playerView, progressBar, uri);


        } else {
            view = inflater.inflate(R.layout.blog_row, container, false);
            ImageView imageView = view.findViewById(R.id.image);
            Picasso.get().load(Uri.parse(model.getImage())).into(imageView);
            TextView tvtime=view.findViewById(R.id.tvtime);
            TextView tvtitle=view.findViewById(R.id.tvTitle);
            TextView tvDetials=view.findViewById(R.id.tvDetials);
            tvtime.setText(model.getDate_time());
            tvtitle.setText(model.getTitle());
            tvDetials.setText(Html.fromHtml(model.getDetails()).toString());
        }
       view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof FragmentHome){
                    FragmentHome fragmentHome=(FragmentHome) fragment;
                    fragmentHome.navigateToDetails(list.get(position).getId());
                }
            }
        });
        container.addView(view);


        return view;
    }


    private void initPlayer(PlayerView playerView, ProgressBar progBarBuffering, Uri uri) {

        Log.e("vid", uri.toString());

        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());
            DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(context);

            MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);

            player = new ExoPlayer.Builder(context)
                    .setTrackSelector(trackSelector)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .build();
            playerView.setPlayer(player);
            Log.e("1", "1");
        } else {
            currentWindow = 0;
            currentPosition = 0;
            Log.e("2", "2");

        }

        MediaItem mediaItem = MediaItem.fromUri(uri);

        player.seekTo(currentWindow, currentPosition);
        player.setPlayWhenReady(playWhenReady);
        player.setMediaItem(mediaItem);

        playerView.setOnTouchListener((v, event) -> {
            if (player != null && player.isPlaying()) {
                player.setPlayWhenReady(false);
            } else if (player != null && !player.isPlaying()) {

                player.setPlayWhenReady(true);

            }
            return false;
        });


    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        BlogModel model = list.get(position);
        if (model.getVideo()!=null) {
            progressBar.setVisibility(View.GONE);
            releasePlayer();
            Log.e("release", "release");
        }
        container.removeView((View) object);
    }

    public void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            currentWindow = player.getCurrentWindowIndex();
            currentPosition = player.getCurrentPosition();
            player.stop();

        }
    }
}

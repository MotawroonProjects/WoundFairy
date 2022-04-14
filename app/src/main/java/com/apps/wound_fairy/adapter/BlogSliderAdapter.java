package com.apps.wound_fairy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.BlogRowBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion.FragmentHome;

import java.util.List;

public class BlogSliderAdapter extends PagerAdapter {
    private List<BlogModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public BlogSliderAdapter(List<BlogModel> list, Context context,Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        BlogRowBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.blog_row, view, false);
        rowBinding.setModel(list.get(position));
        if (list.get(position).getVideo() != null) {
            rowBinding.image.setVisibility(View.GONE);
            rowBinding.webView.loadUrl(list.get(position).getVideo());



        }else{
            rowBinding.flvideo.setVisibility(View.GONE);
        }
        rowBinding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        rowBinding.webView.getSettings().setJavaScriptEnabled(true);
        rowBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                rowBinding.webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                rowBinding.progBarVideo.setVisibility(View.GONE);

            }


        });
        view.addView(rowBinding.getRoot());
        rowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment instanceof FragmentHome){
                    FragmentHome fragmentHome=(FragmentHome) fragment;
                    fragmentHome.navigateToDetails(list.get(position).getId());
                }
            }
        });
        return rowBinding.getRoot();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}

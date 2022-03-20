package com.apps.wound_fairy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.BlogRowBinding;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.uis.activity_home.HomeActivity;
import com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion.FragmentBlogs;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<BlogModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    private Fragment fragment;

    public BlogAdapter(List<BlogModel> list, Context context,Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BlogRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.blog_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentBlogs){
                FragmentBlogs fragmentBlogs=(FragmentBlogs) fragment;
                fragmentBlogs.navigateToDetails(list.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        BlogRowBinding binding;

        public MyHolder(BlogRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateList(List<BlogModel> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}

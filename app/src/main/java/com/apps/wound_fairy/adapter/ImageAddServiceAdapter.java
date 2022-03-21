package com.apps.wound_fairy.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.AddImagesRowBinding;
import com.apps.wound_fairy.uis.activity_request_service.RequestServiceActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAddServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private RequestServiceActivity activity;

    public ImageAddServiceAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (RequestServiceActivity) context;


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddImagesRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.add_images_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        Picasso.get().load(Uri.parse(list.get(position))).fit().into(myHolder.binding.image);
        myHolder.binding.cardViewDelete.setOnClickListener(view -> {
            activity.deleteImage(myHolder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AddImagesRowBinding binding;
        public MyHolder(@NonNull AddImagesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

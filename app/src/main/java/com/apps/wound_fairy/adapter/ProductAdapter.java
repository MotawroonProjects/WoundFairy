package com.apps.wound_fairy.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ProductRowBinding;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.uis.activity_home.fragments_home_navigaion.FragmentMarket;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel.Product> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public ProductAdapter(List<ProductModel.Product> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        Spannable word = new SpannableString(list.get(position).getPrice());

        word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        myHolder.binding.tvPrice.setText(word);
        Spannable wordTwo = new SpannableString(context.getResources().getString(R.string.egp));

        wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        myHolder.binding.tvPrice.append(wordTwo);
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentMarket){
                FragmentMarket fragmentMarket=(FragmentMarket) fragment;
                fragmentMarket.navigateToDetails(list.get(position).getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ProductRowBinding binding;

        public MyHolder(ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateList(List<ProductModel.Product> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}

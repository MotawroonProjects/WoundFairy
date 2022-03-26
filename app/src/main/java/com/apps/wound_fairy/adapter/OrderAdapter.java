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
import com.apps.wound_fairy.databinding.OrderRowBinding;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.uis.activity_base.BaseFragment;
import com.apps.wound_fairy.uis.activity_my_orders.fragments.FragmentCurrentOrders;
import com.apps.wound_fairy.uis.activity_my_orders.fragments.FragmentPreviousOrders;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    private Fragment fragment;
    private String lang;


    public OrderAdapter(List<OrderModel> list, Context context, Fragment fragment,String lang) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        this.lang=lang;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.order_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentCurrentOrders){
                FragmentCurrentOrders fragmentCurrentOrders=(FragmentCurrentOrders) fragment;
                fragmentCurrentOrders.setItemPos(list.get(myHolder.getAbsoluteAdapterPosition()),myHolder.getAdapterPosition());
            }else if (fragment instanceof FragmentPreviousOrders){
                FragmentPreviousOrders fragmentPreviousOrders=(FragmentPreviousOrders) fragment;
                fragmentPreviousOrders.setItemPos(list.get(myHolder.getAbsoluteAdapterPosition()),myHolder.getAdapterPosition());
            }
        });

        Spannable word = new SpannableString(list.get(position).getTotal_price());

        word.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        myHolder.binding.tvPrice.setText(word);
        Spannable wordTwo = new SpannableString(context.getResources().getString(R.string.egp));

        wordTwo.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        myHolder.binding.tvPrice.append(wordTwo);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        OrderRowBinding binding;

        public MyHolder(OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateList(List<OrderModel> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}

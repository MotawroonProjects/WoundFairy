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
import com.apps.wound_fairy.databinding.ReservationRowBinding;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.uis.activity_my_reservations.fragments.FragmentCurrentReservations;
import com.apps.wound_fairy.uis.activity_my_reservations.fragments.FragmentPreviousReservations;

import java.util.List;

public class ReservationAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ReservationModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;
    private Fragment fragment;
    private String lang;

    public ReservationAdapter(List<ReservationModel> list, Context context, Fragment fragment,String lang) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        this.lang=lang;
        inflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReservationRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.reservation_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentCurrentReservations){
                FragmentCurrentReservations fragmentCurrentReservations=(FragmentCurrentReservations) fragment;
                fragmentCurrentReservations.setItemPos(list.get(myHolder.getAbsoluteAdapterPosition()),myHolder.getAdapterPosition());
            }else if (fragment instanceof FragmentPreviousReservations){
                FragmentPreviousReservations fragmentPreviousReservations=(FragmentPreviousReservations) fragment;
                fragmentPreviousReservations.setItemPos(list.get(myHolder.getAbsoluteAdapterPosition()),myHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        ReservationRowBinding binding;

        public MyHolder(ReservationRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public void updateList(List<ReservationModel> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}

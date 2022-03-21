package com.apps.wound_fairy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.SpinnerDepartmentRowBinding;
import com.apps.wound_fairy.model.ServiceDepartmentModel;

import java.util.List;

public class SpinnerDepartmentAdapter extends BaseAdapter{
    private List<ServiceDepartmentModel.Department> dataList;
    private Context context;

    public SpinnerDepartmentAdapter( Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (dataList == null)
            return 0;
        else
            return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerDepartmentRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_department_row, viewGroup, false);
        binding.setTitle(dataList.get(i).getTitle());
        return binding.getRoot();
    }

    public void updateList(List<ServiceDepartmentModel.Department> dataList){
        if (dataList!=null){
            this.dataList = dataList;
        }
        notifyDataSetChanged();
    }
}

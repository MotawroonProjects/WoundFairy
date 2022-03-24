package com.apps.wound_fairy.uis.activity_reservation_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ImageAddServiceAdapter;
import com.apps.wound_fairy.databinding.ActivityReservationDetailsBinding;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

public class ReservationDetailsActivity extends BaseActivity {
    private ActivityReservationDetailsBinding binding;
    private ReservationModel reservationModel;
    private ImageAddServiceAdapter imageAddServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_reservation_details);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent(){
        Intent intent=getIntent();
        reservationModel= (ReservationModel) intent.getSerializableExtra("data");
    }
    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.reservationDetails), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        binding.setReservationModel(reservationModel);

        imageAddServiceAdapter = new ImageAddServiceAdapter(reservationModel.getImages(), this);
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewImages.setAdapter(imageAddServiceAdapter);
    }
}
package com.apps.wound_fairy.uis.activity_confirm_request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ImageAddServiceAdapter;
import com.apps.wound_fairy.databinding.ActivityConfirmRequestBinding;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.mvvm.ActivityConfirmRequestMvvm;
import com.apps.wound_fairy.mvvm.ActivityServicesMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_my_reservations.MyReservationsActivity;
import com.apps.wound_fairy.uis.activity_send_order.SendOrderActivity;

import java.util.List;

public class ConfirmRequestActivity extends BaseActivity {
    private ActivityConfirmRequestBinding binding;
    private RequestServiceModel requestServiceModel;
    private ReservationModel reservationModel;
    private ActivityConfirmRequestMvvm mvvm;
    private ImageAddServiceAdapter imageAddServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_request);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        requestServiceModel = (RequestServiceModel) intent.getSerializableExtra("data");

        if (intent != null) {
            if (intent.getSerializableExtra("reservation") != null) {
                reservationModel = (ReservationModel) intent.getSerializableExtra("reservation");

            }
        }

    }

    private void initView() {
        binding.setLang(getLang());
        setUpToolbar(binding.toolbar, getString(R.string.confirm_request), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());



        binding.setModel(requestServiceModel);

        imageAddServiceAdapter = new ImageAddServiceAdapter(requestServiceModel.getImages(), this);
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewImages.setAdapter(imageAddServiceAdapter);

        mvvm = ViewModelProviders.of(this).get(ActivityConfirmRequestMvvm.class);

        mvvm.getReservation().observe(this, new Observer<ReservationModel>() {
            @Override
            public void onChanged(ReservationModel reservationModel) {
                if (reservationModel != null) {
                    Toast.makeText(ConfirmRequestActivity.this, getResources().getString(R.string.succ), Toast.LENGTH_LONG).show();

                    Intent intent = getIntent();
                    intent.putExtra("data", reservationModel);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
                mvvm.getConfirmMutableLiveData().observe(this, aBoolean -> {
                    if (aBoolean) {
                        Toast.makeText(ConfirmRequestActivity.this, getResources().getString(R.string.succ), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ConfirmRequestActivity.this, MyReservationsActivity.class);
                        startActivity(intent);
                    }
                });


        binding.btnConfirm.setOnClickListener(view -> {
            if (reservationModel==null){
                mvvm.confirmRequest(ConfirmRequestActivity.this, requestServiceModel, getUserModel());
            }else {
                mvvm.updateRequest(ConfirmRequestActivity.this,requestServiceModel,getUserModel(),reservationModel);
            }

        });


    }
}
package com.apps.wound_fairy.uis.activity_reservation_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ImageAddServiceAdapter;
import com.apps.wound_fairy.databinding.ActivityReservationDetailsBinding;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_confirm_request.ConfirmRequestActivity;
import com.apps.wound_fairy.uis.activity_request_service.RequestServiceActivity;

public class ReservationDetailsActivity extends BaseActivity {
    private ActivityReservationDetailsBinding binding;
    private ReservationModel reservationModel;
    private ImageAddServiceAdapter imageAddServiceAdapter;
    private ActivityResultLauncher<Intent> launcher;

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

        if (reservationModel.getImages().isEmpty()){
            binding.tvImages.setVisibility(View.GONE);
            binding.recViewImages.setVisibility(View.GONE);
        }
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        reservationModel = (ReservationModel) result.getData().getSerializableExtra("data");
                        binding.setReservationModel(reservationModel);
                        imageAddServiceAdapter.updateList(reservationModel.getImages());
                        if (!reservationModel.getImages().isEmpty()){
                            binding.tvImages.setVisibility(View.VISIBLE);
                            binding.recViewImages.setVisibility(View.VISIBLE);
                        }
                    }
                });
        imageAddServiceAdapter = new ImageAddServiceAdapter(reservationModel.getImages(), this);
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewImages.setAdapter(imageAddServiceAdapter);

        binding.btnEdit.setOnClickListener(view -> {
            Intent intent=new Intent(ReservationDetailsActivity.this, RequestServiceActivity.class);
            intent.putExtra("reservation",reservationModel);
            launcher.launch(intent);
        });
    }
}
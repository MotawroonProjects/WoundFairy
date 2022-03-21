package com.apps.wound_fairy.uis.activity_request_service;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.SpinnerDepartmentAdapter;
import com.apps.wound_fairy.databinding.ActivityRequestServiceBinding;
import com.apps.wound_fairy.model.LocationModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.mvvm.ActivityMapMvvm;
import com.apps.wound_fairy.mvvm.ActivityRequestServiceMvvm;
import com.apps.wound_fairy.mvvm.ActivityServicesMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_confirm_request.ConfirmRequestActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequestServiceActivity extends BaseActivity implements OnMapReadyCallback {
    private ActivityRequestServiceBinding binding;
    private GoogleMap mMap;
    private float zoom = 15.0f;
    private ActivityMapMvvm activitymapMvvm;
    private ActivityRequestServiceMvvm mvvm;
    private LocationModel locationmodel;
    private ServiceDepartmentModel serviceDepartmentModel;
    private SpinnerDepartmentAdapter spinnerDepartmentAdapter;
    private RequestServiceModel requestServiceModel;
    private ActivityResultLauncher<String> permissionLauncher;
    private String selected_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_service);
        initView();
    }

    private void initView() {
        binding.setLang(getLang());
        setUpToolbar(binding.toolbar, getString(R.string.request_service), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        mvvm = ViewModelProviders.of(this).get(ActivityRequestServiceMvvm.class);


        spinnerDepartmentAdapter = new SpinnerDepartmentAdapter(this);
        binding.spinnerDepartment.setAdapter(spinnerDepartmentAdapter);

        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    requestServiceModel.setService_id(0);
                }else {
                    requestServiceModel.setService_id(Integer.parseInt(mvvm.getServiceMutableLiveData().getValue().get(i).getId()));
                    Log.e("id",Integer.parseInt(mvvm.getServiceMutableLiveData().getValue().get(i).getId())+"_");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mvvm.getServiceMutableLiveData().observe(this, new Observer<List<ServiceDepartmentModel.Department>>() {
            @Override
            public void onChanged(List<ServiceDepartmentModel.Department> departments) {
                if (spinnerDepartmentAdapter != null) {
                    departments.add(0, new ServiceDepartmentModel.Department(getResources().getString(R.string.choose_service)));
                    spinnerDepartmentAdapter.updateList(departments);
                }
            }
        });
        mvvm.getServiceDepartment(getLang());

        requestServiceModel = new RequestServiceModel();

        binding.flDate.setOnClickListener(view -> openCalender());
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
//                activitymapMvvm.initGoogleApi();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        });
        binding.llReqService.setOnClickListener(view -> {
            if (requestServiceModel.isDataValid(RequestServiceActivity.this)) {
                Intent intent=new Intent(RequestServiceActivity.this, ConfirmRequestActivity.class);
                intent.putExtra("datra",requestServiceModel);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            if (activitymapMvvm.getGoogleMap().getValue() == null) {
                activitymapMvvm.setmMap(mMap);

            }


        }
    }

    private void addMarker(double lat, double lng) {
        if (activitymapMvvm.getGoogleMap().getValue() != null) {
            mMap = activitymapMvvm.getGoogleMap().getValue();
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

    }

    private void openCalender() {
        binding.flCalender.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        binding.calendar.setMinDate(calendar.getTimeInMillis());
        binding.calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            Log.e("date", i + "/" + (i1 + 1) + "/" + i2);
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            selected_date = dateFormat.format(new Date(calendar.getTimeInMillis()));
            binding.flCalender.setVisibility(View.GONE);
            binding.tvDate.setText(selected_date);
            requestServiceModel.setDate(selected_date);
        });
    }
}
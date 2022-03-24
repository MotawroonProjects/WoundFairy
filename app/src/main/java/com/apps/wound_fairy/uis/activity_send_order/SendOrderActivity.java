package com.apps.wound_fairy.uis.activity_send_order;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.SpinnerDepartmentAdapter;
import com.apps.wound_fairy.databinding.ActivityRequestServiceBinding;
import com.apps.wound_fairy.databinding.ActivitySendOrderBinding;
import com.apps.wound_fairy.model.LocationModel;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.SendOrderModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.mvvm.ActivityConfirmRequestMvvm;
import com.apps.wound_fairy.mvvm.ActivityMapMvvm;
import com.apps.wound_fairy.mvvm.ActivityRequestServiceMvvm;
import com.apps.wound_fairy.mvvm.ActivitySendOrderMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_base.FragmentMapTouchListener;
import com.apps.wound_fairy.uis.activity_confirm_request.ConfirmRequestActivity;
import com.apps.wound_fairy.uis.activity_my_orders.MyOrdersActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SendOrderActivity extends BaseActivity implements OnMapReadyCallback {
    private ActivitySendOrderBinding binding;
    private GoogleMap mMap;
    private float zoom = 15.0f;
    private ActivityMapMvvm activitymapMvvm;
    private ActivitySendOrderMvvm sendOrderMvvm;
    private ActivityResultLauncher<String> permissionLauncher;
    private ProductModel.Product product;
    private int amount;
    private SendOrderModel sendOrderModel;
    private OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_order);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        product = (ProductModel.Product) intent.getSerializableExtra("data");
        amount = intent.getIntExtra("amount", 0);
        if (intent.getStringExtra("order") != null) {
            orderModel = (OrderModel) intent.getSerializableExtra("order");

        }
    }

    private void initView() {

        binding.setLang(getLang());
        binding.setModel(product);
        sendOrderModel = new SendOrderModel();

        if (orderModel != null) {
            amount = Integer.parseInt(orderModel.getAmount());
            sendOrderModel.setLongitude(orderModel.getLongitude());
            sendOrderModel.setLatitude(orderModel.getLatitude());
            sendOrderModel.setAddress(orderModel.getAddress());
            if (orderModel.getNote() != null) {
                sendOrderModel.setNote(orderModel.getNote());
            }
        }
        binding.setSendModel(sendOrderModel);

        binding.tvCount.setText(amount + "");
        setprice();
        setUpToolbar(binding.toolbar, getString(R.string.request), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        sendOrderMvvm = ViewModelProviders.of(this).get(ActivitySendOrderMvvm.class);
        activitymapMvvm = ViewModelProviders.of(this).get(ActivityMapMvvm.class);

        sendOrderMvvm.getSend().observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(SendOrderActivity.this, getResources().getString(R.string.succ), Toast.LENGTH_LONG).show();

                navigateToMyOrdersActivity();

            }
        });
        sendOrderMvvm.getOrder().observe(this, new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {
                if (orderModel != null) {
                    Toast.makeText(SendOrderActivity.this, getResources().getString(R.string.succ), Toast.LENGTH_LONG).show();

                    Intent intent = getIntent();
                    intent.putExtra("data", orderModel);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        binding.btnConfirm.setOnClickListener(view -> {
            if (orderModel == null) {
                sendOrderMvvm.storeOrder(SendOrderActivity.this, sendOrderModel, getUserModel(), product, amount + "", getLang());
            } else {
                sendOrderMvvm.updateOrder(SendOrderActivity.this, sendOrderModel, getUserModel(), product, amount + "", getLang(), orderModel);

            }

        });
        activitymapMvvm.getLocationData().observe(this, locationModel -> {

            addMarker(locationModel.getLat(), locationModel.getLng());
            sendOrderModel.setLatitude(locationModel.getLat() + "");
            sendOrderModel.setLongitude(locationModel.getLng() + "");
            sendOrderModel.setAddress(locationModel.getAddress());
            binding.setSendModel(sendOrderModel);

        });


        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                activitymapMvvm.initGoogleApi();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        });
        binding.imIncrease.setOnClickListener(view -> {
            amount += 1;
            binding.tvCount.setText(amount + "");
            setprice();
        });
        binding.imDecreese.setOnClickListener(view -> {
            if (amount > 1) {
                amount -= 1;
                binding.tvCount.setText(amount + "");
                setprice();
            }
        });
        binding.edtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.edtSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    Log.e("q", query);
                    activitymapMvvm.Search(query, getLang());
                }
            }
            return false;
        });
        updateUI();
        checkPermission();

    }

    private void navigateToMyOrdersActivity() {
        Intent intent = new Intent(SendOrderActivity.this, MyOrdersActivity.class);
        startActivity(intent);
    }

    private void setprice() {
        Spannable word = new SpannableString(String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(product.getPrice()) * amount));

        word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvPrice.setText(word);
        Spannable wordTwo = new SpannableString(getResources().getString(R.string.egp));

        wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvPrice.append(wordTwo);
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

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, BaseActivity.fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(BaseActivity.fineLocPerm);
        } else {

            activitymapMvvm.initGoogleApi();
        }
    }


    private void updateUI() {
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.map, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);

        FragmentMapTouchListener fragmentMapTouchListener = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        fragmentMapTouchListener.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            activitymapMvvm.startLocationUpdate();

        }
    }
}
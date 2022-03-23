package com.apps.wound_fairy.uis.activity_request_service;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ImageAddServiceAdapter;
import com.apps.wound_fairy.adapter.SpinnerDepartmentAdapter;
import com.apps.wound_fairy.databinding.ActivityRequestServiceBinding;
import com.apps.wound_fairy.model.LocationModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.mvvm.ActivityMapMvvm;
import com.apps.wound_fairy.mvvm.ActivityRequestServiceMvvm;
import com.apps.wound_fairy.mvvm.ActivityServicesMvvm;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_base.FragmentMapTouchListener;
import com.apps.wound_fairy.uis.activity_confirm_request.ConfirmRequestActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequestServiceActivity extends BaseActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener {
    private ActivityRequestServiceBinding binding;
    private GoogleMap mMap;
    private float zoom = 15.0f;
    private ActivityMapMvvm activitymapMvvm;
    private ActivityRequestServiceMvvm mvvm;
    private LocationModel locationmodel;
    private List<String> imagesUriList;
    private ImageAddServiceAdapter imageAddServiceAdapter;
    private ServiceDepartmentModel serviceDepartmentModel;
    private SpinnerDepartmentAdapter spinnerDepartmentAdapter;
    private RequestServiceModel requestServiceModel;
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> launcher;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2, VIDEO_REQ = 3;
    private String type;
    private int selectedReq = 0;
    private Uri uri = null;
    private TimePickerDialog timePickerDialog;
    private String selected_date;
    private SettingsModel.Settings settingModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_service);
        initView();
    }


    private void initView() {
        binding.setLang(getLang());
        imagesUriList=new ArrayList<>();
        requestServiceModel = new RequestServiceModel();
        binding.setRequestService(requestServiceModel);
        settingModel=new SettingsModel.Settings();
        imageAddServiceAdapter = new ImageAddServiceAdapter(imagesUriList, this);
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recViewImages.setAdapter(imageAddServiceAdapter);
        setUpToolbar(binding.toolbar, getString(R.string.request_service), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        activitymapMvvm = ViewModelProviders.of(this).get(ActivityMapMvvm.class);
        mvvm = ViewModelProviders.of(this).get(ActivityRequestServiceMvvm.class);

        activitymapMvvm.getLocationData().observe(this, locationModel -> {

            addMarker(locationModel.getLat(), locationModel.getLng());
//            addServiceModel.setLat(locationModel.getLat());
//            addServiceModel.setLng(locationModel.getLng());
//            addServiceModel.setAddress(locationModel.getAddress());
//            binding.setModel(addServiceModel);

        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                activitymapMvvm.initGoogleApi();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        });

        spinnerDepartmentAdapter = new SpinnerDepartmentAdapter(this);
        binding.spinnerDepartment.setAdapter(spinnerDepartmentAdapter);

        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    requestServiceModel.setService_id(0);
                    requestServiceModel.setSelected_department("");

                }else {
                    requestServiceModel.setService_id(Integer.parseInt(mvvm.getServiceMutableLiveData().getValue().get(i).getId()));
                    Log.e("id",Integer.parseInt(mvvm.getServiceMutableLiveData().getValue().get(i).getId())+"_");
                    requestServiceModel.setSelected_department(mvvm.getServiceMutableLiveData().getValue().get(i).getTitle());
                }
                binding.setRequestService(requestServiceModel);
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

        mvvm.getSettingMutableLiveData().observe(this, settings -> {
            if (settings!=null){
                settingModel=settings.getData();
                requestServiceModel.setTotal_price(settingModel.getHome_visit_price());
                binding.setRequestService(requestServiceModel);

            }
        });
        mvvm.getSettings();

        binding.flDate.setOnClickListener(view -> openCalender());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                if (selectedReq == READ_REQ) {

                    uri = result.getData().getData();
                    File file = new File(Common.getImagePath(this, uri));
                    imagesUriList.add(uri.toString());
                    imageAddServiceAdapter.notifyItemInserted(imagesUriList.size() - 1);
                    binding.iconUpload.setVisibility(View.GONE);
                    binding.llImages.setVisibility(View.VISIBLE);
//                        if (imagesUriList.size() < 5) {
//
//                        } else {
//                            Toast.makeText(this, R.string.max_ad_photo, Toast.LENGTH_SHORT).show();
//                        }

                } else if (selectedReq == CAMERA_REQ) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    uri = getUriFromBitmap(bitmap);
                    if (uri != null) {
                        String path = Common.getImagePath(this, uri);
                        if (path != null) {
                            imagesUriList.add(uri.toString());
                            imageAddServiceAdapter.notifyItemInserted(imagesUriList.size() - 1);
                            binding.iconUpload.setVisibility(View.GONE);
                            binding.llImages.setVisibility(View.VISIBLE);
//                                if (imagesUriList.size() < 5) {
//
//                                } else {
//                                    Toast.makeText(this, R.string.max_ad_photo, Toast.LENGTH_SHORT).show();
//                                }

                        } else {
                            imagesUriList.add(uri.toString());
                            imageAddServiceAdapter.notifyItemInserted(imagesUriList.size() - 1);
//
//                                if (imagesUriList.size() < 5) {
//
//                                } else {
//                                    Toast.makeText(this, R.string.max_ad_photo, Toast.LENGTH_SHORT).show();
//                                }


                        }
                    }
                }
            }
        });

        binding.cardAddImage.setOnClickListener(view -> {
            openSheet();
        });
        binding.iconUpload.setOnClickListener(view -> openSheet());

        binding.flGallery.setOnClickListener(view -> {
            closeSheet();
            checkReadPermission();
        });

        binding.flCamera.setOnClickListener(view -> {
            closeSheet();
            checkCameraPermission();
        });
        binding.llReqService.setOnClickListener(view -> {
            if (requestServiceModel.isDataValid(RequestServiceActivity.this)) {
                Intent intent=new Intent(RequestServiceActivity.this, ConfirmRequestActivity.class);
                intent.putExtra("data",requestServiceModel);
                requestServiceModel.setImages(imagesUriList);
                startActivity(intent);
            }
        });

        binding.tvTime.setOnClickListener(view ->     timePickerDialog.show(getFragmentManager(), ""));
        binding.closeCalender.setOnClickListener(view -> binding.flCalender.setVisibility(View.GONE));
        binding.btnCancel.setOnClickListener(view -> closeSheet());
        createDateDialog();

        updateUI();
        checkPermission();
    }


    private void createDateDialog() {

        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), true);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(this, R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(this, R.color.colorPrimary));

        // datePickerDialog.setOkText(getString(R.string.select));
        //datePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        //  timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

    }


    public void openSheet() {
        binding.expandLayout.setExpanded(true, true);
    }

    public void closeSheet() {
        binding.expandLayout.collapse(true);

    }


    public void checkReadPermission() {
        closeSheet();
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {

        closeSheet();

        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {
        selectedReq = req;
        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");

            launcher.launch(intent);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                launcher.launch(intent);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
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
//        fragmentMapTouchListener.setListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            activitymapMvvm.startLocationUpdate();

        }
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

    public void deleteImage(int adapterPosition) {
        if (imagesUriList.size() > 0) {
            imagesUriList.remove(adapterPosition);
            imageAddServiceAdapter.notifyItemRemoved(adapterPosition);

        }
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String time = (hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute);
        requestServiceModel.setTime(time);
        binding.setRequestService(requestServiceModel);
    }
}
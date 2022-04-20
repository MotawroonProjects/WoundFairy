package com.apps.wound_fairy.uis.activity_request_chat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ImageAddServiceAdapter;
import com.apps.wound_fairy.adapter.SpinnerDepartmentAdapter;
import com.apps.wound_fairy.databinding.ActivityRequestChatBinding;
import com.apps.wound_fairy.databinding.ActivityRequestServiceBinding;
import com.apps.wound_fairy.model.LocationModel;
import com.apps.wound_fairy.model.PaymentDataModel;
import com.apps.wound_fairy.model.RequestChatModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.mvvm.ActivityMapMvvm;
import com.apps.wound_fairy.mvvm.ActivityRequestChatMvvm;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_base.FragmentMapTouchListener;
import com.apps.wound_fairy.uis.activity_chat.ChatActivity;
import com.apps.wound_fairy.uis.activity_confirm_request.ConfirmRequestActivity;
import com.apps.wound_fairy.uis.activity_payment.PaypalwebviewActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequesChatActivity extends BaseActivity {
    private ActivityRequestChatBinding binding;

    private ActivityRequestChatMvvm mvvm;

    private List<String> imagesUriList;
    private ImageAddServiceAdapter imageAddServiceAdapter;
    private RequestChatModel requestChatModel;
    private ActivityResultLauncher<Intent> launcher;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private String type;
    private int selectedReq = 0;
    private Uri uri = null;
    private SettingsModel.Settings settingModel;
    private PaymentDataModel paymentDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_chat);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

    }

    private void initView() {
        binding.setLang(getLang());
        imagesUriList = new ArrayList<>();
        requestChatModel = new RequestChatModel();
        binding.setRequestService(requestChatModel);
        settingModel = new SettingsModel.Settings();
        imageAddServiceAdapter = new ImageAddServiceAdapter(imagesUriList, this);
        binding.recViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recViewImages.setAdapter(imageAddServiceAdapter);
        setUpToolbar(binding.toolbar, getString(R.string.chat), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        mvvm = ViewModelProviders.of(this).get(ActivityRequestChatMvvm.class);
        mvvm.getConfirmMutableLiveData().observe(this, new Observer<PaymentDataModel>() {
            @Override
            public void onChanged(PaymentDataModel paymentDataModel) {
                if(Double.parseDouble(settingModel.getOnline_price())>0){
                RequesChatActivity.this.paymentDataModel = paymentDataModel;
                binding.flData.setVisibility(View.VISIBLE);}
                else{
                    Intent intent = new Intent(RequesChatActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        mvvm.getSettingMutableLiveData().observe(this, settings -> {
            if (settings != null) {
                settingModel = settings.getData();
                binding.setModel(settingModel);

            }
        });
        mvvm.getSettings();


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
            else if(result.getResultCode()==RESULT_OK){
                  if (selectedReq == 1000) {

                    Intent intent = new Intent(RequesChatActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
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
        binding.llReqChat.setOnClickListener(view -> {
            if (requestChatModel.isDataValid(RequesChatActivity.this)) {
                mvvm.confirmRequest(this, requestChatModel, getUserModel(), getLang(), type);
            }
        });
        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedReq = 1000;
                binding.flData.setVisibility(View.GONE);
                if(paymentDataModel.getData().getLink()!=null){
                Intent intent = new Intent(RequesChatActivity.this, PaypalwebviewActivity.class);
                intent.putExtra("url", paymentDataModel.getData().getLink());

                launcher.launch(intent);}
                else{
                    Intent intent = new Intent(RequesChatActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

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


    public void deleteImage(int adapterPosition) {
        if (imagesUriList.size() > 0) {
            imagesUriList.remove(adapterPosition);
            imageAddServiceAdapter.notifyItemRemoved(adapterPosition);

        }
    }


}
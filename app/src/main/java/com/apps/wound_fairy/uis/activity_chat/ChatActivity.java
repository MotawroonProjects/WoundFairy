package com.apps.wound_fairy.uis.activity_chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.wound_fairy.BuildConfig;
import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.ChatAdapter;
import com.apps.wound_fairy.chat_service.ChatService;
import com.apps.wound_fairy.databinding.ActivityChatBinding;
import com.apps.wound_fairy.model.AddChatMessageModel;
import com.apps.wound_fairy.model.ChatUserModel;
import com.apps.wound_fairy.model.MessageModel;
import com.apps.wound_fairy.mvvm.ActivityChatMvvm;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private ActivityChatMvvm mvvm;
    private String imagePath = "";
    private ChatAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        initView();
    }


    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityChatMvvm.class);
        mvvm.getIsLoading().observe(this, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
        });


        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.setLang(getLang());
        binding.edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.send.setVisibility(View.GONE);
                } else {
                    binding.send.setVisibility(View.VISIBLE);

                }
            }
        });
        adapter = new ChatAdapter(this, getUserModel().getData().getUser().getId(), binding.recView);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recView.setAdapter(adapter);

        mvvm.onDataSuccess().observe(this, list -> {
            if (adapter != null) {
                adapter.updateList(list);
            }
        });


        binding.imageCamera.setOnClickListener(v -> {
            checkCameraFilePermission();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK) {
                sendMessage("image", "", imagePath);

            }
        });

        binding.imageBack.setOnClickListener(v -> {
            finish();
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mvvm.getChatMessages(getUserModel());

        binding.send.setOnClickListener(v -> {
            sendMessage("text", binding.edtMessage.getText().toString(), null);
            binding.edtMessage.setText(null);

        });

        binding.cardLastMsg.setOnClickListener(v -> {
            binding.setMsg("");
            binding.cardLastMsg.setVisibility(View.GONE);
            binding.recView.scrollToPosition(mvvm.onDataSuccess().getValue().size() - 1);
            adapter.notifyDataSetChanged();
            //binding.recView.post(() -> );
        });

        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getChatMessages(getUserModel()));


    }

    private void sendMessage(String type, String msg, String image_url) {
        AddChatMessageModel addChatMessageModel = new AddChatMessageModel(type, msg, image_url,getUserModel().getData().getAccess_token());
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtra("data", addChatMessageModel);
        startService(intent);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMessage(MessageModel messageModel) {
        imagePath = "";
        mvvm.addNewMessage(messageModel);
        adapter.notifyItemInserted(mvvm.onDataSuccess().getValue().size() - 1);

    }

    private void checkCameraFilePermission() {
        if (ActivityCompat.checkSelfPermission(this, BaseActivity.WRITE_PERM) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, BaseActivity.CAM_PERM) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{BaseActivity.WRITE_PERM, BaseActivity.CAM_PERM}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2) {
                openCamera();
            }
        }
    }

    private void openCamera() {

        req = 1;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = getImageFile();

        if (imageFile != null) {
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            launcher.launch(intent);
        }


    }

    private File getImageFile() {
        File imageFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH:mm", Locale.ENGLISH).format(new Date());
        String imageName = "JPEG_" + timeStamp + "_";
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Etbo5lyClientImages");
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            imageFile = File.createTempFile(imageName, ".jpg", file);
            imagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void displayLastMessage(MessageModel messageModel) {
        if (messageModel.getType().equals("image")) {
            // binding.setMsg(getString(R.string.attach_sent));
        } else {
            binding.setMsg(messageModel.getText());
        }
        binding.cardLastMsg.setVisibility(View.VISIBLE);


    }

    public void hideLastMessageView() {
        binding.setMsg("");
        binding.cardLastMsg.setVisibility(View.GONE);
    }
}
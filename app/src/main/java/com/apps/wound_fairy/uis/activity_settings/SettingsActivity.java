package com.apps.wound_fairy.uis.activity_settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivitySettingsBinding;
import com.apps.wound_fairy.language.Language;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.mvvm.ActivitySettingsMvvm;
import com.apps.wound_fairy.tags.Tags;
import com.apps.wound_fairy.uis.activity_app.AppActivity;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_contact_us.ContactUsActivity;
import com.apps.wound_fairy.uis.activity_language.LanguageActivity;

import java.util.List;

import io.paperdb.Paper;

public class SettingsActivity extends BaseActivity {
    private ActivitySettingsBinding binding;
    private ActivitySettingsMvvm mvvm;
    private SettingsModel.Settings settingModel;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        initView();
    }

    private void initView() {
        String app_url = "https://play.google.com/store/apps/details?id=" + this.getPackageName();
        settingModel = new SettingsModel.Settings();
        setUpToolbar(binding.toolbar, getString(R.string.settings), R.color.white, R.color.black);
        mvvm = ViewModelProviders.of(this).get(ActivitySettingsMvvm.class);
        mvvm.getSettingMutableLiveData().observe(this, model -> settingModel = model.getData());
        mvvm.getSettings();

        binding.llLanguage.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(SettingsActivity.this, LanguageActivity.class);
            launcher.launch(intent);
//            getSupportFragmentManager().beginTransaction()
//         .add(android.R.id.content, new FragmentLanguage ()).commit()
        });
        if (getLang().equals("ar")) {
            binding.tvLang.setText("عربي");
        } else {
            binding.tvLang.setText("english");
        }
        binding.llContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });
        binding.llTerms.setOnClickListener(view -> navigateToAppActivity("terms", Tags.base_url + "webView?type=terms"));
        binding.llPrivacy.setOnClickListener(view -> navigateToAppActivity("privacy", Tags.base_url + "webView?type=privacy"));
        binding.llRate.setOnClickListener(view -> rateApp());
        binding.llShare.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, "Wound Fairy App");
            intent.putExtra(Intent.EXTRA_TEXT, app_url);
            startActivity(intent);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                String lang = result.getData().getStringExtra("lang");
                Intent intent = getIntent();
                intent.putExtra("lang", lang);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    private void navigateToAppActivity(String type, String url) {
        Intent intent = new Intent(SettingsActivity.this, AppActivity.class);
        intent.putExtra("data", type);
        intent.putExtra("url", url);
        startActivity(intent);

    }

    private void rateApp() {
        String appId = getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        final List<ResolveInfo> otherApps = getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appId));
            startActivity(webIntent);
        }

    }


}
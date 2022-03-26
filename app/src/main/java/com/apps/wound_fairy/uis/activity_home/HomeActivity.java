package com.apps.wound_fairy.uis.activity_home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.apps.wound_fairy.interfaces.Listeners;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.mvvm.HomeActivityMvvm;
import com.apps.wound_fairy.preferences.Preferences;
import com.apps.wound_fairy.tags.Tags;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;

import com.apps.wound_fairy.R;

import com.apps.wound_fairy.databinding.ActivityHomeBinding;
import com.apps.wound_fairy.language.Language;
import com.apps.wound_fairy.uis.activity_login.LoginActivity;
import com.apps.wound_fairy.uis.activity_my_orders.MyOrdersActivity;
import com.apps.wound_fairy.uis.activity_my_reservations.MyReservationsActivity;
import com.apps.wound_fairy.uis.activity_notification.NotificationActivity;
import com.apps.wound_fairy.uis.activity_settings.SettingsActivity;
import com.apps.wound_fairy.uis.activity_sign_up.SignUpActivity;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity implements Listeners.VerificationListener {
    private ActivityHomeBinding binding;
    private NavController navController;
    private HomeActivityMvvm homeActivityMvvm;
    private ActionBarDrawerToggle toggle;
    private Preferences preferences;
    private UserModel userModel;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;
    private AppBarConfiguration appBarConfiguration;
    private SettingsModel.Settings settingsModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();

    }


    private void initView() {
        binding.setLang(getLang());
        preferences = Preferences.getInstance();
        userModel = getUserModel();

        if (userModel != null) {
            binding.setModel(userModel);
        }
        if (getLang().equals("ar")) {
            binding.toolBar.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            binding.toolBar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }
        homeActivityMvvm = ViewModelProviders.of(this).get(HomeActivityMvvm.class);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                userModel = getUserModel();
                if (userModel.getData().getUser().getImage() != null) {
                    binding.icon.setVisibility(View.GONE);
                    Picasso.get().load(Tags.base_url + userModel.getData().getUser().getImage()).into(binding.image);
                }
                binding.setModel(userModel);
                updateFirebase();
            }
//            else if (req == 2 && result.getResultCode() == Activity.RESULT_OK) {
//                userModel = getUserModel();
//                binding.setModel(getUserModel());
//                updateFirebase();
//            } else
            else if (req == 3 && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                String lang = result.getData().getStringExtra("lang");
                refreshActivity(lang);
            }
        });
        homeActivityMvvm.getSettingMutableLiveData().observe(this, new Observer<SettingsModel>() {
            @Override
            public void onChanged(SettingsModel settingsModel) {
                if (settingsModel != null) {
                    HomeActivity.this.settingsModel = settingsModel.getData();
                }
            }
        });
        navController = Navigation.findNavController(this, R.id.navHostFragment);

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()) //Pass the ids of fragments from nav_graph which you dont want to show back button in toolbar
                        .setDrawerLayout(binding.drawerLayout)
                        .build();

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        binding.imgNotification.setOnClickListener(v -> {
            if (getUserModel() != null) {
                binding.setCount("0");
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            } else {
                navigationToLoginActivity();
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupWithNavController(binding.toolBar, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolBar, R.string.open, R.string.close);
        // toggle.setDrawerIndicatorEnabled(true);
        if (getLang().equals("ar")) {

            toggle.setHomeAsUpIndicator(R.drawable.ic_menu2);
        } else {
            toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        toggle.syncState();
        if (getLang().equals("ar")) {
            binding.toolBar.setNavigationIcon(R.drawable.ic_menu2);
        } else {
            binding.toolBar.setNavigationIcon(R.drawable.ic_menu);
        }
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (binding.toolBar.getNavigationIcon() != null) {
                if (getLang().equals("ar")) {
                    binding.toolBar.setNavigationIcon(R.drawable.ic_menu2);
                } else {
                    binding.toolBar.setNavigationIcon(R.drawable.ic_menu);
                }
                binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

            }
        });


        homeActivityMvvm.logout.observe(this, aBoolean -> {
            if (aBoolean) {
                logout();
            }
        });
        homeActivityMvvm.firebase.observe(this, token -> {
            if (getUserModel() != null) {
                UserModel userModel = getUserModel();
                Log.e("taken", token);
                userModel.getData().setFirebase_token(token);
                setUserModel(userModel);
            }
        });
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
        }
        binding.llEditAccount.setOnClickListener(view -> {
            if (userModel == null) {
                navigationToLoginActivity();
            } else {
                navigationToSignupActivity();
            }
        });
        binding.llMyOrders.setOnClickListener(view -> {
            if (getUserModel() != null) {
                Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            } else {
                navigationToLoginActivity();
            }
        });
        binding.llMyReservations.setOnClickListener(view -> {
            if (getUserModel() != null) {
                Intent intent = new Intent(HomeActivity.this, MyReservationsActivity.class);
                startActivity(intent);
            } else {
                navigationToLoginActivity();

            }

        });

        binding.llSettings.setOnClickListener(view -> {

            navigateToSettingsActivity();

        });
        binding.llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getUserModel() == null) {
                    logout();
                } else {
                    homeActivityMvvm.logout(HomeActivity.this, getUserModel());
                }
            }
        });

        if (userModel == null) {
            binding.tvName.setOnClickListener(view -> navigationToLoginActivity());

        }
        binding.imgNotification.setOnClickListener(v -> {


        });
        homeActivityMvvm.firebase.observe(this, token -> {
            if (getUserModel() != null) {
                UserModel userModel = getUserModel();
                userModel.getData().setFirebase_token(token);
                setUserModel(userModel);
            }
        });

        binding.imgNotification.setOnClickListener(v -> {
            if (getUserModel() != null) {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            } else {
                navigationToLoginActivity();
            }
        });
        binding.imFacevook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingsModel != null && settingsModel.getFacebook() != null && !settingsModel.getFacebook().equals("#")) {
                    open(settingsModel.getFacebook());
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.imInstegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingsModel != null && settingsModel.getInsta() != null && !settingsModel.getInsta().equals("#")) {
                    open(settingsModel.getInsta());
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.imTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingsModel != null && settingsModel.getTwitter() != null && !settingsModel.getTwitter().equals("#")) {
                    open(settingsModel.getTwitter());
                } else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();
                }

            }
        });
//        binding.imSnapChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (settingModel != null && settingModel.getSnapchat() != null && !settingModel.getSnapchat().equals("#")) {
//                    open("https://snapchat.com/add/" + settingModel.getSnapchat());
//                } else {
//                    Toast.makeText(activity, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
        }
        homeActivityMvvm.getSettings();
    }

    private void navigateToSettingsActivity() {
        req = 3;
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        launcher.launch(intent);
    }

    private void navigationToSignupActivity() {
        req = 1;
        Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
        launcher.launch(intent);
    }

    private void logout() {
        clearUserModel(this);
        userModel = getUserModel();
        binding.setModel(null);
        binding.icon.setVisibility(View.VISIBLE);
        navigationToLoginActivity();
    }

    private void navigationToLoginActivity() {
        req = 1;
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        launcher.launch(intent);
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    @Override
    public boolean onNavigateUp() {

        return NavigationUI.navigateUp(navController, binding.drawerLayout);
    }

    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.home) {
            finish();

        } else {
            navController.popBackStack();
        }

    }

    @Override
    public void onVerificationSuccess() {

    }


    public void updateFirebase() {
        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
        }
    }

    private void open(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);
    }
}

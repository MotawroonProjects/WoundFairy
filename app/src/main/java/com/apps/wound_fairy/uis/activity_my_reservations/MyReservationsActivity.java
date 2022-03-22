package com.apps.wound_fairy.uis.activity_my_reservations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityMyReservationsBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_my_reservations.fragments.FragmentCurrentReservations;
import com.apps.wound_fairy.uis.activity_my_reservations.fragments.FragmentPreviousReservations;

import java.util.ArrayList;
import java.util.List;

public class MyReservationsActivity extends BaseActivity {
    private ActivityMyReservationsBinding binding;
    private List<Fragment> fragmentList;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_reservations);
        initView();
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        setUpToolbar(binding.toolbar, getString(R.string.my_reservation), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());

        fragmentList.add(FragmentCurrentReservations.newInstance());
        fragmentList.add(FragmentPreviousReservations.newInstance());
        pagerAdapter =new com.apps.wound_fairy.adapter.PagerAdapter(getSupportFragmentManager(), PagerAdapter.POSITION_UNCHANGED,fragmentList);
        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setOffscreenPageLimit(fragmentList.size());

    }
}
package com.apps.wound_fairy.uis.activity_my_reservations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.adapter.MyPagerAdapter;
import com.apps.wound_fairy.databinding.ActivityMyReservationsBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_my_reservations.fragments.FragmentCurrentReservations;
import com.apps.wound_fairy.uis.activity_my_reservations.fragments.FragmentPreviousReservations;

import java.util.ArrayList;
import java.util.List;

public class MyReservationsActivity extends BaseActivity {
    private ActivityMyReservationsBinding binding;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private MyPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_reservations);
        initView();
    }

    private void initView() {
        titles = new ArrayList<>();
        fragmentList = new ArrayList<>();
        setUpToolbar(binding.toolbar, getString(R.string.my_reservation), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());

        titles.add(getString(R.string.current));
        titles.add(getString(R.string.prev));
        binding.tab.setupWithViewPager(binding.pager);
        fragmentList.add(FragmentCurrentReservations.newInstance());
        fragmentList.add(FragmentPreviousReservations.newInstance());
        pagerAdapter =new MyPagerAdapter(getSupportFragmentManager(), PagerAdapter.POSITION_UNCHANGED,fragmentList,titles);
        binding.pager.setAdapter(pagerAdapter);
        binding.pager.setOffscreenPageLimit(fragmentList.size());
        for (int i = 0; i < binding.tab.getChildCount(); i++) {
            View view = ((ViewGroup) binding.tab.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(10, 0, 10, 0);
        }
    }
}
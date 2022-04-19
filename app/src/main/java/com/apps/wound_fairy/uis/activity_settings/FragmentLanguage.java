package com.apps.wound_fairy.uis.activity_settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.FragmentBottomSheetLanguageDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.paperdb.Paper;

public class FragmentLanguage extends BottomSheetDialogFragment {
    private FragmentBottomSheetLanguageDialogBinding binding;
    private String lang = "";
    private String selectedLang;
    private SettingsActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (SettingsActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_language_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        lang = getLang();
        selectedLang = lang;
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);

        } else {
            binding.rbEn.setChecked(true);

        }

        binding.rbAr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                selectedLang="ar";
            }
        });

        binding.rbEn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                selectedLang="en";
            }
        });

        binding.btnConfirm.setOnClickListener(view -> {
          dismiss();
          //activity.refreshActivity(selectedLang);
        });
    }

    protected String getLang() {
        Paper.init(activity);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

}

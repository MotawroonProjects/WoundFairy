package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private Context context;


    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

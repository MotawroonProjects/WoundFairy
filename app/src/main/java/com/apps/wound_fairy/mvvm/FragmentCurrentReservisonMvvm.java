package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class FragmentCurrentReservisonMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentCurrReservMvvm";

    private Context context;


    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentCurrentReservisonMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }




    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }


    //_________________________hitting api_________________________________



    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

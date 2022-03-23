package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivitySettingsMvvm extends AndroidViewModel {

    private MutableLiveData<SettingsModel> settingMutableLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<SettingsModel> getSettingMutableLiveData() {
        if (settingMutableLiveData == null) {
            settingMutableLiveData = new MutableLiveData<>();
        }
        return settingMutableLiveData;
    }
    public ActivitySettingsMvvm(@NonNull Application application) {
        super(application);
    }

    public void getSettings() {
        Api.getService(Tags.base_url).getSettings()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SettingsModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SettingsModel> response) {
                        if (response.isSuccessful() && response.body().getData() != null){
                            if (response.body().getStatus()==200){
                                settingMutableLiveData.postValue(response.body());
                                Log.e("status",response.code()+"_"+response.body().getStatus()+"_");
                                Log.e("hhhh",response.body().getData().getHome_visit_price());
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("error", e.toString());
                    }
                });
    }
}

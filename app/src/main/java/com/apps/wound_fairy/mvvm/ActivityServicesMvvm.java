package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.ServiceModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityServicesMvvm extends AndroidViewModel {
    private MutableLiveData<ServiceModel> mutableLiveData;
    private MutableLiveData<Boolean> isLoading;

    public MutableLiveData<ServiceModel> getMutableLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityServicesMvvm(@NonNull Application application) {
        super(application);
    }

    public void getService(String lang,String type){
        isLoading.setValue(true);

        Api.getService(Tags.base_url).getServiceDetails(lang,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ServiceModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<ServiceModel> response) {
                        isLoading.postValue(false);
                        Log.e("sssss",response.code()+"_"+response.body().getData()+""+response.body().getStatus()+type+lang);
                        if (response.isSuccessful() && response.body().getData()!=null){
                            if (response.body().getStatus()==200){

                                mutableLiveData.setValue(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.setValue(false);
                    }
                });
    }
}

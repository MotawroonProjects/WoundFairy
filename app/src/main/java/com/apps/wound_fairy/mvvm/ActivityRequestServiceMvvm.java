package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityRequestServiceMvvm extends AndroidViewModel {

    private MutableLiveData<List<ServiceDepartmentModel.Department>> serviceMutableLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<List<ServiceDepartmentModel.Department>> getServiceMutableLiveData() {
        if (serviceMutableLiveData == null) {
            serviceMutableLiveData = new MutableLiveData<>();
        }
        return serviceMutableLiveData;
    }

    public ActivityRequestServiceMvvm(@NonNull Application application) {
        super(application);
    }

    public void getServiceDepartment(String lang) {
        Api.getService(Tags.base_url).getServiceDepartment(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ServiceDepartmentModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<ServiceDepartmentModel> response) {
                        if (response.isSuccessful() && response.body().getData() != null) {
                            if (response.body().getStatus() == 200) {
                                serviceMutableLiveData.setValue(response.body().getData());
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

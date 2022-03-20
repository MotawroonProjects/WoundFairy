package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.BlogDataModel;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentBlogsMvvm extends AndroidViewModel {
    private MutableLiveData<Boolean> isDataLoading;
    private MutableLiveData<List<BlogModel>> onDataSuccess;
    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentBlogsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsDataLoading() {
        if (isDataLoading == null) {
            isDataLoading = new MutableLiveData<>();
        }
        return isDataLoading;
    }

    public MutableLiveData<List<BlogModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public void getBlogs(String lang) {
        getIsDataLoading().setValue(true);
        Api.getService(Tags.base_url).getBlogs(lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<BlogDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<BlogDataModel> response) {
                        getIsDataLoading().postValue(false);

                        if (response.isSuccessful() && response.body() !=null) {
                            if (response.body().getStatus()==200){
                                onDataSuccess.setValue(response.body().getData());
                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getIsDataLoading().setValue(false);
                    }
                });
    }
}

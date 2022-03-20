package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.BlogDataModel;
import com.apps.wound_fairy.model.BlogModel;
import com.apps.wound_fairy.model.SliderDataModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentHomeMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private Context context;
    private MutableLiveData<SliderDataModel> sliderDataModelMutableLiveData;
    private MutableLiveData<List<BlogModel>> blogDataModelMutableLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isLoadingLiveData;

    public FragmentHomeMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<SliderDataModel> getSliderDataModelMutableLiveData() {
        if (sliderDataModelMutableLiveData == null) {
            sliderDataModelMutableLiveData = new MutableLiveData<>();
        }
        return sliderDataModelMutableLiveData;
    }

    public MutableLiveData<List<BlogModel>> getBlogDataModelMutableLiveData() {
        if (blogDataModelMutableLiveData==null){
            blogDataModelMutableLiveData=new MutableLiveData<>();
        }
        return blogDataModelMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLiveData == null) {
            isLoadingLiveData = new MutableLiveData<>();
        }
        return isLoadingLiveData;
    }

    public void getSlider(){
        isLoadingLiveData.setValue(true);
        Api.getService(Tags.base_url).getSlider()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SliderDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SliderDataModel> response) {
                        isLoadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getStatus()==200){
                                sliderDataModelMutableLiveData.postValue(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLiveData.setValue(false);
                    }
                });
    }

    public void getBlogs(String lang) {
        isLoadingLiveData.setValue(true);
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
                        isLoadingLiveData.postValue(false);

                        if (response.isSuccessful() && response.body() !=null) {
                            if (response.body().getStatus()==200){
                                blogDataModelMutableLiveData.postValue(response.body().getData());
                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLiveData.setValue(false);
                    }
                });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

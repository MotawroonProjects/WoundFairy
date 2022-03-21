package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.SingleProductModel;
import com.apps.wound_fairy.model.SliderDataModel;
import com.apps.wound_fairy.model.SliderProductModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityProductDetailsMvvm extends AndroidViewModel {

//    private MutableLiveData<SliderProductModel> sliderDataModelMutableLiveData;
    private MutableLiveData<Boolean> isDataLoading;
    private MutableLiveData<ProductModel.Product> onDataSuccess;
    private CompositeDisposable disposable = new CompositeDisposable();


//    public MutableLiveData<SliderProductModel> getSliderDataModelMutableLiveData() {
//        if (sliderDataModelMutableLiveData==null){
//            sliderDataModelMutableLiveData=new MutableLiveData<>();
//        }
//        return sliderDataModelMutableLiveData;
//    }

    public MutableLiveData<Boolean> getIsDataLoading() {
        if (isDataLoading==null){
            isDataLoading=new MutableLiveData<>();
        }
        return isDataLoading;
    }

    public MutableLiveData<ProductModel.Product> getOnDataSuccess() {
        if (onDataSuccess==null){
            onDataSuccess=new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public ActivityProductDetailsMvvm(@NonNull Application application) {
        super(application);
    }

    public void getSingleProduct(String id,String lang){
        getIsDataLoading().setValue(true);
        Api.getService(Tags.base_url).getSingleProduct(id,lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleProductModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleProductModel> response) {
                        getIsDataLoading().postValue(false);
                        if (response.isSuccessful() && response.body()!=null){
                            if (response.body().getStatus()==200){
                                onDataSuccess.setValue(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getIsDataLoading().setValue(false);
                        Log.e("status",e.toString());

                    }
                });
    }

//    public void getSlider(){
//        isDataLoading.setValue(true);
//        Api.getService(Tags.base_url).getProductSlider()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<Response<SliderProductModel>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable.add(d);
//                    }
//
//                    @Override
//                    public void onSuccess(@NonNull Response<SliderProductModel> response) {
//                        isDataLoading.postValue(false);
//
//                        if (response.isSuccessful() && response.body()!=null){
//                            if (response.body().getStatus()==200){
//                                sliderDataModelMutableLiveData.postValue(response.body());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        isDataLoading.setValue(false);
//                    }
//                });
//    }
}

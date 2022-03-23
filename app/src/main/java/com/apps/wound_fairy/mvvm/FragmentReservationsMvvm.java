package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.ReservationDataModel;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentReservationsMvvm extends AndroidViewModel {
    private MutableLiveData<List<ReservationModel>> mutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;
    private CompositeDisposable disposable = new CompositeDisposable();



    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    public MutableLiveData<List<ReservationModel>> getMutableLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }
    public FragmentReservationsMvvm(@NonNull Application application) {
        super(application);
    }

    public void getCurrentReservations(UserModel userModel,String lang){
        isLoadingLivData.setValue(true);

        Log.e("data",userModel.getData().getAccess_token()+"_"+lang);
        Api.getService(Tags.base_url).getReservations(userModel.getData().getAccess_token(),lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ReservationDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<ReservationDataModel> response) {
                        isLoadingLivData.postValue(false);
                        Log.e("status",response.code()+"");
                        if (response.isSuccessful() && response.body().getData()!=null){
                            if (response.body().getStatus()==200){
                                mutableLiveData.setValue(response.body().getData().getCurrent());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.setValue(false);
                        Log.e("error",e.toString());
                    }
                });
    }

    public void getPreviousReservation(UserModel userModel, String lang) {
        isLoadingLivData.setValue(true);

        Api.getService(Tags.base_url).getReservations(userModel.getData().getAccess_token(), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<ReservationDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<ReservationDataModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body().getData() != null) {
                            if (response.body().getStatus() == 200) {
                                mutableLiveData.setValue(response.body().getData().getPrevious());
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.setValue(false);
                        Log.e("error", e.toString());

                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

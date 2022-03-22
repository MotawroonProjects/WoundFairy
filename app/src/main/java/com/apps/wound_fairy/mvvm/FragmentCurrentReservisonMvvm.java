package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.CurrentReservationModel;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.model.ServiceModel;
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

public class FragmentCurrentReservisonMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentCurrReservMvvm";
    private Context context;

    private MutableLiveData<List<ReservationModel>> mutableLiveData;
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

    public MutableLiveData<List<ReservationModel>> getMutableLiveData() {
        if (mutableLiveData==null){
            mutableLiveData=new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    //_________________________hitting api_________________________________


    public void getCurrentReservations(UserModel userModel,String lang){
        isLoadingLivData.setValue(true);

        Api.getService(Tags.base_url).getCurrentReservations(userModel.getData().getAccess_token(),lang)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CurrentReservationModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<CurrentReservationModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body().getData()!=null){
                            if (response.body().getStatus()==200){
                                mutableLiveData.setValue(response.body().getData());
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

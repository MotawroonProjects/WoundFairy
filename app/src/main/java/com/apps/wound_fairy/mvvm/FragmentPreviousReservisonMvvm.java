package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.PreviousReservationModel;
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

public class FragmentPreviousReservisonMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentPreviousReservisionMvvm";
    private Context context;

    private MutableLiveData<List<ReservationModel>> mutableLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentPreviousReservisonMvvm(@NonNull Application application) {
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
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    //_________________________hitting api_________________________________

    public void getPreviousReservation(UserModel userModel, String lang) {
        isLoadingLivData.setValue(true);

        Api.getService(Tags.base_url).getPreviousReservations(userModel.getData().getAccess_token(), lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PreviousReservationModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<PreviousReservationModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body().getData() != null) {
                            if (response.body().getStatus() == 200) {
                                mutableLiveData.setValue(response.body().getData());
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

//    public void getReservionData(UserModel userModel) {
//      /*  isLoadingLivData.postValue(true);
//
//
//        Api.getService(Tags.base_url)
//                .getPreviousReservion("Bearer " + userModel.getData().getToken(),Tags.api_key,userModel.getData().getId()+"" )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .subscribe(new SingleObserver<Response<ReservionDataModel>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        disposable.add(d);
//                    }
//
//                    @Override
//                    public void onSuccess(@NonNull Response<ReservionDataModel> response) {
//                        isLoadingLivData.postValue(false);
//
//                        if (response.isSuccessful() && response.body() != null) {
//                            if (response.body().getStatus() == 200) {
//                                List<ResevisionModel> list = response.body().getData();
//                                listMutableLiveData.setValue(list);
//                            }
//                        }
//                    }
//
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        isLoadingLivData.postValue(false);
//                        Log.e(TAG, "onError: ", e);
//                    }
//                });*/
//
//    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

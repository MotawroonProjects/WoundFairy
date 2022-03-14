package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.model.UserModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class FragmentPreviousReservisonMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentPreviousReservisionMvvm";

    private Context context;


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

    //_________________________hitting api_________________________________


    public void getReservionData(UserModel userModel) {
      /*  isLoadingLivData.postValue(true);

       
        Api.getService(Tags.base_url)
                .getPreviousReservion("Bearer " + userModel.getData().getToken(),Tags.api_key,userModel.getData().getId()+"" )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Response<ReservionDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<ReservionDataModel> response) {
                        isLoadingLivData.postValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                List<ResevisionModel> list = response.body().getData();
                                listMutableLiveData.setValue(list);
                            }
                        }
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoadingLivData.postValue(false);
                        Log.e(TAG, "onError: ", e);
                    }
                });*/

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

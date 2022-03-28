package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.model.MessageModel;
import com.apps.wound_fairy.model.MessagesDataModel;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.model.VisitOnlineModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;

import java.io.IOException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityServicesMvvm extends AndroidViewModel {
    private MutableLiveData<VisitOnlineModel> mutableLiveData;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<MessageModel>> onDataSuccess;

    public MutableLiveData<VisitOnlineModel> getMutableLiveData() {
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
    public MutableLiveData<List<MessageModel>> onDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityServicesMvvm(@NonNull Application application) {
        super(application);
    }

    public void getService(String lang,String type){
        isLoading.setValue(true);


        Api.getService(Tags.base_url).getVisitOnlineDetails(lang,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<VisitOnlineModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<VisitOnlineModel> response) {
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
    public void getChatData(UserModel userModel, Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // Log.e("id", order_id);
        Api.getService(Tags.base_url).getChatData(userModel.getData().getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MessagesDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<MessagesDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Log.e("dldldk",response.code()+" "+response.body().getStatus());
                            if (response.body() != null && response.body().getStatus() == 200) {
                                onDataSuccess().setValue(response.body().getData());
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        dialog.dismiss();

                        Log.e("chatError", e.toString());
                    }
                });

    }

}

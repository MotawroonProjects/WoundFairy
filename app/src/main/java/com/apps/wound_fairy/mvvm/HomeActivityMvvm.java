package com.apps.wound_fairy.mvvm;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.model.StatusResponse;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HomeActivityMvvm extends AndroidViewModel {
    private Context context;
    private MutableLiveData<SettingsModel> settingMutableLiveData;

    public MutableLiveData<Boolean> logout = new MutableLiveData<>();

    public MutableLiveData<String> firebase = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<SettingsModel> getSettingMutableLiveData() {
        if (settingMutableLiveData == null) {
            settingMutableLiveData = new MutableLiveData<>();
        }
        return settingMutableLiveData;
    }
    public HomeActivityMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();


    }
    public void logout(Context context,UserModel userModel){
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("data",userModel.getData().getAccess_token()+"_"+userModel.getData().getFirebase_token());
        Api.getService(Tags.base_url).logout(userModel.getData().getAccess_token(),userModel.getData().getFirebase_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        dialog.dismiss();
                        Log.e("srrr",response.code()+""+response.body().getStatus()+"");
                        if (response.isSuccessful()){
                            if (response.body().getStatus()==200){
                                logout.postValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                    }
                });
    }

    public void updateFirebase(Context context, UserModel userModel) {
       FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener((Activity) context, task -> {
            if (task.isSuccessful()) {
                String token = task.getResult().getToken();

                Log.e("token",token+"_");
                Api.getService(Tags.base_url).updateFirebasetoken( userModel.getData().getAccess_token(), token , "android")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> statusResponseResponse) {
                        if (statusResponseResponse.isSuccessful()) {
                            if (statusResponseResponse.body().getStatus() == 200) {
                                firebase.postValue(token);
                                Log.e("token", "updated successfully");
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {

                    }
                });
            }
        });


    }

    public void getSettings() {
        Api.getService(Tags.base_url).getSettings()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SettingsModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SettingsModel> response) {
                        if (response.isSuccessful() && response.body().getData() != null){
                            if (response.body().getStatus()==200){
                                settingMutableLiveData.postValue(response.body());
                                Log.e("status",response.code()+"_"+response.body().getStatus()+"_");
                                Log.e("hhhh",response.body().getData().getHome_visit_price());
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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

package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.model.SignUpModel;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ActivitySignupMvvm extends AndroidViewModel {
    private Context context;
    public MutableLiveData<UserModel> onUserDataSuccess = new MutableLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivitySignupMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }

//    public MutableLiveData<UserModel> getOnUserDataSuccess() {
//        if (onUserDataSuccess == null) {
//            onUserDataSuccess = new MutableLiveData<>();
//        }
//        return onUserDataSuccess;
//    }

    public void signUp(SignUpModel signUpModel, Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getFirstName() + " " + signUpModel.getLastName());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());

        MultipartBody.Part image = null;
        if (signUpModel.getImage() != null && !signUpModel.getImage().isEmpty()) {
            image = Common.getMultiPartImage(context, Uri.parse(signUpModel.getImage()), "image");
        }

        Api.getService(Tags.base_url).signUp(phone_code_part, phone_part, name_part, email_part, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){
                            if (response.body()!=null && response.body().getStatus()==200){
                                onUserDataSuccess.postValue(response.body());
                            }else if (response.body().getStatus()==422){
                                Toast.makeText(context, R.string.em_exist, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        dialog.dismiss();
                    }
                });
    }
    public void update(SignUpModel signUpModel, UserModel userModel, Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody phone_code_part = Common.getRequestBodyText(signUpModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(signUpModel.getPhone());
        RequestBody name_part = Common.getRequestBodyText(signUpModel.getFirstName() + " " + signUpModel.getLastName());
        RequestBody email_part = Common.getRequestBodyText(signUpModel.getEmail());

        MultipartBody.Part image = null;
        if (signUpModel.getImage() != null && !signUpModel.getImage().isEmpty()) {
            if (!signUpModel.getImage().startsWith("http")) {
                image = Common.getMultiPart(context, Uri.parse(signUpModel.getImage()), "image");

            }
        }


        Api.getService(Tags.base_url).updateProfile(userModel.getData().getAccess_token(),phone_code_part,phone_part, name_part, email_part, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<UserModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {

                                    onUserDataSuccess.postValue(response.body());
                                }
                                else if (response.body().getStatus()==422){
                                    Toast.makeText(context, R.string.em_exist, Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

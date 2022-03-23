package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.model.RequestChatModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.model.StatusResponse;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ActivityRequestChatMvvm extends AndroidViewModel {

    private MutableLiveData<SettingsModel> settingMutableLiveData;
    private MutableLiveData<Boolean> confirmMutableLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<SettingsModel> getSettingMutableLiveData() {
        if (settingMutableLiveData == null) {
            settingMutableLiveData = new MutableLiveData<>();
        }
        return settingMutableLiveData;
    }

    public ActivityRequestChatMvvm(@NonNull Application application) {
        super(application);
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
    public MutableLiveData<Boolean> getConfirmMutableLiveData() {
        if (confirmMutableLiveData == null) {
            confirmMutableLiveData = new MutableLiveData<>();
        }
        return confirmMutableLiveData;
    }

    public void confirmRequest(Context context, RequestChatModel model, UserModel userModel,String lang,String type) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
Log.e("type",type);

//        String m_date = sendServiceModel.getOrder_date();

        RequestBody complaint = Common.getRequestBodyText(model.getComplaint());
        RequestBody lang_part = Common.getRequestBodyText(lang);
        RequestBody type_part = Common.getRequestBodyText(type);
        List<MultipartBody.Part> imageList ;

            imageList = getMultipartBodyList(model.getImages(), "images[]", context);


        Api.getService(Tags.base_url).requestChat(userModel.getData().getAccess_token(), complaint, imageList,type_part)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<StatusResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onSuccess(@NonNull Response<StatusResponse> response) {
                        dialog.dismiss();


                           // Log.e("stt",response.code()+""+response.body().getStatus());

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                confirmMutableLiveData.postValue(true);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("onError", e.getMessage());
                        dialog.dismiss();
                    }
                });

    }

    private List<MultipartBody.Part> getMultipartBodyList(List<String> uriList, String name, Context context) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            Uri uri = Uri.parse(uriList.get(i));
            MultipartBody.Part part = Common.getMultiPart(context, uri, name);
            partList.add(part);

        }
        return partList;
    }
}

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
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.RequestServiceModel;
import com.apps.wound_fairy.model.ReservationModel;
import com.apps.wound_fairy.model.SingleReservationModel;
import com.apps.wound_fairy.model.StatusResponse;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;

import java.text.DateFormat;
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

public class ActivityConfirmRequestMvvm extends AndroidViewModel {
    private MutableLiveData<Boolean> confirmMutableLiveData;
    private MutableLiveData<ReservationModel> reservationModelMutableLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();
    private String lang = "ar";

    public ActivityConfirmRequestMvvm(@NonNull Application application) {
        super(application);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public MutableLiveData<Boolean> getConfirmMutableLiveData() {
        if (confirmMutableLiveData == null) {
            confirmMutableLiveData = new MutableLiveData<>();
        }
        return confirmMutableLiveData;
    }

    public MutableLiveData<ReservationModel> getReservation() {
        if (reservationModelMutableLiveData == null) {
            reservationModelMutableLiveData = new MutableLiveData<>();
        }
        return reservationModelMutableLiveData;
    }

    public void confirmRequest(Context context, RequestServiceModel model, UserModel userModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        Date m_date = null;
        try {
            m_date = dateFormat.parse(model.getDate() + " " + model.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String m_date = sendServiceModel.getOrder_date();

        RequestBody complaint = Common.getRequestBodyText(model.getComplaint());
        List<MultipartBody.Part> imageList = getMultipartBodyList(model.getImages(), "images[]", context);
        RequestBody service_id = Common.getRequestBodyText(model.getService_id() + "");
        RequestBody date_time = Common.getRequestBodyText((m_date.getTime()/1000)+"");
        RequestBody total_price = Common.getRequestBodyText(model.getTotal_price());
        RequestBody latitude = Common.getRequestBodyText(model.getLatitude());
        RequestBody longitude = Common.getRequestBodyText(model.getLongitude());
        RequestBody address = Common.getRequestBodyText(model.getAddress());
        RequestBody lang = Common.getRequestBodyText(getLang());


        Api.getService(Tags.base_url).confirmRequest(userModel.getData().getAccess_token(), complaint, imageList, service_id, date_time, total_price, latitude, longitude, address, lang)
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

                        Log.e("stt",response.code()+"");
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

    public void updateRequest(Context context, RequestServiceModel model, UserModel userModel,ReservationModel reservationModel) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.ENGLISH);
        Date m_date = null;
        try {
            m_date = dateFormat.parse(model.getDate() + " " + model.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        String m_date = sendServiceModel.getOrder_date();

        RequestBody complaint = Common.getRequestBodyText(model.getComplaint());
        List<MultipartBody.Part> imageList = getMultipartBodyList(model.getImages(), "images[]", context);
        RequestBody service_id = Common.getRequestBodyText(model.getService_id() + "");
        RequestBody date_time = Common.getRequestBodyText((m_date.getTime()/1000)+"");
        RequestBody total_price = Common.getRequestBodyText(model.getTotal_price());
        RequestBody latitude = Common.getRequestBodyText(model.getLatitude());
        RequestBody longitude = Common.getRequestBodyText(model.getLongitude());
        RequestBody address = Common.getRequestBodyText(model.getAddress());
        RequestBody lang = Common.getRequestBodyText(getLang());
        RequestBody reservation_id=Common.getRequestBodyText(reservationModel.getId());


        Api.getService(Tags.base_url).updateRequest(userModel.getData().getAccess_token(), complaint, imageList, service_id, date_time, total_price, latitude, longitude, address, lang,reservation_id)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleReservationModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleReservationModel> response) {
                        dialog.dismiss();

                        Log.e("stt",response.code()+"");
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                reservationModelMutableLiveData.postValue(response.body().getData());
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
}

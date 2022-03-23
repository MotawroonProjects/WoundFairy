package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.SendOrderModel;
import com.apps.wound_fairy.model.StatusResponse;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivitySendOrderMvvm extends AndroidViewModel{
    private MutableLiveData<Boolean> send;
    private CompositeDisposable disposable = new CompositeDisposable();
    public ActivitySendOrderMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getSend() {
        if (send==null){
            send=new MutableLiveData<>();
        }
        return send;
    }

    public void storeOrder(Context context, SendOrderModel sendOrderModel, UserModel userModel, ProductModel.Product productModel,String amount, String lang){
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .storeOrder(userModel.getData().getAccess_token(),productModel.getId(),amount,sendOrderModel.getLatitude(),sendOrderModel.getLongitude(),sendOrderModel.getAddress(),sendOrderModel.getNote(),(Double.parseDouble(productModel.getPrice())*Integer.parseInt(amount))+"",lang)
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
                        if (response.isSuccessful()){
                            if (response.body()!=null && response.body().getStatus()==200){
                                send.postValue(true);
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

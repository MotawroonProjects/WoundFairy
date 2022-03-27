package com.apps.wound_fairy.mvvm;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.model.CountryModel;
import com.apps.wound_fairy.model.UserModel;
import com.apps.wound_fairy.remote.Api;
import com.apps.wound_fairy.share.Common;
import com.apps.wound_fairy.tags.Tags;
import com.apps.wound_fairy.uis.activity_login.LoginActivity;
import com.apps.wound_fairy.uis.activity_verification_code.VerificationCodeActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityLoginMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityVerificationMvvm";
    private Context context;
    private FirebaseAuth mAuth;
    private String verificationId;
    private String smsCode;
    private boolean canSend = false;
    private String time;
    private String phone, phone_code;
    public MutableLiveData<String> smscode = new MutableLiveData<>();
    public MutableLiveData<Boolean> canresnd = new MutableLiveData<>();
    public MutableLiveData<String> timereturn = new MutableLiveData<>();
    public MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> found = new MutableLiveData<>();
    private MutableLiveData<List<CountryModel>> coListMutableLiveData;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ActivityLoginMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();


    }

    public MutableLiveData<List<CountryModel>> getCoListMutableLiveData() {
        if (coListMutableLiveData == null) {
            coListMutableLiveData = new MutableLiveData<>();
        }
        return coListMutableLiveData;
    }

    public void setCountry() {
        CountryModel[] countries = new CountryModel[]{
                new CountryModel("EG", "Egypt", "+20", R.drawable.flag_eg, "EGP"), new CountryModel("SA", "Saudi Arabia", "+966", R.drawable.flag_sa, "SAR")};
        coListMutableLiveData.postValue(new ArrayList<>(Arrays.asList(countries)));
    }

    public void sendSmsCode(String lang, String phone_code, String phone, LoginActivity activity) {

        startTimer();
        this.phone_code = phone_code;
        this.phone = phone;
        mAuth.setLanguageCode(lang);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                smsCode = phoneAuthCredential.getSmsCode();
                smscode.postValue(smsCode);
                checkValidCode(smsCode, activity);
            }

            @Override
            public void onCodeSent(@NonNull String verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                verificationId = verification_id;
            }


            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("dkdkdk", e.toString());
                if (e.getMessage() != null) {
                } else {

                }
            }
        };
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phone_code + phone,
                        120,
                        TimeUnit.SECONDS,
                        activity,
                        mCallBack

                );


    }

    private void startTimer() {
        canSend = false;
        canresnd.postValue(canSend);
        Observable.intervalRange(1, 120, 1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        long diff = 120 - aLong;
                        int min = ((int) diff / 60);
                        int sec = ((int) diff % 60);
                        time = String.format(Locale.ENGLISH, "%02d:%02d", min, sec);
                        timereturn.postValue(time);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        canSend = true;
                        time = "00:00";
                        timereturn.postValue("00:00");

                        canresnd.postValue(true);
                    }
                });

    }


    public void checkValidCode(String code, LoginActivity activity) {
        login(activity);
        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        login(activity);
                    }).addOnFailureListener(e -> {
                if (e.getMessage() != null) {
                } else {

                }
            });
        } else {
            // Toast.makeText(context, "wait sms", Toast.LENGTH_SHORT).show();
        }

    }

    private void login(Context context) {
        ProgressDialog dialog = Common.createProgressDialog(context, context.getResources().getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).login( phone_code, phone).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io()).subscribe(new SingleObserver<Response<UserModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull Response<UserModel> userModelResponse) {
                dialog.dismiss();

                if (userModelResponse.isSuccessful()) {
                    Log.e("status", userModelResponse.body().getStatus() + "");
                    if (userModelResponse.body().getStatus() == 200) {

                        userModelMutableLiveData.postValue(userModelResponse.body());
                    } else if (userModelResponse.body().getStatus() == 422) {
                        found.postValue("not_found");
                    }

                } else {

                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                dialog.dismiss();
                Log.e("ddkkdkd",e.toString());

            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

package com.apps.wound_fairy.services;


import com.apps.wound_fairy.model.AboutAusModel;
import com.apps.wound_fairy.model.BlogDataModel;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.model.ServiceModel;
import com.apps.wound_fairy.model.NotificationDataModel;
import com.apps.wound_fairy.model.PlaceGeocodeData;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.model.SingleBlogModel;
import com.apps.wound_fairy.model.SingleProductModel;
import com.apps.wound_fairy.model.SliderDataModel;
import com.apps.wound_fairy.model.SliderProductModel;
import com.apps.wound_fairy.model.StatusResponse;
import com.apps.wound_fairy.model.UserModel;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @GET("geocode/json")
    Single<Response<PlaceGeocodeData>> getGeoData(@Query(value = "latlng") String latlng,
                                                  @Query(value = "language") String language,
                                                  @Query(value = "key") String key);


    @FormUrlEncoded
    @POST("api/auth/login")
    Single<Response<UserModel>> login(@Field("phone_code") String phone_code,
                                      @Field("phone") String phone);


    @Multipart
    @POST("api/auth/register")
    Single<Response<UserModel>> signUp(@Part("phone_code") RequestBody phone_code,
                                       @Part("phone") RequestBody phone,
                                       @Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part MultipartBody.Part image

    );


    @FormUrlEncoded
    @POST("api/auth/logout")
    Single<Response<StatusResponse>> logout(@Header("Authorization") String Authorization,
                                            @Field("phone_token") String phone_token

    );

    @GET("api/home/slider")
    Single<Response<SliderDataModel>> getSlider();

    @GET("api/home/setting")
    Single<Response<SettingsModel>> getSettings();


    @FormUrlEncoded
    @POST("api/auth/insert_token")
    Single<Response<StatusResponse>> updateFirebasetoken(@Header("Authorization") String Authorization,
                                                         @Field("phone_token") String phone_token,
                                                         @Field("type") String type

    );

    @GET("api/home/blogs")
    Single<Response<BlogDataModel>> getBlogs(@Query("lang") String lang);

    @GET("api/home/blogs")
    Single<Response<SingleBlogModel>> getBlogDetails(@Query("blog_id") String blog_id,
                                                     @Query("lang") String lang);

    @GET("api/home/products")
    Single<Response<ProductModel>> getProducts(@Query("product_id") String product_id,
                                               @Query("lang") String lang,
                                               @Query("search") String search);

    @GET("api/home/products")
    Single<Response<SingleProductModel>> getSingleProduct(@Query("product_id") String product_id,
                                                          @Query("lang") String lang);

    @FormUrlEncoded
    @POST("api/contact-us")
    Single<Response<StatusResponse>> contactUs(@Field("api_key") String api_key,
                                               @Field("name") String name,
                                               @Field("email") String email,
                                               @Field("subject") String phone,
                                               @Field("message") String message


    );


    @GET("api/notifications")
    Single<Response<NotificationDataModel>> getNotifications(@Header("AUTHORIZATION") String token,
                                                             @Query(value = "api_key") String api_key,
                                                             @Query(value = "user_id") String user_id
    );

    @GET("api/home/about-us")
    Single<Response<AboutAusModel>> getAboutUs(@Query("lang") String lang);

    @GET("api/home/online-consultations")
    Single<Response<ServiceModel>> getServiceDetails(@Query("lang") String lang,
                                              @Query("type") String type);

    @GET("api/home/services")
    Single<Response<ServiceDepartmentModel>> getServiceDepartment(@Query("lang") String lang);

}
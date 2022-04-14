package com.apps.wound_fairy.services;


import com.apps.wound_fairy.model.AboutAusModel;
import com.apps.wound_fairy.model.BlogDataModel;
import com.apps.wound_fairy.model.MessagesDataModel;
import com.apps.wound_fairy.model.OrderDataModel;
import com.apps.wound_fairy.model.PaymentDataModel;
import com.apps.wound_fairy.model.ReservationDataModel;
import com.apps.wound_fairy.model.PlaceMapDetailsData;
import com.apps.wound_fairy.model.ProductModel;
import com.apps.wound_fairy.model.ServiceDepartmentModel;
import com.apps.wound_fairy.model.SingleMessageModel;
import com.apps.wound_fairy.model.SingleOrderDataModel;
import com.apps.wound_fairy.model.SingleReservationModel;
import com.apps.wound_fairy.model.VisitOnlineModel;
import com.apps.wound_fairy.model.NotificationDataModel;
import com.apps.wound_fairy.model.PlaceGeocodeData;
import com.apps.wound_fairy.model.SettingsModel;
import com.apps.wound_fairy.model.SingleBlogModel;
import com.apps.wound_fairy.model.SingleProductModel;
import com.apps.wound_fairy.model.SliderDataModel;
import com.apps.wound_fairy.model.StatusResponse;
import com.apps.wound_fairy.model.UserModel;

import java.util.List;

import io.reactivex.Observable;
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

    @GET("place/findplacefromtext/json")
    Single<Response<PlaceMapDetailsData>> searchOnMap(@Query(value = "inputtype") String inputtype,
                                                      @Query(value = "input") String input,
                                                      @Query(value = "fields") String fields,
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
    @POST("api/home/contact-us")
    Single<Response<StatusResponse>> contactUs(@Field("name") String name,
                                               @Field("email") String email,
                                               @Field("subject") String subject,
                                               @Field("message") String message

    );


    @GET("api/home/notifications")
    Single<Response<NotificationDataModel>> getNotifications(@Header("Authorization") String Authorization
    );

    @GET("api/home/about-us")
    Single<Response<AboutAusModel>> getAboutUs(@Query("lang") String lang);

    @GET("api/home/online-consultations")
    Single<Response<VisitOnlineModel>> getVisitOnlineDetails(@Query("lang") String lang,
                                                             @Query("type") String type);

    @GET("api/home/services")
    Single<Response<ServiceDepartmentModel>> getServiceDepartment(@Query("lang") String lang);


    @Multipart
    @POST("api/reservation/store-reservation")
    Single<Response<StatusResponse>> confirmRequest(@Header("Authorization") String Authorization,
                                                    @Part("complaint") RequestBody complaint,
                                                    @Part List<MultipartBody.Part> images,
                                                    @Part("service_id") RequestBody service_id,
                                                    @Part("date_time") RequestBody date_time,
                                                    @Part("total_price") RequestBody total_price,
                                                    @Part("latitude") RequestBody latitude,
                                                    @Part("longitude") RequestBody longitude,
                                                    @Part("address") RequestBody address,
                                                    @Part("lang") RequestBody lang

    );

    @Multipart
    @POST("api/chat/store-chat-data")
    Single<Response<PaymentDataModel>> requestChat(@Header("Authorization") String Authorization,
                                                   @Part("complaint") RequestBody complaint,
                                                   @Part List<MultipartBody.Part> images,
                                                   @Part("type") RequestBody type


    );

    @GET("api/reservation/getReservations")
    Single<Response<ReservationDataModel>> getReservations(@Header("Authorization") String Authorization,
                                                           @Query("lang") String lang);

    @FormUrlEncoded
    @POST("api/order/store-order")
    Single<Response<PaymentDataModel>> storeOrder(@Header("Authorization") String Authorization,
                                                  @Field("product_id") String product_id,
                                                  @Field("amount") String amount,
                                                  @Field("latitude") String latitude,
                                                  @Field("longitude") String longitude,
                                                  @Field("address") String address,
                                                  @Field("note") String note,
                                                  @Field("total_price") String total_price,
                                                  @Field("lang") String lang

    );

    @GET("api/order/getOrders")
    Single<Response<OrderDataModel>> getOrders(@Header("Authorization") String Authorization,
                                               @Query("lang") String lang);


    @GET("api/chat/user-chat")
    Single<Response<MessagesDataModel>> getChatMessages(@Header("Authorization") String Authorization);

    @Multipart
    @POST("api/chat/store-chat-message")
    Single<Response<SingleMessageModel>> sendMessages(@Header("Authorization") String Authorization,
                                                      @Part("type") RequestBody type,
                                                      @Part("text") RequestBody message,
                                                      @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/reservation/edit-reservation")
    Single<Response<SingleReservationModel>> updateRequest(@Header("Authorization") String Authorization,
                                                           @Part("complaint") RequestBody complaint,
                                                           @Part List<MultipartBody.Part> images,
                                                           @Part("service_id") RequestBody service_id,
                                                           @Part("date_time") RequestBody date_time,
                                                           @Part("total_price") RequestBody total_price,
                                                           @Part("latitude") RequestBody latitude,
                                                           @Part("longitude") RequestBody longitude,
                                                           @Part("address") RequestBody address,
                                                           @Part("lang") RequestBody lang,
                                                           @Part("reservation_id") RequestBody reservation_id
    );

    @Multipart
    @POST("api/auth/update_profile")
    Observable<Response<UserModel>> updateProfile(@Header("Authorization") String Authorization,
                                                  @Part("phone_code") RequestBody phone_code,
                                                  @Part("phone") RequestBody phone,
                                                  @Part("name") RequestBody name,
                                                  @Part("email") RequestBody email,
                                                  @Part MultipartBody.Part image

    );

    @FormUrlEncoded
    @POST("api/order/edit-order")
    Single<Response<SingleOrderDataModel>> updateOrder(@Header("Authorization") String Authorization,
                                                       @Field("product_id") String product_id,
                                                       @Field("order_id") String order_id,
                                                       @Field("amount") String amount,
                                                       @Field("latitude") String latitude,
                                                       @Field("longitude") String longitude,
                                                       @Field("address") String address,
                                                       @Field("note") String note,
                                                       @Field("total_price") String total_price,
                                                       @Field("lang") String lang

    );

    @GET("api/chat/user-chat-data")
    Single<Response<MessagesDataModel>> getChatData(@Header("Authorization") String Authorization);
}
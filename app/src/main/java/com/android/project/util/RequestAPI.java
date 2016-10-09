package com.android.project.util;

import com.android.project.model.Record;
import com.android.project.model.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Lobster on 19.06.16.
 */

public interface RequestAPI {

    @GET("sign_up")
    Observable<Void> signUp(@Query("name") String name, @Query("password") String password, @Query("email") String email);

    @GET("check_name")
    Observable<Void> checkName(@Query("name") String name);

    @GET("check_email")
    Observable<Void> checkEmail(@Query("email") String email);

    @GET("remind_info")
    Observable<Void> remindInfo(@Query("email") String email);

    @GET("sign_in")
    Observable<Void> signIn(@Query("name") String name, @Query("password") String password);

    @GET("get_last_records")
    Observable<List<Record>> getLastRecords();

    @GET("get_next_records")
    Observable<List<Record>> getNextRecords(@Query("recordId") Long recordId);

    @GET("get_user_info")
    Observable<User> getUserInfo(@Query("name") String name);

    @GET("get_last_user_records")
    Observable<List<Record>> getLastUserRecords(@Query("name") String name);

    @GET("get_next_user_records")
    Observable<List<Record>> getNextUserRecords(@Query("name") String name, @Query("recordId") Long recordId);

    @GET("get_users")
    Observable<List<User>> getUsers(@Query("searchQuery") String searchQuery);

    @GET("get_users_from_id")
    Observable<List<User>> getUsersFromId(@Query("searchQuery") String searchQuery, @Query("userId") Long userId);

    @POST("save_vote")
    Observable<Void> sendVote(@Query("userName") String userName, @Query("recordId") Long recordId, @Query("option") String option);

    @Multipart
    @POST("update_avatar")
    Observable<User> updateAvatar(@Part("avatar\"; filename=\"*.png") RequestBody file, @Part("name") RequestBody name);

    @Multipart
    @POST("update_background")
    Observable<User> updateBackground(@Part("background\"; filename=\"*.png") RequestBody file, @Part("name") RequestBody name);

    @Multipart
    @POST("add_record")
    Observable<Void> addRecord(@Part("files\"; filename=\"*.png") List<RequestBody> files,
                               @Part("options") List<RequestBody> options,
                               @Part("name") RequestBody name,
                               @Part("title") RequestBody title);

}

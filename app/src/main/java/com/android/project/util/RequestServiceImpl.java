package com.android.project.util;

import android.util.Log;

import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lobster on 31.05.16.
 */
public class RequestServiceImpl implements RequestService {

    public static final String BASE_URL = "http://10.0.3.2:8080/project/";
    public static final String IMAGE_FOLDER = "images/";
    private static final String TAG = RequestServiceImpl.class.getName();
    //public static final String BASE_URL = "http://52.27.160.199:8080/project/";
    private RequestAPI mRequest;

    public RequestServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRequest = retrofit.create(RequestAPI.class);
    }

    @Override
    public void signUp(String name, String password, String email) {
        Call<Void> signUpCall = mRequest.signUp(name, password, email);
        signUpCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "Request code: " + String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void checkName(String name, final Checking callback) {
        Call<Void> checkNameCall = mRequest.checkName(name);
        checkNameCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.checkResult(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void signIn(String name, String password, final Checking callback) {
        Call<Void> signInCall = mRequest.signIn(name, password);
        signInCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.checkResult(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void checkEmail(String email, final Checking callback) {
        Call<Void> checkEmailCall = mRequest.checkEmail(email);
        checkEmailCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.checkResult(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void remindInfo(String email, final Checking callback) {
        Call<Void> remindInfoCall = mRequest.remindInfo(email);
        remindInfoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.checkResult(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void getNextRecords(final Long recordId, final LoadNextRecordsCallback callback) {
        Call<List<Record>> nextRecordsCall = mRequest.getNextRecords(recordId);
        nextRecordsCall.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                if (response.body() != null){
                    callback.onNextRecordsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getLastRecords(final LoadLastRecordsCallback callback) {
        Call<List<Record>> lastRecordsCall = mRequest.getLastRecords();
        lastRecordsCall.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                Log.i(TAG, "Records have been received successfully");
                callback.onLastRecordsLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                Log.e(TAG, "Records have not been received");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void getLastUserRecords(String userName, final LoadLastUserRecordsCallback callback) {
        Call<List<Record>> lastUserRecordsCall = mRequest.getLastUserRecords(userName);
        lastUserRecordsCall.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                if (response.body() != null){
                    callback.onLastUserRecordsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getNextUserRecords(String userName, Long recordId, final LoadNextUserRecordsCallback callback) {
        Call<List<Record>> nextUserRecordsCall = mRequest.getNextUserRecords(userName, recordId);
        nextUserRecordsCall.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                if (response.body() != null){
                    callback.onNextUserRecordsLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {

            }
        });
    }


    @Override
    public void getRecordById(Long recordId, final LoadRecord callback) {
        Call<Record> recordCall = mRequest.getRecordById(recordId);
        recordCall.enqueue(new Callback<Record>() {
            @Override
            public void onResponse(Call<Record> call, Response<Record> response) {
                callback.recordLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Record> call, Throwable t) {

            }
        });
    }

    @Override
    public void getUserInfo(String name, final LoadUserInfo callback) {
        Call<User> userInfoCall = mRequest.getUserInfo(name);
        userInfoCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                callback.onUserInfoLoaded(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void addRecord(List<File> files, List<String> options, String name, String title, final NewRecord callback) {
        List<RequestBody> requestFiles = new ArrayList<>();
        List<RequestBody> requestOptions = new ArrayList<>();

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        RequestBody requestTitle =
                RequestBody.create(MediaType.parse("text/plain"), title);

        for (File f: files){
            requestFiles.add(RequestBody.create(MediaType.parse("image/png"), f));
        }

        for (String s: options){
            requestOptions.add(RequestBody.create(MediaType.parse("text/plain"), s));
        }

        Call<ResponseBody> call = mRequest.addRecord(requestFiles, requestOptions, requestName, requestTitle);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v(TAG, "New record was loaded successfully");
                callback.newRecordLoaded();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "New record wasn't loaded");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void updateBackground(File background, String name, final LoadUserInfo callback) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), background);

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        Call<User> call = mRequest.updateBackground(requestFile, requestName);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                callback.onUserInfoLoaded(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void updateAvatar(File avatar, String name, final LoadUserInfo callback) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), avatar);

        RequestBody requestName =
                RequestBody.create(MediaType.parse("text/plain"), name);

        Call<User> call = mRequest.updateAvatar(requestFile, requestName);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                callback.onUserInfoLoaded(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void getUsers(String searchQuery, final LoadUsers callback) {
        Call<List<User>> usersCall = mRequest.getUsers(searchQuery);
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body()!=null) {
                    callback.usersLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public void getUsersFromId(String searchQuery, Long userId, final LoadUsers callback) {
        Call<List<User>> usersCall = mRequest.getUsersFromId(searchQuery, userId);
        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body()!=null) {
                    callback.usersLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public void sendVote(final Long recordId, final Option option, final String userName, final NewVote callback) {
        Call<Void> sendVoteCall = mRequest.sendVote(userName, recordId, option.getOptionName());
        sendVoteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.voteSent(recordId, option, userName);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}


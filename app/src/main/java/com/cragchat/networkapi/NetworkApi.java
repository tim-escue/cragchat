package com.cragchat.networkapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cragchat.mobile.database.models.Tag;
import com.cragchat.mobile.database.models.TagRealmListConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import io.realm.RealmList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by timde on 10/12/2017.
 */

public class NetworkApi {

    private static CragChatApi api;

    static {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<RealmList<Tag>>() {
                        }.getType(),
                        new TagRealmListConverter())
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response response = chain.proceed(chain.request());
                                Log.w("Retrofit@Response", response.peekBody(Long.MAX_VALUE).string());
                                return response;
                            }
                        }
                )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-54-148-84-77.us-west-2.compute.amazonaws.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(CragChatApi.class);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static CragChatApi getInstance() {
        return api;
    }

}

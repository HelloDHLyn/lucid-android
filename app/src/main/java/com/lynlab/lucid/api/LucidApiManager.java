package com.lynlab.lucid.api;

import android.content.Context;

import com.lynlab.lucid.util.PreferenceUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author lyn
 * @since 2016/09/03
 */
public class LucidApiManager {

    private static final String BASE_URL = "http://lucid.lynlab.co.kr/";

    private static LucidApiManager instance;
    private static LucidApi.ApiService service;

    public static LucidApiManager getInstance(Context context) {
        if (instance == null) {
            instance = new LucidApiManager(context);
        }

        return instance;
    }

    public LucidApiManager(final Context context) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    // 요청에 액세스 토큰을 추가
                    final String accessToken = PreferenceUtil.getStringPreference(context, PreferenceUtil.KEY_ACCESS_TOKEN, "");
                    Request request = chain.request().newBuilder()
                            .header("X-Access-Token", accessToken)
                            .build();

                    return chain.proceed(request);
                }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient)
                .build();
        service = retrofit.create(LucidApi.ApiService.class);
    }

    public LucidApi.ApiService getService() {
        return service;
    }
}

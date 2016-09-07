package com.lynlab.lucid.api;

import com.lynlab.lucid.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author lyn
 * @since 2016/09/03
 */
public class LucidApi {

    public interface ApiService {

        ////////// 애플리케이션 관련 API

        /**
         * 모든 애플리케이션 목록을 불러온다.
         *
         * @return 애플리케이션 목록
         */
        @GET("api/v1/applications")
        Call<List<Application>> getApplications();


        ////////// 배포 관련 API

        /**
         * 모든 애플리케이션 배포를 불러온다.
         *
         * @return 배포 목록
         */
        @GET("api/v1/releases")
        Call<List<Application>> getRelease();


        ////////// 계정 관련 API

        /**
         * 로그인을 시도한다.
         *
         * @param email    계정 이메일
         * @param password 계정 비밀번호 (암호화)
         * @return JSONObject.exist (boolean)
         */
        @FormUrlEncoded
        @POST("api/v1/users")
        Call<AccessToken> login(
                @Field("email") String email,
                @Field("password") String password
        );
    }
}

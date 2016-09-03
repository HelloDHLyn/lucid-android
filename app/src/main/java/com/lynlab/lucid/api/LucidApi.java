package com.lynlab.lucid.api;

import com.lynlab.lucid.model.Application;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author lyn
 * @since 2016/09/03
 */
public class LucidApi {

    public interface ApiService {

        /**
         * 모든 애플리케이션 목록을 불러온다.
         *
         * @return 애플리케이션 목록
         */
        @GET("api/v1/applications")
        Call<List<Application>> getApplications();

        /**
         * 모든 애플리케이션 배포를 불러온다.
         *
         * @return 배포 목록
         */
        @GET("api/v1/releases")
        Call<List<Application>> getRelease();
    }
}

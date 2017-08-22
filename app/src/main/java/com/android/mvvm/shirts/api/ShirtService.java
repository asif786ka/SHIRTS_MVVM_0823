package com.android.mvvm.shirts.api;

import com.android.mvvm.shirts.vo.Shirt;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.http.GET;

/**
 * REST API access points
 */
public interface ShirtService {

    @GET("shirts")
    LiveData<ApiResponse<List<Shirt>>> loadShirts();

}

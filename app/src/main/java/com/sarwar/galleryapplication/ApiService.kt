package com.sarwar.galleryapplication

import com.sarwar.galleryapplication.model.UnsplashApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search/photos")
    fun searchImage(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ) : Call<UnsplashApiResponse>

}
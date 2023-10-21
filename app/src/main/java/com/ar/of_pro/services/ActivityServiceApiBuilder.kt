package com.ar.of_pro.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object ActivityServiceApiBuilder {

    private val BASE_URL = "https://api.imgur.com/3/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(): UploadService {
        return retrofit.create(UploadService::class.java)
    }
}
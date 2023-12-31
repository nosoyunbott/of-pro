package com.ar.of_pro.services

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Headers("Authorization: Client-ID d6b8ed34ca2b6b0")
    @Multipart
    @POST("image")
    fun uploadImage(@Part image:MultipartBody.Part): Call<ResponseBody>



}
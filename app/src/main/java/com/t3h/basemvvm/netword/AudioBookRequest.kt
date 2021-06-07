 package com.t3h.basemvvm.netword

import com.google.gson.JsonObject
import com.t3h.basemvvm.data.model.api.Book
import com.t3h.basemvvm.data.model.api.CateModel
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AudioBookRequest {

    //    http://20.36.46.207:8080/book/trending
    @GET("book/trending")
    fun getTrending(): Observable<List<Book>>
    @GET("category/list")
    fun getCategory(): Observable<List<CateModel>>
    @GET("/book?")
    fun getBookById(@Query("id") id: String): Observable<Book>
    @GET("/book/list")
    fun getListBook(): Observable<List<Book>>
//    /api/v1/users/log-in
    @GET("/search-by-name")
    fun getBookSearch(@Query("name") name: String): Observable<List<Book>>

    @POST("/api/v1/users/log-in")
    fun loginUser(
        @Body user: JsonObject
    ): Observable<JsonObject>
    companion object {
        private const val BASE_URL = "http://40.90.168.71:8080/"
        fun create(): AudioBookRequest {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .callTimeout(10,TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AudioBookRequest::class.java)
        }
    }
}
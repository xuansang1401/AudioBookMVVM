package com.t3h.basemvvm.netword

import com.t3h.basemvvm.data.model.api.Book
import com.t3h.basemvvm.data.model.api.CateModel
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AudioBookRequest {

    //    http://20.36.46.207:8080/book/trending
    @GET("book/trending")
    fun getTrending(): Observable<List<Book>>
    //    http://20.36.38.9:8080/category/list?fbclid=IwAR2oQI8jy8e-hQSivJEyyNoyVT85YipJLTRuuOT88d_f1Rs84mCDZIIC0v0
//    http://20.36.38.9:8080/category/list?fbclid=IwAR2ly3iot3NZLyHqGHDbkmhTQFgHNXRN0fz4mFGTvl7iVjPorQRAI0mWV8o
    @GET("category/list")
    fun getCategory(): Observable<List<CateModel>>
//    http://20.36.46.207:8080//book?id=1
    @GET("/book?")
    fun getBookById(@Query("id") id: String): Observable<Book>
    @GET("/book/list")
    fun getListBook(): Observable<List<Book>>
//    @GET("level/{id_level}/listtrend")
//    fun getRecommendLevel(@Path("id_level") id_level: String): Observable<RecommendModel>
//
//    @GET("levels/{idlevel}/allbook_chap_lesson/{idbook}")
//    fun getBookAndLessonByLevel(
//        @Path("idlevel") id_level: String,
//        @Path("idbook") idbook: String
//    ): Observable<BookLessonModel>
//    https://fonos-clone-api.herokuapp.com/category/list?fbclid=IwAR1vxZLLaMFKnoCkIsvh4v77-_VsNL0sG_gCs9rQsE5GvLeNZS1W6I0v2pE
    companion object {
        private const val BASE_URL = "http://20.36.46.207:8080/"
        fun create(): AudioBookRequest {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
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
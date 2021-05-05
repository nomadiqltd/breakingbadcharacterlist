package com.nomadiq.storeproject.ui

import com.nomadiq.storeproject.ui.character.BreakingBadCharacter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://breakingbadapi.com/api/"

/** Logging */
fun provideLoggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

/**
OkhttpClient for building http request url
 */
private val client = OkHttpClient().newBuilder()
    .addInterceptor(provideLoggingInterceptor())
    .build()


/**
 * Build the Moshi converter that Retrofit will be using.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder.
 */
private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getCharactersAsync] method
 */
interface ApiService {
    /**
     * Returns a Coroutine [List] of [BreakingBadCharacter] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "movies" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("characters")
    suspend fun getCharactersAsync(
    ): List<BreakingBadCharacter>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object BreakingBadApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
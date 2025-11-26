package com.example.lvlup.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.lvlup.data.AuthDataStore

const val BASE_URL = "http://18.116.201.66:8080/"

object RetrofitClient {

    lateinit var authDataStore: AuthDataStore

    // Interceptor para añadir el Token a las peticiones
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        if (this::authDataStore.isInitialized) {
            val token = authDataStore.getToken()
            token?.let {
                builder.header("Authorization", "Bearer $it")
            }
        }
        chain.proceed(builder.build())
    }

    // Interceptor para ver logs en Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productoApiService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }
}
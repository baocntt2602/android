package com.nexxtidea.sample.network.di

import android.app.Application
import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.nexxtidea.sample.core.network.BuildConfig
import com.nexxtidea.sample.network.api.middleware.DefaultInterceptor
import com.nexxtidea.sample.network.data.DefaultTokenStorage
import com.nexxtidea.sample.network.data.TokenStorage
import com.nexxtidea.sample.network.either.NetworkResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  private const val READ_TIMEOUT: Long = 120
  private const val WRITE_TIMEOUT: Long = 120

  @Singleton
  @Provides
  fun provideTokenStorage(
    @ApplicationContext context: Context,
  ): TokenStorage {
    return DefaultTokenStorage.create(context)
  }

  @Singleton
  @Provides
  fun provideHeaderInterceptor(tokenStorage: TokenStorage): DefaultInterceptor = DefaultInterceptor(tokenStorage)

  @Provides
  @Singleton
  internal fun provideCache(application: Application): Cache {
    val cacheSize = (25 * 1024 * 1024).toLong() // 25 MB
    val httpCacheDirectory = File(application.cacheDir, "http-cache")
    return Cache(httpCacheDirectory, cacheSize)
  }

  @Provides
  fun provideOktHttpClient(
    cache: Cache
  ): OkHttpClient {
    val httpClientBuilder = OkHttpClient.Builder()
      .cache(cache)
      .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
      .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
      .hostnameVerifier { _, _ -> true }
    if (BuildConfig.DEBUG) {
      val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      }
      httpClientBuilder.addInterceptor(logging)
    }
    return httpClientBuilder.build()
  }

  @Singleton
  @Provides
  fun provideDefaultRetrofit(
    mapper: ObjectMapper,
    okHttpClient: OkHttpClient,
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl("BASE_URL")
      .addCallAdapterFactory(NetworkResponseCallAdapterFactory())
      .client(okHttpClient)
      .addConverterFactory(JacksonConverterFactory.create(mapper))
      .build()
  }
}

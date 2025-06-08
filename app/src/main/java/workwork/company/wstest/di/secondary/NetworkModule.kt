package workwork.company.wstest.di.secondary

import workwork.company.wstest.core.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import workwork.company.wstest.data.remote.ApiService
import workwork.company.wstest.data.remote.FakeApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val USE_FAKE_API = true

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return if (USE_FAKE_API) {
            FakeApiService()
        } else {
            retrofit.create(ApiService::class.java)
        }
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit): ApiService {
//        return retrofit.create(ApiService::class.java)
//    }
}

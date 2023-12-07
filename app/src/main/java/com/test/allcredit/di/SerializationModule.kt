package com.test.allcredit.di

import com.test.allcredit.application.rest.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://financial-apps.hb.ru-msk.vkcs.cloud/microloans/microloans_second/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePurchaseSubscriptionInfo(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
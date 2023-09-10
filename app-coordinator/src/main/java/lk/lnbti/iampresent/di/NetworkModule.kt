package lk.lnbti.iampresent.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.repo.LectureListRepo
import lk.lnbti.iampresent.service.LectureListService
import lk.lnbti.iampresent.view_model.LectureListViewModel
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Dependency injection using Hilt module that provides network-related functions.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides an instance of OkHttpClient.
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        return okHttpClient.build()
    }

    /**
     * Provides an instance of Retrofit.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String, okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
        return retrofit.build()
    }

    /**
     * Provides the base URL for the API.
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return Constant.BASE_URL
    }

    /**
     * Provides an instance of GsonConverterFactory.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideLectureListService(retrofit: Retrofit): LectureListService {
        return retrofit.create(LectureListService::class.java)
    }

    @Singleton
    @Provides
    fun provideLectureListRepo(lectureListService: LectureListService): LectureListRepo {
        return LectureListRepo(lectureListService)
    }
//    @Singleton
//    @Provides
//    fun provideLectureListViewModel(lectureListRepo: LectureListRepo): LectureListViewModel {
//        return LectureListViewModel(lectureListRepo)
//    }
}
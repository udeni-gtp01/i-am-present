package lk.lnbti.iampresent.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lk.lnbti.app_student.repo.AttendanceRepo
import lk.lnbti.app_student.service.AttendanceService
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.dao.LectureService
import lk.lnbti.iampresent.ui_state.LectureInfoUiState
import lk.lnbti.iampresent.ui_state.LectureListUiState
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
    fun provideLectureListService(retrofit: Retrofit): LectureService {
        return retrofit.create(LectureService::class.java)
    }

    @Singleton
    @Provides
    fun provideLectureListRepo(lectureListService: LectureService): LectureRepo {
        return LectureRepo(lectureListService)
    }

    @Singleton
    @Provides
    fun provideAttendanceService(retrofit: Retrofit): AttendanceService {
        return retrofit.create(AttendanceService::class.java)
    }

    @Singleton
    @Provides
    fun provideAttendanceRepo(attendanceService: AttendanceService): AttendanceRepo {
        return AttendanceRepo(attendanceService)
    }
    @Singleton
    @Provides
    fun provideLectureListUiState(): LectureListUiState {
        return LectureListUiState()
    }
    @Singleton
    @Provides
    fun provideLectureInfoUiState(): LectureInfoUiState {
        return LectureInfoUiState()
    }
}
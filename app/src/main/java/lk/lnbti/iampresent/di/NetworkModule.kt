package lk.lnbti.iampresent.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.dao.AttendanceDao
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.dao.LectureDao
import lk.lnbti.iampresent.repo.AttendanceRepo
import lk.lnbti.iampresent.ui_state.LectureInfoUiState
import lk.lnbti.iampresent.ui_state.LectureListUiState
import lk.lnbti.iampresent.view_model.AttendanceListUiState
import lk.lnbti.iampresent.view_model.TodaysLectureListUiState
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
    fun provideLectureDao(retrofit: Retrofit): LectureDao {
        return retrofit.create(LectureDao::class.java)
    }
    @Singleton
    @Provides
    fun provideAttendanceDao(retrofit: Retrofit): AttendanceDao {
        return retrofit.create(AttendanceDao::class.java)
    }
    @Singleton
    @Provides
    fun provideLectureRepo(lectureDao: LectureDao): LectureRepo {
        return LectureRepo(lectureDao)
    }

    @Singleton
    @Provides
    fun provideAttendanceRepo(attendanceDao: AttendanceDao): AttendanceRepo {
        return AttendanceRepo(attendanceDao)
    }
    @Singleton
    @Provides
    fun provideLectureListUiState(): LectureListUiState {
        return LectureListUiState()
    }
    @Singleton
    @Provides
    fun provideAttendanceListUiState(): AttendanceListUiState {
        return AttendanceListUiState()
    }
    @Singleton
    @Provides
    fun provideTodaysLectureListUiState(): TodaysLectureListUiState {
        return TodaysLectureListUiState()
    }
    @Singleton
    @Provides
    fun provideLectureInfoUiState(): LectureInfoUiState {
        return LectureInfoUiState()
    }
}
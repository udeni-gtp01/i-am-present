package lk.lnbti.iampresent.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lk.lnbti.app_student.view_model.AttendanceInfoUiState
import javax.inject.Singleton

/**
 * Dagger Hilt module responsible for providing dependencies related to the student network.
 */
@Module
@InstallIn(SingletonComponent::class)
object StudentNetworkModule {
    /**
     * Provides an instance of AttendanceInfoUiState.
     */
    @Singleton
    @Provides
    fun provideAttendanceInfoUiState(): AttendanceInfoUiState {
        return AttendanceInfoUiState()
    }
}
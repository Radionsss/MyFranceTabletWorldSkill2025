package workwork.company.wstest.di.secondary

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import workwork.company.wstest.domain.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesManagerModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(
        @ApplicationContext context: Context
    ): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }
}
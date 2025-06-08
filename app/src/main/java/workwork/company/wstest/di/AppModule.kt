package workwork.company.wstest.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import workwork.company.wstest.di.secondary.NetworkModule
import workwork.company.wstest.di.secondary.RepositoryModule
import workwork.company.wstest.di.secondary.SharedPreferencesManagerModule


@Module(includes = [ RepositoryModule::class, NetworkModule::class, SharedPreferencesManagerModule::class,])
@InstallIn(SingletonComponent::class)
object AppModule

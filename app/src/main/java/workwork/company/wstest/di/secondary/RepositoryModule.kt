package workwork.company.wstest.di.secondary

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import workwork.company.wstest.data.MainRepositoryImpl
import workwork.company.wstest.domain.MainRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Suppress("FunctionName")
    @Binds
    fun bindClassRepositoryImpl_to_ClassRepository(mainRepositoryImpl:MainRepositoryImpl): MainRepository
}
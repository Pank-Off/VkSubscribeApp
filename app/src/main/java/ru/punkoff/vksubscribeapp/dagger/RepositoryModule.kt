package ru.punkoff.vksubscribeapp.dagger

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.punkoff.vksubscribeapp.repository.*

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindRepository(repositoryImpl: RepositoryImpl): Repository

    @Binds
    fun bindLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    fun bindNetworkRepository(networkRepositoryImpl: NetworkRepositoryImpl): NetworkRepository
}

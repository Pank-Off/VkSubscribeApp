package ru.punkoff.vksubscribeapp.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.punkoff.vksubscribeapp.data.room.Database
import ru.punkoff.vksubscribeapp.data.room.SubscribeDao
import javax.inject.Singleton

@Module(includes = [DaoModule::class])
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(applicationContext, Database::class.java, "vk_subscription.db")
            .build()
}

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    fun provideSubscribeDao(database: Database): SubscribeDao {
        return database.subscribeDao()
    }
}

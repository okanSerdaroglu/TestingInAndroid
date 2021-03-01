package com.okan.market.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import com.okan.market.data.local.ShoppingDao
import com.okan.market.data.local.ShoppingItemDB
import com.okan.market.other.Constants.BASE_URL
import com.okan.market.other.Constants.DATABASE_NAME
import com.okan.market.data.remote.ApiService
import com.okan.market.repositories.DefaultShoppingRepository
import com.okan.market.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
            @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDB::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingDao(
            database: ShoppingItemDB
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
            dao: ShoppingDao,
            api: ApiService
    ) = DefaultShoppingRepository(
            shoppingDao = dao,
            apiService = api
    ) as ShoppingRepository

}
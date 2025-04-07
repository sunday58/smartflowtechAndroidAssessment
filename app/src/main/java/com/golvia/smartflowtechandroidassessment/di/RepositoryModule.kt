package com.golvia.smartflowtechandroidassessment.di

import com.golvia.smartflowtechandroidassessment.repo.InventoryRepository
import com.golvia.smartflowtechandroidassessment.repo.InventoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        articleRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository
}
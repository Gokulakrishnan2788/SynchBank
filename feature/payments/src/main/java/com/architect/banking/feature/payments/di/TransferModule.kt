package com.architect.banking.feature.payments.di

import com.architect.banking.feature.payments.data.TransferRepositoryImpl
import com.architect.banking.feature.payments.domain.TransferRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TransferModule {

    @Binds
    @Singleton
    abstract fun bindTransferRepository(
        impl: TransferRepositoryImpl,
    ): TransferRepository
}

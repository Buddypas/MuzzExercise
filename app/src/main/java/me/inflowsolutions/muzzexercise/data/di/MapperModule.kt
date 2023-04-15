package me.inflowsolutions.muzzexercise.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.inflowsolutions.muzzexercise.data.db.mapper.MessageDtoToMessageMapper
import me.inflowsolutions.muzzexercise.data.db.mapper.UserDtoToUserMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    @Singleton
    fun provideMessageDtoToMessageMapper(): MessageDtoToMessageMapper = MessageDtoToMessageMapper()

    @Provides
    @Singleton
    fun provideUserDtoToUserMapper(): UserDtoToUserMapper = UserDtoToUserMapper()
}
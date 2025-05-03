package com.example.bluetoothchat.di

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.example.bluetoothchat.data.bluetooth.BluetoothService
import com.example.bluetoothchat.util.PermissionsHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
    }

    @Provides
    @Singleton
    fun provideBluetoothService(
        bluetoothAdapter: BluetoothAdapter,
        @ApplicationContext context: Context
    ): BluetoothService {
        return BluetoothService(bluetoothAdapter, context)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providePermissionsHandler(): PermissionsHandler {
        return PermissionsHandler()
    }
}
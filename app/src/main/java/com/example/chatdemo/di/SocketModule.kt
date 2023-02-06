package com.example.chatdemo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SocketModule {
    @Provides
    @Singleton
    fun providesSocketInstance(): Socket = IO.socket("http://192.168.1.59:3000",
        IO.Options().apply {
        transports = arrayOf(WebSocket.NAME)
    })
}
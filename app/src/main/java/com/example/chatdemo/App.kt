package com.example.chatdemo

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.Socket
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var socketClient: Socket

    override fun onCreate() {
        super.onCreate()
        socketClient.on(Socket.EVENT_CONNECT) {
            Log.e("socketClientConnect", ":${Arrays.toString(it)} ")
        }
        socketClient.on(Socket.EVENT_DISCONNECT) {
            Log.e("SocketDisconnect", ":${Arrays.toString(it)} ")
        }
        socketClient.on(Socket.EVENT_CONNECT_ERROR) {
            Log.e("SocketError", ":${Arrays.toString(it)} ")
        }

        socketClient.connect()
    }
}
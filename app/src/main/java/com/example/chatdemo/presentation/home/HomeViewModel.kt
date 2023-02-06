package com.example.chatdemo.presentation.home

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdemo.other.Constants
import com.example.chatdemo.network.model.JoinRoom
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Ack
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val socketClient: Socket,
    private val pref: SharedPreferences
) : ViewModel() {

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    fun onUserNameChange(newUserName: String) {
        _userName.value = newUserName
    }

    fun onRoomNumberChange(newRoomNumber: String) {
        _roomNumber.value = newRoomNumber
    }

    private val _roomNumber = MutableLiveData("")
    val roomNumber: LiveData<String> = _roomNumber

    init {
        if (socketClient.connected()) {
            Log.e("SocketId", "socket id: ${socketClient.id()}")
        }
    }

    fun joinRoom(
        navigateToChat: () -> Unit,
        showToast: (String) -> Unit
    ) {

        pref.edit {
            putString(Constants.USERNAME, userName.value)
            putString(Constants.ROOM_NUMBER, roomNumber.value)
        }

        val jsonData = JSONObject(
            Gson().toJson(
                JoinRoom(
                    pref.getString(Constants.USERNAME, "").toString(),
                    pref.getString(Constants.ROOM_NUMBER, "").toString(),
                    SimpleDateFormat.getInstance().format(Calendar.getInstance().time)
                )
            )
        )

        if (_roomNumber.value.toString().isNotEmpty() && _userName.value.toString().isNotEmpty()) {
            socketClient.emit("room", jsonData, Ack {
                Log.e("SocketRoomAck", ":${Arrays.toString(it)} ")
            })
            navigateToChat()
        } else {
            showToast("Please Enter Valid Inputs")
        }

        Log.e("RoomEmit", "joinRoom:$jsonData ")

    }
}
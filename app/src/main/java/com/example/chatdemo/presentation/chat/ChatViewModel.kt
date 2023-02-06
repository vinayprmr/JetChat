package com.example.chatdemo.presentation.chat

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdemo.network.model.Message
import com.example.chatdemo.other.Constants
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Ack
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketClient: Socket,
    private val pref: SharedPreferences
) : ViewModel() {
    private val _messageText = MutableLiveData("")
    val messageText: LiveData<String> = _messageText

    private var _isSender = MutableLiveData(true)
    val isSender: LiveData<Boolean> = _isSender

    private val _messageListState = MutableStateFlow<MutableList<String>?>(mutableListOf())
    val messageListState = _messageListState.asStateFlow()

    fun onUserNameChange(newMessageText: String) {
        _messageText.value = newMessageText
    }

    init {

        if (!socketClient.connected()) {
            socketClient.connect()
        }
        viewModelScope.launch(Dispatchers.Main) {
            socketClient.on("receive-chat") { receivedArray ->
                val receivedMessage =
                    Gson().fromJson(receivedArray.last().toString(), Message::class.java)
                _messageText.postValue(receivedMessage.msg)
                _messageListState.value?.add(receivedMessage.msg)
                _isSender.postValue(
                    receivedMessage.user == pref.getString(Constants.USERNAME, "").toString()
                )
                Log.e("ReceiveChatEmitterListener", "$receivedMessage")

            }
        }

    }

    fun sendMessage(message: String) {
        if (messageText.value!!.isNotBlank()) {
            val jsonData = JSONObject()
                .put(
                    "time",
                    SimpleDateFormat.getInstance().format(Calendar.getInstance().time)
                )
                .put("room", pref.getString(Constants.ROOM_NUMBER, ""))
                .put("msg", message)
                .put("user", pref.getString(Constants.USERNAME, ""))
            _messageListState.value?.add(message)
            _messageText.value = ""
            socketClient.emit("send-chat", jsonData, Ack {
                Log.e("SendChatAck", Arrays.toString(it))
            })
            Log.e("SendMessage", "sendMessage: $jsonData")
        }
    }
}
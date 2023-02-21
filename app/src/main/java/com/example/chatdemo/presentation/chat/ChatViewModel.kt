package com.example.chatdemo.presentation.chat

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatdemo.network.model.Message
import com.example.chatdemo.network.model.MessageWithFlag
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
    private val _sendMessageText = MutableLiveData("")
    val sendMessageText: LiveData<String> = _sendMessageText

    private val _messageListState = MutableStateFlow<List<MessageWithFlag>>(emptyList())
    val messageListState = _messageListState.asStateFlow()

    fun onSendMessageTextChange(newMessageText: String) {
        _sendMessageText.value = newMessageText
    }

    init {
        if (!socketClient.connected()) {
            socketClient.connect()
        }
        viewModelScope.launch(Dispatchers.Main) {
            socketClient.on("receive-chat") { receivedArray ->
                val receivedMessage =
                    Gson().fromJson(receivedArray.last().toString(), Message::class.java)
                addMessageToTheList(
                    MessageWithFlag(
                        receivedMessage.msg,
                        false,
                        receivedMessage.user
                    )
                )
                Log.e("ReceiveChatEmitterListener", "$receivedMessage")
                Log.e("MessagesList", "${messageListState.value}")

            }
        }
    }

    private fun addMessageToTheList(messageWithFlag: MessageWithFlag) {
        viewModelScope.launch(Dispatchers.IO) {
            _messageListState.value = buildList {
                add(messageWithFlag)
                messageListState.value.forEach { add(it) }
            }
        }
    }


    fun sendMessage(message: String) {
        if (sendMessageText.value!!.isNotBlank()) {
            val jsonData = JSONObject()
                .put(
                    "time",
                    SimpleDateFormat.getInstance().format(Calendar.getInstance().time)
                )
                .put("room", pref.getString(Constants.ROOM_NUMBER, ""))
                .put("msg", message)
                .put("user", pref.getString(Constants.USERNAME, ""))
            addMessageToTheList(
                MessageWithFlag(
                    message,
                    true,
                    pref.getString(Constants.USERNAME, "").toString()
                )
            )
            _sendMessageText.value = ""
            socketClient.emit("send-chat", jsonData, Ack {
                Log.e("SendChatAck", Arrays.toString(it))
            })
            Log.e("SendMessage", "sendMessage: $jsonData")
        }
    }
}
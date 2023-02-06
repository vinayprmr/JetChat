package com.example.chatdemo.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatdemo.presentation.home.HomeViewModel
import com.example.chatdemo.ui.theme.ChatDemoTheme

@Composable
fun HomeScreenUi(
    userName: String = "",
    onUserNameChange: (String) -> Unit = {},
    roomNumber: String = "",
    onRoomNumberChange: (String) -> Unit = {},
    onJoinRoom: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName, label = {
                    Text(text = "Username")
                },
                onValueChange = onUserNameChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = roomNumber, label = {
                    Text(text = "Room no")
                },
                onValueChange = onRoomNumberChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(modifier = Modifier.fillMaxWidth(),
                onClick = { onJoinRoom() }) {
                Text(text = "Join Room")
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userName by viewModel.userName.observeAsState("")
    val roomNumber by viewModel.roomNumber.observeAsState("")
    HomeScreenUi(
        userName = userName,
        onUserNameChange = { viewModel.onUserNameChange(it) },
        roomNumber = roomNumber,
        onRoomNumberChange = { viewModel.onRoomNumberChange(it) },
        onJoinRoom = {
            viewModel.joinRoom(
                navigateToChat = {
                    Log.e("TAGHomeScreen", "HomeScreen: navigate to chat")
                    navController.navigate("chat")
                },
                showToast = {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatDemoTheme {
        HomeScreenUi()
    }
}

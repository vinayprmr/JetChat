package com.example.chatdemo.presentation.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messageList by viewModel.messageListState.collectAsState()
    val messageText by viewModel.messageText.observeAsState("")
    val isSender by viewModel.isSender.observeAsState(true)

    ChatScreenUi(
        messageText = messageText,
        onMessageTextChange = { viewModel.onUserNameChange(it) },
        navigateToHome = {
            navController.popBackStack()
        },
        sendMessage = {
            viewModel.sendMessage(messageText)
        },
        messageList = messageList,
        isSender = isSender
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenUi(
    messageText: String = "",
    onMessageTextChange: (String) -> Unit = {},
    navigateToHome: () -> Unit = {},
    sendMessage: () -> Unit = {},
    messageList: MutableList<String>? = mutableListOf(),
    isSender: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Box(Modifier.fillMaxWidth()) {

                OutlinedIconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = navigateToHome,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Chatbox",
                    style = MaterialTheme.typography.titleLarge
                        .copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                reverseLayout = true
            ) {
                items(messageList!!.toMutableList()) { message ->
                    MessageBubble(message = message, isSender = isSender)
                }
                /*              val groupByTimestampHistoryList = chatState.data.groupBy { it.formattedDate }

                              groupByTimestampHistoryList.forEach { (date, messages) ->
                                  items(messages) { message ->
                                      val isSender = message.sender == loggedInUser?.email
                                      Box(
                                          modifier = Modifier.fillMaxWidth(),
                                          contentAlignment = if (isSender)
                                              Alignment.CenterEnd
                                          else
                                              Alignment.CenterStart
                                      ) {
                                          MessageBubble(
                                              message = message,
                                              isSender = isSender,
                                              loggedInUser?.avatar.orEmpty(),
                                              friendAvatar
                                          )
                                      }
                                  }
                                  stickyHeader {
                                      Box(
                                          modifier = Modifier
                                              .fillMaxWidth()
                                              .padding(20.dp),
                                          contentAlignment = Alignment.Center
                                      ) {
                                          Text(
                                              text = date.orEmpty(),
                                              style = MaterialTheme.typography.bodySmall
                                                  .copy(color = MaterialTheme.colorScheme.onBackground)
                                          )
                                      }
                                  }
                              }*/
            }

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(
                    width = 0.5.dp,
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.background
                )
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = messageText,
                    onValueChange = onMessageTextChange,
                    placeholder = {
                        Text(
                            text = "Type message...",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    },
                    singleLine = false,
                    maxLines = 4,
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                        disabledIndicatorColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    trailingIcon = {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    sendMessage()
                                }
                                .rotate(-45f),
                            imageVector = Icons.Filled.Send,
                            contentDescription = "Send message",
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            sendMessage()
                        }
                    )
                )
            }

        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreenUi()
}
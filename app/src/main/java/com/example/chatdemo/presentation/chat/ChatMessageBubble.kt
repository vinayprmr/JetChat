package com.example.chatdemo.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageBubble(
    message: String,
    isSender: Boolean,
    userName: String
) {

    val radius =
        if (isSender) RoundedCornerShape(
            topStart = 16.dp,
            bottomStart = 16.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp
        ) else RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 16.dp
        )

    Row(
        modifier = Modifier
            .padding(bottom = 24.dp)
    ) {
        if (!isSender) {
            DisplayUsername(userName)
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(0.8f)
                .wrapContentSize(align = if (isSender) Alignment.CenterEnd else Alignment.CenterStart)
                .background(
                    color = if (isSender)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer,
                    shape = radius
                )
                .padding(12.dp),
            text = message,
            color = if (isSender)
                MaterialTheme.colorScheme.onSecondaryContainer
            else
                MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Normal
        )
        if (isSender) {
            DisplayUsername(userName)
        }

    }
}

@Composable
fun DisplayUsername(
    userName: String
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = userName,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
                .copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
        )
    }
}

@Preview
@Composable
fun MessageBubblePreview() {
    MessageBubble(
        message = "this is ",
        isSender = true,
        userName = "sender"
    )
}
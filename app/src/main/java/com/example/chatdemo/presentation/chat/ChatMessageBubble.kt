package com.example.chatdemo.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatdemo.R

@Composable
fun MessageBubble(
    message: String,
    isSender: Boolean
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
    }
}

@Composable
fun AvatarHead() {

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Image(
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color = MaterialTheme.colorScheme.onBackground)
                .align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "Friend avatar"
        )
    }
}

@Preview
@Composable
fun MessageBubblePreview() {
    MessageBubble(
        message = "this is ",
        isSender = true
    )
}
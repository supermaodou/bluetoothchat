package com.example.bluetoothchat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluetoothchat.data.bluetooth.BluetoothService
import com.example.bluetoothchat.data.bluetooth.BluetoothState
import com.example.bluetoothchat.data.model.Message
import com.example.bluetoothchat.ui.viewmodel.BluetoothViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: BluetoothViewModel) {
    val state by viewModel.state.collectAsState()
    val messages by viewModel.messages.collectAsState()
    var messageInput by remember { mutableStateOf("") }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.navigationBars.only(WindowInsetsSides.Start + WindowInsetsSides.End)
        ),
        topBar = @Composable {
            TopAppBar(
                title = {
                    Text(
                        when (state) {
                            is BluetoothState.Connected -> (state as BluetoothState.Connected).deviceName
                            else -> "蓝牙聊天"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                actions = {
                    TextButton(
                        onClick = { viewModel.disconnect() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("断开连接")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF1F1F1))
                .imePadding()
        ) {
            // 消息列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages.reversed()) { message ->
                    MessageItem(message)
                }
            }

            // 输入框和发送按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = { Text("输入消息") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.sendMessage(messageInput)
                        messageInput = ""
                    },
                    enabled = messageInput.isNotBlank() && state is BluetoothState.Connected,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color(0xFFE0E0E0)
                    )
                ) {
                    Text(
                        text = "发送",
                        color = if (messageInput.isNotBlank() && state is BluetoothState.Connected)
                            Color.White
                        else
                            Color(0xFF9E9E9E)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // 时间显示
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        Text(
            text = timeFormat.format(Date(message.timestamp ?: System.currentTimeMillis())),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 12.sp
        )

        // 消息气泡
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.isSent) 16.dp else 4.dp,
                            bottomEnd = if (message.isSent) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (message.isSent) Color(0xFF95EC69)
                        else Color.White
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    color = Color.Black
                )
            }
        }
    }
}
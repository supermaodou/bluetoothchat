package com.example.bluetoothchat.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bluetoothchat.data.bluetooth.BluetoothService
import com.example.bluetoothchat.data.bluetooth.BluetoothState
import com.example.bluetoothchat.ui.viewmodel.BluetoothViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: BluetoothViewModel) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("蓝牙聊天") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (state) {
                    is BluetoothState.Connected -> "已连接到 ${(state as BluetoothState.Connected).deviceName}"
                    is BluetoothState.Listening -> "正在监听连接..."
                    is BluetoothState.Error -> (state as BluetoothState.Error).message
                    else -> "未连接"
                },
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("device_list") },
                enabled = state !is BluetoothState.Connected
            ) {
                Text("扫描设备")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.startServer() },
                enabled = state !is BluetoothState.Connected &&
                        state !is BluetoothState.Listening
            ) {
                Text("启动服务器")
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (state is BluetoothState.Connected) {
                Button(
                    onClick = { navController.navigate("chat") }
                ) {
                    Text("进入聊天")
                }
            }
        }
    }
}
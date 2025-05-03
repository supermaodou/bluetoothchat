package com.example.bluetoothchat.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.bluetoothchat.ui.viewmodel.BluetoothViewModel
import android.bluetooth.BluetoothDevice
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(navController: NavController, viewModel: BluetoothViewModel) {
    val pairedDevices by remember { derivedStateOf { viewModel.getPairedDevices().toList() } }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // 检查权限并显示提示
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.BLUETOOTH_CONNECT"
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            snackbarHostState.showSnackbar("Please grant Bluetooth permission")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Device") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Paired Devices",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            if (pairedDevices.isEmpty()) {
                Text(
                    text = "No paired devices found",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(pairedDevices) { device ->
                        DeviceItem(device = device, onClick = {
                            viewModel.connectToDevice(device)
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceItem(device: BluetoothDevice, onClick: () -> Unit) {
    val context = LocalContext.current
    val deviceName = remember(device) {
        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.BLUETOOTH_CONNECT"
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            try {
                device.name ?: "Unknown Device"
            } catch (e: SecurityException) {
                "Unknown Device"
            }
        } else {
            "Unknown Device"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = deviceName,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = device.address,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
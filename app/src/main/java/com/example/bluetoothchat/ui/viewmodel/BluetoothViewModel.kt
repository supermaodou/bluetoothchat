package com.example.bluetoothchat.ui.viewmodel

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothchat.data.bluetooth.BluetoothService
import com.example.bluetoothchat.data.bluetooth.BluetoothState
import com.example.bluetoothchat.data.model.Message
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BluetoothViewModel @Inject constructor(
    private val bluetoothService: BluetoothService,
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context // 添加 Context 用于权限检查
) : ViewModel() {

    val state: StateFlow<BluetoothState> = bluetoothService.state
    val messages: StateFlow<List<Message>> = bluetoothService.messages

    // 获取已配对设备
    fun getPairedDevices(): Set<BluetoothDevice> {
        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.BLUETOOTH_CONNECT"
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            return emptySet()
        }
        try {
            return bluetoothAdapter.bondedDevices ?: emptySet()
        } catch (e: SecurityException) {
            return emptySet()
        }
    }

    // 启动服务器
    fun startServer() {
        bluetoothService.startServer()
    }

    // 连接设备
    fun connectToDevice(device: BluetoothDevice) {
        bluetoothService.connectToDevice(device)
    }

    // 发送消息
    fun sendMessage(message: String) {
        if (message.isNotBlank()) {
            bluetoothService.sendMessage(message)
        }
    }

    // 断开连接
    fun disconnect() {
        bluetoothService.disconnect()
    }

    // 清理资源
    override fun onCleared() {
        bluetoothService.disconnect()
        super.onCleared()
    }
}
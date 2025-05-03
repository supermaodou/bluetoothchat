package com.example.bluetoothchat.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import androidx.core.content.ContextCompat
import com.example.bluetoothchat.data.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class BluetoothService(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context // 添加上下文以进行权限检查
) {
    private val _state = MutableStateFlow<BluetoothState>(BluetoothState.Disconnected)
    val state: StateFlow<BluetoothState> = _state

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private var socket: BluetoothSocket? = null
    private var serverSocket: BluetoothServerSocket? = null
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // SPP UUID

    // 启动服务器，监听连接
    fun startServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 检查 BLUETOOTH_ADVERTISE 权限
                if (ContextCompat.checkSelfPermission(
                        context,
                        "android.permission.BLUETOOTH_ADVERTISE"
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    _state.value = BluetoothState.Error("缺少 BLUETOOTH_ADVERTISE 权限")
                    return@launch
                }

                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothChat", uuid)
                _state.value = BluetoothState.Listening
                val socket = serverSocket?.accept()
                this@BluetoothService.socket = socket
                _state.value = BluetoothState.Connected(socket?.remoteDevice?.name ?: "未知设备")
                serverSocket?.close()
                serverSocket = null
                listenForMessages()
            } catch (e: SecurityException) {
                _state.value = BluetoothState.Error("权限被拒绝：${e.message}")
            } catch (e: IOException) {
                _state.value = BluetoothState.Error("服务器错误：${e.message}")
            }
        }
    }

    // 连接到设备（客户端）
    fun connectToDevice(device: BluetoothDevice) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 检查 BLUETOOTH_CONNECT 权限
                if (ContextCompat.checkSelfPermission(
                        context,
                        "android.permission.BLUETOOTH_CONNECT"
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    _state.value = BluetoothState.Error("缺少 BLUETOOTH_CONNECT 权限")
                    return@launch
                }

                socket = device.createRfcommSocketToServiceRecord(uuid)
                socket?.connect()
                _state.value = BluetoothState.Connected(device.name ?: "未知设备")
                listenForMessages()
            } catch (e: SecurityException) {
                _state.value = BluetoothState.Error("权限被拒绝：${e.message}")
            } catch (e: IOException) {
                _state.value = BluetoothState.Error("连接错误：${e.message}")
            }
        }
    }

    // 发送消息
    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 检查 BLUETOOTH_CONNECT 权限以进行 socket 操作
                if (ContextCompat.checkSelfPermission(
                        context,
                        "android.permission.BLUETOOTH_CONNECT"
                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    _state.value = BluetoothState.Error("缺少 BLUETOOTH_CONNECT 权限")
                    return@launch
                }

                socket?.outputStream?.write(message.toByteArray())
                _messages.value += Message(message, isSent = true)
            } catch (e: SecurityException) {
                _state.value = BluetoothState.Error("权限被拒绝：${e.message}")
            } catch (e: IOException) {
                _state.value = BluetoothState.Error("发送错误：${e.message}")
            }
        }
    }

    // 监听接收消息
    private fun listenForMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(1024)
            while (socket?.isConnected == true) {
                try {
                    // 检查 BLUETOOTH_CONNECT 权限以进行 socket 操作
                    if (ContextCompat.checkSelfPermission(
                            context,
                            "android.permission.BLUETOOTH_CONNECT"
                        ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        _state.value = BluetoothState.Error("缺少 BLUETOOTH_CONNECT 权限")
                        return@launch
                    }

                    val bytes = socket?.inputStream?.read(buffer) ?: 0
                    val message = String(buffer, 0, bytes)
                    _messages.value += Message(message, isSent = false)
                } catch (e: SecurityException) {
                    _state.value = BluetoothState.Error("权限被拒绝：${e.message}")
                    break
                } catch (e: IOException) {
                    _state.value = BluetoothState.Disconnected
                    break
                }
            }
        }
    }

    // 断开连接
    fun disconnect() {
        try {
            // 检查 BLUETOOTH_CONNECT 权限以进行 socket 操作
            if (ContextCompat.checkSelfPermission(
                    context,
                    "android.permission.BLUETOOTH_CONNECT"
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                _state.value = BluetoothState.Error("缺少 BLUETOOTH_CONNECT 权限")
                return
            }

            socket?.close()
            serverSocket?.close()
            _state.value = BluetoothState.Disconnected
        } catch (e: SecurityException) {
            _state.value = BluetoothState.Error("权限被拒绝：${e.message}")
        } catch (e: IOException) {
            _state.value = BluetoothState.Error("断开连接错误：${e.message}")
        }
    }
}
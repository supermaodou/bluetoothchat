package com.example.bluetoothchat.data.bluetooth

sealed class BluetoothState {
    data object Disconnected : BluetoothState()
    data object Listening : BluetoothState()
    data class Connected(val deviceName: String) : BluetoothState()
    data class Error(val message: String) : BluetoothState()
}
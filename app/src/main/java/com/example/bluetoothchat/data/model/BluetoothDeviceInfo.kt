package com.example.bluetoothchat.data.model

import android.bluetooth.BluetoothDevice

data class BluetoothDeviceInfo(
    val name: String?,
    val address: String,
    val device: BluetoothDevice
)
package com.example.bluetoothchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothchat.ui.screen.ChatScreen
import com.example.bluetoothchat.ui.screen.DeviceListScreen
import com.example.bluetoothchat.ui.screen.HomeScreen
import com.example.bluetoothchat.ui.theme.BluetoothChatTheme
import com.example.bluetoothchat.ui.viewmodel.BluetoothViewModel
import com.example.bluetoothchat.util.PermissionsHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var viewModel: BluetoothViewModel
    @Inject lateinit var permissionsHandler: PermissionsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BluetoothChatTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // 请求权限
                permissionsHandler.RequestPermissions()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController, viewModel)
                    }
                    composable("device_list") {
                        DeviceListScreen(navController, viewModel)
                    }
                    composable("chat") {
                        ChatScreen(viewModel)
                    }
                }
            }
        }
    }
}
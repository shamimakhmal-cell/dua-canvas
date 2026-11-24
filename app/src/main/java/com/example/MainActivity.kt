package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.DuaApp
import com.example.ui.theme.DuaCanvasTheme
import com.example.viewmodel.DuaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuaCanvasTheme {
                val viewModel: DuaViewModel = viewModel()
                DuaApp(viewModel = viewModel)
            }
        }
    }
}

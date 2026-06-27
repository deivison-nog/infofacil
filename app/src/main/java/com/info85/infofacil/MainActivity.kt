package com.info85.infofacil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.info85.infofacil.ui.InfoFacilApp
import com.info85.infofacil.ui.theme.InfoFacilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            InfoFacilTheme {
                InfoFacilApp()
            }
        }
    }
}

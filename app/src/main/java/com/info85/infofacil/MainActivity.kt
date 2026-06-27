package com.info85.infofacil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.info85.infofacil.ui.InfoFacilApp
import com.info85.infofacil.ui.theme.InfoFacilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoFacilTheme {
                InfoFacilApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    InfoFacilTheme {
        InfoFacilApp()
    }
}

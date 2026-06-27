package com.info85.infofacil.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.info85.infofacil.data.ProgressRepository
import com.info85.infofacil.navigation.AppNavGraph

@Composable
fun InfoFacilApp() {
    val context = LocalContext.current
    val progressRepository = ProgressRepository(context.applicationContext)
    val navController = rememberNavController()
    AppNavGraph(
        navController = navController,
        progressRepository = progressRepository,
    )
}

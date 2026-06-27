package com.info85.infofacil.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class BottomNavTab { Home, Modules, Progress }

@Composable
fun BottomNavBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.Home,
            onClick = { onTabSelected(BottomNavTab.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.Modules,
            onClick = { onTabSelected(BottomNavTab.Modules) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Módulos") },
            label = { Text("Módulos") },
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.Progress,
            onClick = { onTabSelected(BottomNavTab.Progress) },
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Progresso") },
            label = { Text("Progresso") },
        )
    }
}

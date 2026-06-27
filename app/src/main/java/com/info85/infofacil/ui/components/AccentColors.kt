package com.info85.infofacil.ui.components

import androidx.compose.ui.graphics.Color
import com.info85.infofacil.model.ModuleAccent
import com.info85.infofacil.ui.theme.BlueAccent
import com.info85.infofacil.ui.theme.BlueSecondary
import com.info85.infofacil.ui.theme.GreenAccentModule
import com.info85.infofacil.ui.theme.OrangeAccent
import com.info85.infofacil.ui.theme.PurpleAccent
import com.info85.infofacil.ui.theme.RoseAccent
import com.info85.infofacil.ui.theme.TealAccent

fun ModuleAccent.toColor(): Color = when (this) {
    ModuleAccent.Blue -> BlueAccent
    ModuleAccent.Green -> GreenAccentModule
    ModuleAccent.Orange -> OrangeAccent
    ModuleAccent.Purple -> PurpleAccent
    ModuleAccent.Teal -> TealAccent
    ModuleAccent.Rose -> RoseAccent
}

fun ModuleAccent.toGradientColors(): List<Color> = when (this) {
    ModuleAccent.Blue -> listOf(Color(0xFF1D4ED8), Color(0xFF0EA5E9))
    ModuleAccent.Green -> listOf(Color(0xFF15803D), Color(0xFF22C55E))
    ModuleAccent.Orange -> listOf(Color(0xFFEA580C), Color(0xFFF59E0B))
    ModuleAccent.Purple -> listOf(Color(0xFF6D28D9), Color(0xFFA855F7))
    ModuleAccent.Teal -> listOf(Color(0xFF0F766E), Color(0xFF2DD4BF))
    ModuleAccent.Rose -> listOf(Color(0xFFBE123C), Color(0xFFF43F5E))
}

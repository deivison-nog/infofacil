package com.info85.infofacil.data

import com.info85.infofacil.model.Module
import com.info85.infofacil.model.ModuleAccent

object LocalModulesDataSource {
    val modules = listOf(
        Module(
            id = "primeiros-passos",
            title = "Primeiros passos",
            description = "Conheça o computador, celular e os conceitos básicos de informática.",
            lessons = 5,
            duration = "25 min",
            progress = 0.85f,
            accent = ModuleAccent.Blue,
        ),
        Module(
            id = "mouse-teclado",
            title = "Mouse e teclado",
            description = "Aprenda cliques, digitação, teclas principais e atalhos simples.",
            lessons = 6,
            duration = "30 min",
            progress = 0.55f,
            accent = ModuleAccent.Green,
        ),
        Module(
            id = "internet-basica",
            title = "Internet básica",
            description = "Descubra como pesquisar, navegar com segurança e usar e-mail.",
            lessons = 7,
            duration = "40 min",
            progress = 0.20f,
            accent = ModuleAccent.Orange,
        ),
        Module(
            id = "seguranca-digital",
            title = "Segurança digital",
            description = "Aprenda a criar senhas fortes e identificar golpes online.",
            lessons = 4,
            duration = "22 min",
            progress = 0f,
            accent = ModuleAccent.Purple,
        ),
    )
}

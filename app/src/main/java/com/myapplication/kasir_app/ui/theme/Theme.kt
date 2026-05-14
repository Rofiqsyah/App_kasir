package com.myapplication.kasir_app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val CafeColorScheme = lightColorScheme(

    primary = BrownPrimary,
    secondary = BrownSecondary,
    tertiary = BrownTertiary,

    background = BrownBackground,
    surface = BrownSurface,

    onPrimary = Cream,
    onSecondary = Cream,
    onBackground = BrownDark,
    onSurface = BrownDark

)
@Composable
fun Kasir_ApkTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = CafeColorScheme,
        typography = Typography(),
        content = content
    )

}
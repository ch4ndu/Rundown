/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package app.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val FireflyAppSerif = FontFamily.SansSerif
//    FontFamily(
//    Font(Res.font.roboto_flex)
//)


interface CommonTypography {

    val headlineLarge: TextStyle
    val headlineMedium: TextStyle
    val titleLarge: TextStyle
    val titleMedium: TextStyle
    val bodyLarge: TextStyle
    val bodyMedium: TextStyle
    val labelLarge: TextStyle
    val labelMedium: TextStyle
//    val button: TextStyle

    fun toTypography(): Typography {
        return Typography(
            headlineLarge = FireflyAppTypography.headlineLarge,
            headlineMedium = FireflyAppTypography.headlineMedium,
            titleLarge = FireflyAppTypography.titleLarge,
            titleMedium = FireflyAppTypography.titleMedium,
            bodyLarge = FireflyAppTypography.bodyLarge,
            bodyMedium = FireflyAppTypography.bodyMedium,
            labelLarge = FireflyAppTypography.labelLarge,
            labelMedium = FireflyAppTypography.labelMedium,
//            button = FireflyAppTypography.button
        )
    }
}

// Set of Material typography styles to start with
object FireflyAppTypography : CommonTypography {

    override val headlineLarge = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Bold, // same as W700
        fontSize = 42.sp
    )
    override val headlineMedium = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Bold, // same as W700
        fontSize = 20.sp
    )
    override val titleLarge = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Bold, // same as W700
        fontSize = 16.sp
    )
    override val titleMedium = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.SemiBold, // same as W600
        fontSize = 16.sp
    )
    override val bodyLarge = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Medium, // same as W500
        fontSize = 16.sp
    )
    override val bodyMedium = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Medium, // same as W500
        fontSize = 14.sp
    )
    override val labelLarge = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Normal, // same as W400
        fontSize = 12.sp
    )
    override val labelMedium = TextStyle(
        fontFamily = FireflyAppSerif,
        fontWeight = FontWeight.Medium, // same as W500
        fontSize = 8.sp
    )
}

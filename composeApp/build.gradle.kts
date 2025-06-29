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

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
//    alias(libs.plugins.skie)
    alias(libs.plugins.room)
    alias(libs.plugins.swiftklib)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
//            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        iosTarget.compilations {
            val main by getting {
                cinterops {
                    create("piechart")
                }
            }
        }
    }
    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }

//    cocoapods {
//        summary = "Some description for the Shared Module"
//        homepage = "Link to the Shared Module homepage"
//        version = "1.0"
//        ios.deploymentTarget = "16.0"
//        podfile = project.file("../iosApp/Podfile")
//        framework {
//            baseName = "shared"
//            isStatic = true
//        }
//    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.room.paging)
            implementation(libs.androidx.paging.runtime)
            implementation(libs.androidx.paging.runtime.ktx)
//            implementation(libs.androidx.paging.compose.android)
            implementation(libs.paging.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.okhttp)
            implementation(libs.okhttp.logging.interceptor)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation(libs.androidx.room.runtime.android)

            implementation(libs.mp.android.chart)
            implementation(libs.vico.compose)
            implementation(libs.vico.core)
            implementation(libs.vico.compose.m3)
            implementation(libs.androidx.work.runtime)
            implementation(libs.androidx.koin.work)
        }

        commonMain.dependencies {
//            implementation("androidx.paging:paging-common:3.3.5")

//            implementation("org.jetbrains.androidx.savedstate:savedstate:1.3.0-beta01")

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
//            implementation(libs.skie.annotations)
            implementation(libs.sqlite.bundled)
            implementation(libs.androidx.room.paging)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.paging.common)
//            implementation(libs.androidx.paging.common)
//            implementation(libs.paging.compose)

            implementation(libs.kotlinx.atomicfu)
            api(libs.androidx.datastore.preferences.core)
            api(libs.androidx.datastore.core.okio)
            implementation(libs.okio)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.navigation.compose)

            implementation(libs.bundles.ktor)

            implementation(libs.ktorfit.lib)
//            implementation(libs.ktorfit.compiler)
            implementation(libs.ktorfit.converters.call)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktorfit.converters.response)
            implementation(libs.kotlinx.serialization)

            api(libs.kmlog)
            implementation(libs.stately.common)

            implementation(libs.androidx.datastore.preferences.core)

            implementation(libs.vico.multiplatform)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.room.paging)
//            implementation(libs.androidx.paging.compose)
            implementation(libs.androidx.room.runtime)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.room.compiler)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.room.paging)
            implementation(libs.slf4j.api)
            implementation(libs.logback.classic)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

dependencies {
    listOf(
        "kspAndroid",
        "kspDesktop",
        "kspIosSimulatorArm64",
        "kspIosX64",
        "kspIosArm64"
    ).forEach {
        add(it, libs.androidx.room.compiler)
    }
}

android {
    namespace = "com.udnahc.firefly"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.udnahc.rundown.dev"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    buildFeatures {
        compose = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    dependencies {
        implementation(libs.mp.android.chart)
        implementation(libs.vico.compose)
        implementation(libs.vico.core)
        implementation(libs.vico.compose.m3)

        implementation(libs.navigation.compose)
        implementation(libs.lifecycle.viewmodel.compose)
//    room-ksp bug
//        add("kspCommonMainMetadata", libs.androidx.room.compiler)

        debugImplementation(compose.uiTooling)
    }
}
kotlin.sourceSets.all {
    languageSettings {
        optIn("kotlin.experimental.ExperimentalObjCName")
        optIn("kotlinx.cinterop.ExperimentalForeignApi")
    }
}

composeCompiler {
    featureFlags = setOf(
//        ComposeFeatureFlag.IntrinsicRemember,
        ComposeFeatureFlag.OptimizeNonSkippingGroups,
//        ComposeFeatureFlag.StrongSkipping
    )

    reportsDestination = layout.buildDirectory.dir("compose_compiler")
//    stabilityConfigurationFile = project.file("compose_compiler_config.txt")
}
room {
    schemaDirectory("$projectDir/schemas")
}
ksp {
    arg("room.schemaLocation", "${projectDir}/schemas")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Rundown"
            packageVersion = "1.0.0"
            macOS {
                bundleID = "org.udnahc.rundown"
                iconFile.set(project.file("src/desktopMain/resources/MyIcon.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/composeResources/drawable/launcher.png"))
            }
            linux {
                iconFile.set(project.file("src/desktopMain/composeResources/drawable/launcher.png"))
            }
        }
    }
}

swiftklib {
    create("piechart") {
        path = file("../iosApp/iosApp/piechart")
        packageName("com.chandu")
    }
}

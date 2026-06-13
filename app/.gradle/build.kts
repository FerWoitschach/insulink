import java.io.File

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
}

val appVersionCode = gradle.extra["appVersionCode"] as Int
val appVersionName = gradle.extra["appVersionName"] as String

android {
	namespace  = "com.github.ferwoitschach.insulink"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.github.ferwoitschach.insulink"
		minSdk = 33
		targetSdk = 35
		versionCode = appVersionCode
		versionName = appVersionName
		ndk { abiFilters += listOf("arm64-v8a") }
	}

	sourceSets {
		getByName("main") {
			assets.srcDirs(listOf("../assets"))
			manifest.srcFile("../manifest.xml")
			java.setSrcDirs(listOf("../source/"))
			res.setSrcDirs(listOf("../resources/"))
			jniLibs.srcDir(file("../../include/"))
		}
	}

	buildFeatures { compose = true }
	composeOptions { kotlinCompilerExtensionVersion = "1.5.0" }
	externalNativeBuild {
		ndkBuild {
			path = file("../../scripts/android.mk")
		}
	}
	
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			signingConfig = signingConfigs.getByName("debug")
		}
	}

	@Suppress("DEPRECATION") buildDir = file("../build/")

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs += listOf(
			"-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
			"-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
		)
	}
}

dependencies {
	// implementation("androidx.navigation:navigation-compose")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.compose.foundation:foundation-layout") 
	implementation("androidx.compose.foundation:foundation")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.ui:ui-text")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("com.google.android.material:material:1.9.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
	implementation(platform("androidx.compose:compose-bom:2024.09.03"))

	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}

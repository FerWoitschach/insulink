import java.io.File
import org.tomlj.Toml
import org.tomlj.TomlResult

buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.tomlj:tomlj:1.1.1")
	}
}

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
}

var major = 1
var minor = 0
var patch = 0
var alias = ""

val applicationSettingsFile = file("../settings.toml")

val applicationSettings: TomlResult? = if (applicationSettingsFile.exists()) {
	val parsed = Toml.parse(applicationSettingsFile.toPath())

	if (parsed.hasErrors()) {
		parsed.errors().forEach {
			System.err.println(it)
		}

		throw GradleException("Parser error.")
	}

	parsed
} else {
	null
}

fun timestamp(): Int {
	val timestamp   = (System.currentTimeMillis() / 10000).toInt()
	val versionFile = file("../version")

	versionFile.writeText(timestamp.toString())

	return timestamp
}

if (applicationSettings != null) {
	major = applicationSettings.getLong("version.major")?.toInt() ?: 0
	minor = applicationSettings.getLong("version.minor")?.toInt() ?: 0
	patch = applicationSettings.getLong("version.patch")?.toInt() ?: 0
	alias = applicationSettings.getString("version.alias") ?: ""
}

val build = timestamp()
val semantic = "$major.$minor.$patch+$build"
val fullVersionName = if (alias.isNotEmpty()) "$semantic-$alias" else semantic

android {
	namespace  = "com.github.ferwoitschach.insulink"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.github.ferwoitschach.insulink"
		minSdk = 33
		targetSdk = 35
		versionCode = build
		versionName = fullVersionName
		ndk { abiFilters += listOf("arm64-v8a") }
	}

	sourceSets {
		getByName("main") {
			assets.srcDirs(listOf("../assets"))
			manifest.srcFile("../app/manifest.xml")
			java.setSrcDirs(listOf("../app/source/"))
			res.setSrcDirs(listOf("../app/resources/"))
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

	@Suppress("DEPRECATION") buildDir = file("../app/build/")

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
	companion.implementation("com.google.android.material:material:1.9.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
	implementation(platform("androidx.compose:compose-bom:2024.09.03"))
	
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}

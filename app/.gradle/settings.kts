import org.tomlj.Toml
import org.tomlj.TomlResult

pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		mavenCentral()
	}

	plugins {
		id("com.android.application")      version "8.8.0"
		id("org.jetbrains.kotlin.android") version "1.9.0"
	}
}

buildscript {
	repositories {
		google()
		mavenCentral()
	}

	dependencies {
		classpath("org.tomlj:tomlj:1.1.1")
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

	repositories {
		google()
		mavenCentral()
	}
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

major = applicationSettings?.getLong("version.major")?.toInt() ?: 0
minor = applicationSettings?.getLong("version.minor")?.toInt() ?: 0
patch = applicationSettings?.getLong("version.patch")?.toInt() ?: 0
alias = applicationSettings?.getString("version.alias") ?: ""

val build = (System.currentTimeMillis() / 10000).toInt()
val versionFile = file("../version")
versionFile.writeText(build.toString())

val semantic = "$major.$minor.$patch+$build"
val appVersionName = if (alias.isNotEmpty()) "$semantic-$alias" else semantic

gradle.extra["appVersionCode"] = build
gradle.extra["appVersionName"] = appVersionName

rootProject.name = applicationSettings?.getString("app.name") ?: "error_name"
rootProject.buildFileName = "build.kts"

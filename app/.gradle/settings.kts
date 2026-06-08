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

rootProject.name = "insulink"
rootProject.buildFileName = "build.kts"

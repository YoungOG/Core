import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    kotlin("jvm") version "2.0.0"

    `java-library`

    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1" // Generates plugin.yml based on the Gradle config
}

group = "org.ml"
version = "1.0.0-SNAPSHOT"
description = "Core gameplay plugin"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.commons.math3)
    implementation(libs.guava)

    paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    implementation("net.axay:kspigot:1.20.4")
}

tasks {
    assemble {
        dependsOn("reobfJar")
    }

    assemble {
        dependsOn(reobfJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    shadowJar
}

java {
    withSourcesJar()

    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

bukkitPluginYaml {
    main = "org.ml.core.CorePlugin"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.add("EnanderW / Young_Explicit")
    apiVersion = "1.20"
    libraries = listOf("net.axay:kspigot:1.20.4")
}
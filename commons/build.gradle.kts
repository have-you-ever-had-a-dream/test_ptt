// This is the build file to configure this module only. To configure all modules use the root level build.gradle.kts file
// More information on multi-module builds can be found at https://docs.gradle.org/current/userguide/multi_project_builds.html
plugins {
    kotlin("plugin.serialization") version "2.1.0"
    id("com.personio.pluginmanager")
}

dependencies {
    val springBootVersion = "3.4.0"
    val springBootDependencies = "org.springframework.boot:spring-boot-dependencies:$springBootVersion"

    // Platform
    implementation(platform(springBootDependencies))
    compileOnly(platform(springBootDependencies))
    implementation("org.springframework.boot:spring-boot-autoconfigure:$springBootVersion")
    compileOnly("org.springframework:spring-webflux")

    // Addressing CVE-2024-1597
    implementation("org.yaml:snakeyaml:2.3")

    // Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

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

    // Use Jakarta for intercepting Servlet requests with Spring Boot 3.+; conditional, hence compileOnly
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    // For working with Spring WebFlux; conditional, hence compileOnly
    compileOnly("org.springframework:spring-webflux")

    // Serialization
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // Addressing CVE-2024-1597
    implementation("org.yaml:snakeyaml:2.3")

    // JWT Dependencies
    implementation("com.auth0:java-jwt:4.5.0")

    api(projects.commons)

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-websocket")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webmvc")
    testImplementation("org.springframework:spring-webflux")
}

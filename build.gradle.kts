val jacocoVersion = "0.8.8"

plugins {
    java
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.dokka") version "1.9.20"
    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
    id("org.sonarqube") version "5.1.0.4882"
    id("com.personio.pluginmanager") version "4.6.0"
}

val globalOptIns = listOf("kotlin.RequiresOptIn")

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.sonarqube")
    apply(plugin = "jacoco")

    group = "com.personio.integrations.papi-gateway"
    version = project.properties["versionOverride"]
        ?.toString()
        ?.removePrefix("v") // v1.0 becomes 1.0
        ?: "local"
    repositories { maven("https://nexus.tools.personio-internal.de/repository/maven/") }

    ktlint {
        filter {
            exclude { "generated" in it.file.path }
        }
    }

    jacoco {
        toolVersion = jacocoVersion
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm") // kotlin("jvm")

    tasks {
        withType<Test> {
            systemProperty("gradle.build.dir", buildDir)
            useJUnitPlatform()
            testLogging {
                showExceptions = false
                showCauses = true
                showStackTraces = true
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            }
        }
    }

    dependencies {
        val kotest = "5.9.1"
        val junit = "5.11.4"
        val mockkVersion = "1.13.16"

        implementation(kotlin("stdlib"))

        // Logging
        implementation("org.slf4j:slf4j-api:2.0.16")
        implementation("net.logstash.logback:logstash-logback-encoder:8.0")

        testImplementation("io.kotest:kotest-assertions-core:$kotest")
        testImplementation("io.kotest:kotest-runner-junit5:$kotest")
        testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")

        testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")

        testImplementation("io.mockk:mockk:$mockkVersion")
    }

    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.dokka")

    val sourcesJar by tasks.creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by tasks.creating(Jar::class) {
        dependsOn("dokkaJavadoc")
        archiveClassifier.set("javadoc")
        from(File(buildDir, "javadoc"))
    }

    extensions.configure<PublishingExtension>("publishing") {
        fun isRelease() = project.hasProperty("release")

        publications {
            register("maven", MavenPublication::class) {
                from(components["kotlin"])
                artifact(sourcesJar)
                artifact(javadocJar)
                artifactId = project.name
                version = project.version.toString() + if (isRelease()) "" else "-SNAPSHOT"
            }
            repositories {
                maven {
                    name = "nexus-releases"
                    val baseUrl = "https://nexus.tools.personio-internal.de/repository/"
                    val repositoryName = if (isRelease()) "maven-releases/" else "maven-snapshots/"
                    url = uri(baseUrl + repositoryName)
                    credentials {
                        username = System.getenv("NEXUS_USER").orEmpty()
                        password = System.getenv("NEXUS_PASSWORD").orEmpty()
                    }
                }
            }
        }
    }
}

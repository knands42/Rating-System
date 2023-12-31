import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logback_version: String by project
val ktor_version: String by project
val kafka_stream_version: String by project
val kafka_test_container: String by project
val kotlin_version: String by project
val confluent_version: String by project
val ak_version: String by project
val h2_version: String by project
val postgres_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("io.ktor.plugin") version "2.3.3"
}

group = "org.eventdriven"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val currentEnvironment: String = System.getProperty("KTOR_ENV") ?: "local"
    applicationDefaultJvmArgs = listOf("-Dio.ktor.env=$currentEnvironment")
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven")
    maven("https://kotlin.bintray.com/ktor")
    maven("https://jitpack.io")
}

dependencies {
    // kafka
    implementation("org.apache.kafka:kafka-clients:$kafka_stream_version")
    implementation("org.apache.kafka:kafka-streams:$kafka_stream_version")
    implementation("io.confluent:kafka-json-schema-serializer:$confluent_version")
    implementation("io.confluent:kafka-streams-json-schema-serde:$confluent_version") {
        exclude("org.apache.kafka", "kafka-clients")
    }
    // Database
    implementation("com.h2database:h2:$h2_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    // Log
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers:$ktor_version")
    implementation("io.ktor:ktor-server-html-builder:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    // Testing
    testImplementation("org.apache.kafka:kafka-streams-test-utils:$ak_version")
    testImplementation("org.testcontainers:kafka:$kafka_test_container")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation(kotlin("test"))
}


tasks.withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}
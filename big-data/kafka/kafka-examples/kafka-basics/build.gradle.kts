plugins {
    kotlin("jvm") version "1.3.70"
}

group = "com.github.arpan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.3.4")
    implementation(group = "org.apache.kafka", name = "kafka-clients", version = "2.4.1")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.30")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
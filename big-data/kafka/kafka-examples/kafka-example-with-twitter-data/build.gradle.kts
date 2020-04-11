plugins {
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.3.4")
    implementation(group = "org.apache.kafka", name = "kafka-clients", version = "2.4.1")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.30")
    implementation(group = "com.twitter", name = "hbc-core", version = "2.2.0")
    implementation(group = "org.elasticsearch.client", name = "elasticsearch-rest-high-level-client", version = "7.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0") // JVM dependency
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
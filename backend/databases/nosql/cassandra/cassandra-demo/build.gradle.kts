plugins {
    kotlin("jvm") version "1.3.72"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.30")
    implementation(group = "com.datastax.oss", name = "java-driver-core", version = "4.5.1")
//    implementation(group = "com.datastax.oss", name = "java-driver-query-builder", version = "4.5.1")
//    implementation(group = "com.datastax.oss", name = "java-driver-mapper-processor", version = "4.5.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
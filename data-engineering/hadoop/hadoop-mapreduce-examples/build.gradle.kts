plugins {
    kotlin("jvm") version "1.4.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.slf4j", "slf4j-simple", "1.7.30")
    implementation("org.apache.hadoop", "hadoop-mapreduce-client-core", "3.3.0")
    implementation("org.apache.hadoop", "hadoop-common", "3.3.0")
    // compile group: 'org.apache.hadoop', name: 'hadoop-common', version: '3.3.0'
}

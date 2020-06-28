plugins {
    java
}

group = "com.github.arpan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // testCompile group: 'org.slf4j', name: 'slf4j-simple', version: '1.7.30'
    implementation("org.slf4j", "slf4j-simple", "1.7.30")
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.6.2")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.6.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
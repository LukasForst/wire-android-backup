plugins {
    java
    kotlin("jvm") version "1.3.72"
}

group = "com.wire.integrations.backups"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // libsodium for decryption
    implementation("com.github.joshjdevl.libsodiumjni", "libsodium-jni", "2.0.2")
    // unzip
    implementation("net.lingala.zip4j", "zip4j", "2.6.1")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    val junitVersion = "5.6.2"
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", junitVersion)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        systemProperties["java.library.path"] = "${projectDir}/libs"
    }

    test {
        useJUnitPlatform()
    }
}

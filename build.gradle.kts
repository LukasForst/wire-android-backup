import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    java
    kotlin("jvm") version "1.3.72"
    `maven-publish`
    id("net.nemerosa.versioning") version "2.8.2"
    id("com.jfrog.bintray") version "1.8.4"

}

group = "pw.forst.wire"
version = versioning.info.lastTag


repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("ai.blindspot.ktoolz", "ktoolz", "1.0.6")
    implementation("com.fasterxml.jackson.core", "jackson-core", "2.11.1")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.11.1")
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", "2.11.1")

    // libsodium for decryption
    implementation("com.github.joshjdevl.libsodiumjni", "libsodium-jni", "2.0.2")
    // unzip
    implementation("net.lingala.zip4j", "zip4j", "2.6.1")

    // logging
    implementation("io.github.microutils", "kotlin-logging", "1.7.9")
    implementation("ch.qos.logback", "logback-classic", "1.2.3")

    implementation("org.xerial", "sqlite-jdbc", "3.32.3")
    val exposedVersion = "0.26.1"
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-java-time", exposedVersion)


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

// ------------------------------------ Deployment Configuration  ------------------------------------
val githubRepository = "LukasForst/wire-backup-export"
val descriptionForPackage = "Library for decryption of Wire iOS and Android backups"
val tags = arrayOf("kotlin", "Wire")
// everything bellow is set automatically

// deployment configuration - deploy with sources and documentation
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

// name the publication as it is referenced
val publication = "default-gradle-publication"
publishing {
    // create jar with sources and with javadoc
    publications {
        register(publication, MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }

    // publish package to the github packages
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$githubRepository")
            credentials {
                username = project.findProperty("gpr.user") as String?
                    ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String?
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// upload to bintray
bintray {
    // env variables loaded from pipeline for publish
    user = project.findProperty("bintray.user") as String?
        ?: System.getenv("BINTRAY_USER")
    key = project.findProperty("bintray.key") as String?
        ?: System.getenv("BINTRAY_TOKEN")
    publish = true
    setPublications(publication)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        // my repository for maven packages
        repo = "jvm-packages"
        name = project.name
        // my user account at bintray
        userOrg = "lukas-forst"
        websiteUrl = "https://forst.pw"
        githubRepo = githubRepository
        vcsUrl = "https://github.com/$githubRepository"
        description = descriptionForPackage
        setLabels(*tags)
        setLicenses("MIT")
        desc = description
    })
}

import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    `maven-publish`
}

group = "net.zomis"
val myVersion = "0.3.0-SNAPSHOT"
version = myVersion

repositories {
    jcenter()
    maven(url = "http://stats.zomis.net/maven/snapshots")
}

dependencies {
    compile(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
    compile("org.slf4j:slf4j-api:1.7.25")
    compile("net.zomis:scorers:0.3.0-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

publishing {
    repositories {
        maven {
            val rootPath = "/var/www/html/maven"
            val releasesRepoUrl = "$rootPath/releases"
            val snapshotsRepoUrl = "$rootPath/snapshots"
            url = if (myVersion.endsWith("SNAPSHOT")) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
        }
    }
}
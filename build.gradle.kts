import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.postgresql:postgresql:42.2.5")
    }
}

plugins {
    kotlin("plugin.jpa") version "1.2.71"
    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71"
    id("org.flywaydb.flyway") version "6.0.0-beta"
}

group = "me.danny"
version = "0.0.4-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}
//
//
//tasks {
//    withType<Jar> {
//        enabled = true
//        doFirst {
//            manifest {
//                attributes["Main-Class"] = "me.danny.instacrawlerkotlin.ApplicationKt"
//            }
//
//            exclude("META-INF/*", "META-INF/*.SF", "META-INF/*.DSA")
//
//            from({
//                configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
//            })
//        }
//    }
//
//}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.h2database:h2")
    implementation("org.postgresql:postgresql:42.2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    implementation("org.jsoup:jsoup:1.12.1")
    implementation("net.logstash.logback:logstash-logback-encoder:+")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

flyway {
    driver = "org.postgresql.Driver"
    url = "jdbc:postgresql://dev-crawling-db.gelato.im:5432/instagram_crawling_db"
    user = "gelato"
    password = "wpffkEh16)&"
    schemas = arrayOf("public")
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

springBoot {
    mainClassName = "me.danny.instacrawlerkotlin.ApplicationKt"
}

tasks.getByName<BootJar>("bootJar") {
    mainClassName = "me.danny.instacrawlerkotlin.ApplicationKt"
}


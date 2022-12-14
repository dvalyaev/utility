import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.0-RC"
    kotlin("plugin.spring") version "1.8.0-RC"
}

group = "utility"
version = "1.0-SNAPSHOT"
// java {
//     sourceCompatibility = JavaVersion.VERSION_17
//     targetCompatibility = JavaVersion.VERSION_17
// }

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc:9.5.0.jre17-preview")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
//        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

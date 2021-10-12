import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
}

group = "com.study"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

fun String.toFile() = file(this)

val snippetsDir = "build/generated-snippets" // spring-rest-docs snippets(adoc) output
val docsResourcesDir = "src/main/resources/static/docs" // rest-docs html5 resource
val asciidocDir = "build/docs/asciidoc" // asciidoc output dir (adoc to html5)

object Versions {
    const val KOTEST = "4.6.3" // kotest version
    const val KOTEST_SPRING = "1.0.1" // kotest-extensions-spring version
    const val REST_DOCS_MOCKMVC = "2.0.5.RELEASE"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }

    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:${Versions.REST_DOCS_MOCKMVC}")

    testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
    testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:${Versions.KOTEST_SPRING}")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> { // Test Task에 대한 공통 설정
    useJUnitPlatform()
}

tasks.test { // test Task에 대한 설정
    outputs.dir(snippetsDir.toFile())
}

tasks.clean {
    delete(docsResourcesDir.toFile())
}

tasks.asciidoctor { // asciidoctor Task에 대한 설정 수정
    outputs.dir(snippetsDir.toFile())
    dependsOn(tasks.test)

    doFirst {
        delete(docsResourcesDir.toFile())
    }
}

tasks.register<Copy>("copyHTML") {
    description = "Copy asciidoc html5 file to static resources"
    dependsOn(tasks.asciidoctor)

    from(asciidocDir.toFile())
    into(docsResourcesDir.toFile())
}

tasks.build {
    dependsOn(tasks.getByName("copyHTML"))
}

tasks.bootJar {
    dependsOn(tasks.asciidoctor, tasks.getByName("copyHTML"))
}

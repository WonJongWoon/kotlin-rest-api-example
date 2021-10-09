import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

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
java.sourceCompatibility = JavaVersion.VERSION_11


val snippetsDir = file("build/generated-snippets")

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
		exclude("junit", "junit")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	//asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(snippetsDir)
}

tasks.asciidoctor {
	inputs.dir(snippetsDir)
	dependsOn(tasks.test)
}

tasks.asciidoctor {
	doFirst {
		println("Start Asciidoctor")
		delete(file("src/main/resources/static/docs"))
	}

	doLast {
		println("Finish Asciidoctor")
	}
}

tasks.register("copyHTML", Copy::class) {
	dependsOn(tasks.getByName("asciidoctor"))
	from(file("build/docs/asciidoc"))
	into(file("src/main/resources/static/docs"))
}

tasks.build {
	dependsOn(tasks.getByName("copyHTML"))
}

tasks.bootJar {
	dependsOn(tasks.asciidoctor)
	dependsOn(tasks.getByName("copyHTML"))
}
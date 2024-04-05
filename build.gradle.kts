import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Base64

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
}

group = "com.h5templates"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation ("org.mariadb.jdbc:mariadb-java-client:2.7.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.10.4")
//	testImplementation("io.mockk:mockk-spring:1.12.0")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("io.jsonwebtoken:jjwt-api:0.11.2")
		classpath("io.jsonwebtoken:jjwt-impl:0.11.2")
		classpath("io.jsonwebtoken:jjwt-jackson:0.11.2")
	}
}

tasks.register("generateJwtKey") {
	doLast {
		val key = Keys.secretKeyFor(SignatureAlgorithm.HS256) // Generate the key
		val encodedKey = Base64.getEncoder().encodeToString(key.encoded) // Encode key as string
		println("Secure key for HS256: $encodedKey")
	}
}

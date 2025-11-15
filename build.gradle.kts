plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("kapt") version "1.9.25"
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.quddaz"
version = "0.0.1-SNAPSHOT"
description = "Stock Simulation Game with Spring Boot & WebSocket"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Core Spring
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	// WebSocket + STOMP
	implementation("org.springframework.boot:spring-boot-starter-websocket")

	// OAuth2
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Database
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("com.h2database:h2")

	// QueryDSL
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

	// Swagger / OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	// Actuator + Prometheus
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Mockito Core
	testImplementation("org.mockito:mockito-core:5.6.0")

	// Mockito Kotlin 확장
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")

	// Cache
	implementation("org.springframework.boot:spring-boot-starter-cache")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// QueryDSL QClass 생성 경로 설정
val generatedDir = layout.buildDirectory.dir("generated/querydsl")

// Kotlin 코드 생성 디렉터리 지정
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

// QClass 파일 생성 위치 지정
tasks.withType<JavaCompile> {
	options.generatedSourceOutputDirectory.set(generatedDir.get().asFile)
}

// sourceSets에 QClass 경로 추가
sourceSets {
	named("main") {
		java.srcDirs(generatedDir)
	}
}

// gradle clean 시 생성된 QClass 제거
tasks.named<Delete>("clean") {
	delete(generatedDir)
}
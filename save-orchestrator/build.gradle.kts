import org.cqfn.save.buildutils.configureJacoco
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.cqfn.save.buildutils.configureSpringBoot

plugins {
    kotlin("jvm")
}

configureSpringBoot()

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = Versions.jdk
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":save-common"))
    implementation("org.liquibase:liquibase-core:${Versions.liquibase}")
    implementation("org.hibernate:hibernate-core:${Versions.hibernate}")
    implementation("org.slf4j:slf4j-api:${Versions.slf4j}")
    implementation("ch.qos.logback:logback-core:${Versions.logback}")
    implementation("com.github.docker-java:docker-java-core:${Versions.dockerJavaApi}")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:${Versions.dockerJavaApi}")
    implementation("org.apache.commons:commons-compress:1.20")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${Versions.springBoot}")
}

configureJacoco()

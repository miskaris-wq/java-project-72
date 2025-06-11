plugins {
    application
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.sonarqube") version "4.4.1.3373"
    id("io.freefair.lombok") version "8.6"
    id("checkstyle")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    // Основные зависимости
    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("gg.jte:jte:3.1.9")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    // JDBC, БД
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.7.3")

    // Веб
    implementation("org.eclipse.jetty:jetty-servlet:11.0.15")

    // Тестирование
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.javalin:javalin-testtools:6.1.3")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}


tasks {
    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes(mapOf("Main-Class" to "hexlet.code.App"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
    }
}

tasks.withType<JavaCompile> {
    options.release = 21
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
}
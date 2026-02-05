plugins {
    id("application")
    id("checkstyle")
    id("jacoco")
    id("org.sonarqube") version "7.0.1.6134"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation(platform("com.konghq:unirest-java-bom:4.5.1"))
    implementation("com.konghq:unirest-java-core")
    implementation("com.konghq:unirest-modules-gson")

    implementation("org.jsoup:jsoup:1.17.2")

    implementation("org.postgresql:postgresql:42.7.8")
    implementation("com.h2database:h2:2.4.240")
    implementation("com.zaxxer:HikariCP:7.0.2")

    implementation("org.slf4j:slf4j-simple:2.0.17")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")

    implementation("gg.jte:jte:3.2.0")

    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-rendering:6.6.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.javalin:javalin-testtools:6.7.0")
    testImplementation("org.assertj:assertj-core:3.26.0")
    testImplementation("com.h2database:h2:2.3.232")
    testImplementation("com.zaxxer:HikariCP:5.1.0")
}

checkstyle {
    toolVersion = "10.12.4"
    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
}

sonar {
    properties {
        property("sonar.projectKey", "pavelchervonenko_java-project-72")
        property("sonar.organization", "pavelchervonenko")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath
        )
    }
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.test {
    useJUnitPlatform()
}
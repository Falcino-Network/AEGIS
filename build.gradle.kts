plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "atlanteshellsing.aegis"
version = "1.0"

repositories {
    mavenCentral()
}

val javafxVersion = "24.0.2"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.openjfx:javafx-controls:${javafxVersion}")
    implementation("org.openjfx:javafx-fxml:${javafxVersion}")
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls", "javafx.fxml")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("atlanteshellsing.aegis.AEGISMainApplication")
}

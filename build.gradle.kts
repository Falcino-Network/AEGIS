import java.io.BufferedInputStream

plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("jacoco")
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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

jacoco {
    toolVersion = "0.8.13"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests must run first

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }

    // Handle both Java and Kotlin outputs
    val classFiles = fileTree(layout.buildDirectory.dir("classes")) {
        include("**/*.class")
    }
    // Bytecode annotation descriptor (binary name)
    val excludeAnnotation = "Latlanteshellsing/aegis/assetations/ExcludeAsGenerated;"
    // Efficiently skip annotated class files
    val filteredClasses = classFiles.matching {
        exclude { it ->
            val file = it.file
            if (!file.isFile || !file.name.endsWith(".class")) return@exclude false
            // Read just enough bytes to detect the annotation
            BufferedInputStream(file.inputStream()).use { input ->
                val buffer = ByteArray(4096) // 4 KB buffer
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    val text = String(buffer, 0, bytesRead, Charsets.ISO_8859_1)
                    if (text.contains(excludeAnnotation)) return@exclude true
                }
            }
            false
        }
    }
    classDirectories.setFrom(files(filteredClasses))
}

application {
    mainClass.set("atlanteshellsing.aegis.AEGISMainApplication")
}

plugins {
    kotlin("js")
}

kotlin {
    js(LEGACY) {
        // as for `-pre.148-kotlin-1.4.21`, react-table gives errors with IR
        browser {
            repositories {
                mavenCentral()
                maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers/")
            }
        }
        binaries.executable()  // already default for LEGACY, but explicitly needed for IR
        sourceSets.all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }
        sourceSets["main"].dependencies {
            implementation(project(":save-cloud-common"))

            // devDependencies for webpack. As for kotlin 1.4.31, kotlin bundles webpack 4.x, and some
            // latest ersions of dependencies already require webpack ^5. These versions are fixed on the last compatible.
            compileOnly(devNpm("node-sass", "5.0.0"))
            compileOnly(devNpm("sass-loader", "10.1.1"))
            compileOnly(devNpm("style-loader", "*"))
            compileOnly(devNpm("css-loader", "*"))
            compileOnly(devNpm("url-loader", "*"))
            compileOnly(devNpm("file-loader", "*"))
            // these dependenceies are bound to postcss 7.x instead of 8.x, because bootstrap 4.x guide uses them
            compileOnly(devNpm("postcss-loader", "3.*"))
            compileOnly(devNpm("postcss", "7.*"))
            compileOnly(devNpm("autoprefixer", "9.*"))
            compileOnly(devNpm("webpack-bundle-analyzer", "*"))

            // web-specific dependencies
            implementation(npm("@fortawesome/fontawesome-svg-core", "1.2.35"))
            implementation(npm("@fortawesome/free-solid-svg-icons", "5.15.3"))
            implementation(npm("@fortawesome/react-fontawesome", "0.1.14"))
            implementation("org.jetbrains:kotlin-react:${Versions.kotlinReact}")
            implementation("org.jetbrains:kotlin-react-dom:${Versions.kotlinReact}")
            implementation("org.jetbrains:kotlin-react-router-dom:5.2.0${Versions.kotlinJsWrappersSuffix}")
            implementation("org.jetbrains:kotlin-react-table:7.7.0${Versions.kotlinJsWrappersSuffix}")
            implementation(npm("jquery", "3.5.1"))
            implementation(npm("popper.js", "1.16.1"))  // peer dependency for bootstrap
            implementation(npm("bootstrap", "4.6.0"))
            implementation(npm("react", Versions.react))
            implementation(npm("react-dom", Versions.react))
            implementation(npm("react-modal", "3.12.1"))

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDatetime}")
        }
        sourceSets["test"].dependencies {
            implementation(kotlin("test-js"))
        }
    }
}

// generate kotlin file with project version to include in web page
val generateVersionFileTaskProvider = tasks.register("generateVersionFile") {
    val versionsFile = File("$buildDir/generated/src/generated/Versions.kt")

    inputs.property("project version", version.toString())
    outputs.file(versionsFile)

    doFirst {
        versionsFile.parentFile.mkdirs()
        versionsFile.writeText(
            """
            package generated

            internal const val SAVE_VERSION = "$version"

            """.trimIndent()
        )
    }
}
val generatedKotlinSrc = kotlin.sourceSets.create("jsGenerated") {
    kotlin.srcDir("$buildDir/generated/src")
}
kotlin.sourceSets.getByName("main").dependsOn(generatedKotlinSrc)
tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().forEach {
    it.dependsOn(generateVersionFileTaskProvider)
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack>().forEach { kotlinWebpack ->
    kotlinWebpack.doFirst {
        val additionalWebpackResources = fileTree("$buildDir/processedResources/js/main/") {
            include("scss/**")
        }
        copy {
            from(additionalWebpackResources)
            into("${rootProject.buildDir}/js/packages/${rootProject.name}-${project.name}")
        }
    }
}

val distribution: Configuration by configurations.creating
val distributionJarTask by tasks.registering(Jar::class) {
    dependsOn(":save-frontend:browserDistribution")
    archiveClassifier.set("distribution")
    from("$buildDir/distributions")
    into("static")
    exclude("scss")
}
artifacts.add(distribution.name, distributionJarTask.get().archiveFile) {
    builtBy(distributionJarTask)
}

detekt {
    config.setFrom(config.plus(file("detekt.yml")))
}

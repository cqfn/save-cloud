import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem

plugins {
    kotlin("multiplatform")
}

kotlin {
    val os = getCurrentOperatingSystem()
    // Create a target for the host platform.
    val hostTarget = when {
        os.isLinux -> linuxX64()
        os.isWindows -> mingwX64()  // you'll need to install msys2 and run `pacman -S mingw-w64-x86_64-curl` to have libcurl for ktor-client
        else -> throw GradleException("Host OS '${os.name}' is not supported in Kotlin/Native $project.")
    }

    configure(listOf(hostTarget)) {
        binaries.executable {
            entryPoint = "org.cqfn.save.agent.main"
        }
    }
    sourceSets {
        val nativeMain by creating {
            dependencies {
                implementation(project(":save-common"))
                implementation("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.ktor:ktor-client-curl:${Versions.ktor}")
                implementation("io.ktor:ktor-client-serialization:${Versions.ktor}")
                implementation("com.benasher44:uuid:0.2.3")
                // as for 2.0.2, kotlin-logging doesn't have mingw version and it'll be PITA to use it
//                implementation("io.github.microutils:kotlin-logging-${hostTarget.name}:2.0.2")
            }
        }
        getByName("${hostTarget.name}Main").dependsOn(nativeMain)
        val nativeTest by creating {
            dependencies {
                implementation("io.ktor:ktor-client-mock:${Versions.ktor}")
            }
        }
        getByName("${hostTarget.name}Test").dependsOn(nativeTest)
    }

    // code coverage: https://github.com/JetBrains/kotlin-native/blob/master/CODE_COVERAGE.md, https://github.com/JetBrains/kotlin-native/blob/master/samples/coverage/build.gradle.kts
    if (false /*os.isLinux*/) {  // this doesn't work for 1.4.21, maybe will be fixed later
        hostTarget.binaries.getTest("DEBUG").apply {
            freeCompilerArgs = freeCompilerArgs + listOf("-Xlibrary-to-cover=${hostTarget.compilations["main"].output.classesDirs.singleFile.absolutePath}")
        }
        val createCoverageReportTask by tasks.creating {
            dependsOn("${hostTarget.name}Test")
            description = "Create coverage report"

            doLast {
                val testDebugBinary = hostTarget.binaries.getTest("DEBUG").outputFile
                val llvmPath = "${System.getenv()["HOME"]}/.konan/dependencies/clang-llvm-8.0.0-linux-x86-64/bin"
                exec {
                    commandLine("$llvmPath/llvm-profdata", "merge", "$testDebugBinary.profraw", "-o", "$testDebugBinary.profdata")
                }
                exec {
                    commandLine("$llvmPath/llvm-cov", "show", "$testDebugBinary", "-instr-profile", "$testDebugBinary.profdata")
                }
            }
        }
        tasks.getByName("${hostTarget.name}Test").finalizedBy(createCoverageReportTask)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinTest> {
    testLogging.showStandardStreams = true
}
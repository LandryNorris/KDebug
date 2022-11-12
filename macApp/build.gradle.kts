plugins {
    kotlin("multiplatform")
}

kotlin {
    val macTargets = listOf(macosArm64())

    macTargets.forEach {
        it.binaries {
            executable("memorytest")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":memory-reader"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val macosArm64Main by getting {
            dependsOn(commonMain)
        }
    }
}
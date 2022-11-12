plugins {
    kotlin("multiplatform")
}

kotlin {
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
    val macTargets = listOf(macosArm64(), macosX64())
    val linuxTargets = listOf(linuxX64(), linuxArm64())
    val androidTargets = listOf(androidNativeArm32(), androidNativeArm64(),
        androidNativeX86(), androidNativeX64())

    (iosTargets + macTargets).forEach {
        val main by it.compilations.getting
        val memory_darwin by main.cinterops.creating
        val objc by main.cinterops.creating
        val kotlin by main.cinterops.creating
    }

    (linuxTargets + androidTargets).forEach {
        val main by it.compilations.getting
        val memory by main.cinterops.creating
        val kotlin by main.cinterops.creating
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val darwinMain by creating {
            dependsOn(commonMain)
        }
        val darwinTest by creating {
            dependsOn(commonTest)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(darwinMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(darwinTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        val macosArm64Main by getting
        val macosX64Main by getting
        val macosMain by creating {
            dependsOn(darwinMain)
            macosArm64Main.dependsOn(this)
            macosX64Main.dependsOn(this)
        }
    }
}
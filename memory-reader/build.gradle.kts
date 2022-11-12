plugins {
    kotlin("multiplatform")
}

kotlin {
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
    val macTargets = listOf(macosArm64(), macosX64())

    iosTargets.forEach {
        it.binaries {
            framework()
        }
        val main by it.compilations.getting
        val memory by main.cinterops.creating
        val objc by main.cinterops.creating
        val kotlin by main.cinterops.creating
        val vm by main.cinterops.creating
    }

    macTargets.forEach {
        it.binaries {
            framework()
        }
        val main by it.compilations.getting
        val memory by main.cinterops.creating
        val objc by main.cinterops.creating
        val kotlin by main.cinterops.creating
        val vm by main.cinterops.creating
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}
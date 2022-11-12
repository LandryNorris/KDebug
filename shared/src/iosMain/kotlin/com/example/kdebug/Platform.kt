package com.example.kdebug

import io.github.landrynorris.memory.reader.MemoryReader
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform().also {
    MemoryReader().getZones()
}
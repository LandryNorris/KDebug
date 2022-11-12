package com.example.kdebug

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
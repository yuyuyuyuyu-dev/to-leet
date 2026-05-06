package dev.yuyuyuyuyu.toleet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
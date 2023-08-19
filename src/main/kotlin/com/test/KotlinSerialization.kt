package com.test

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import kotlinx.serialization.hocon.encodeToConfig
import kotlinx.serialization.json.Json

@Serializable
data class Project(@SerialName("na") val name: String? = null, val language: String)


fun main() {
    val p = Project("leo", "2")
    val json = Json.encodeToString(p)
    println(json)
    val pO = Json.decodeFromString<Project>(json)
    println(pO)

    val hacon = Hocon.encodeToConfig(p)
    println(hacon)
    val pHocon = Hocon.decodeFromConfig<Project>(hacon)
    println(pHocon)

    val jsonStr = "{\"language\":\"2\"}\n"
    val pOO = Json.decodeFromString<Project>(jsonStr)
    println(pOO)
}
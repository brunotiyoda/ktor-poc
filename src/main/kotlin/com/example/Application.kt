package com.example

import com.example.routes.registerCustomerRoutes
import com.example.routes.registerOrderRoutes
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

//    val connect = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

}

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) { json() }
    registerCustomerRoutes()
    registerOrderRoutes()
}
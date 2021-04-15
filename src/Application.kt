package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.text.DateFormat

// fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        install(CORS) {
            header(HttpHeaders.AccessControlAllowOrigin)
        }
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        routing {
            post("/calc") {
                val input = call.receive<ChemInput>()
                val output = Solver.solve(
                    input.n,
                    input.a, input.b, input.c, input.d,
                    input.h, input.s,
                    input.t
                )
                call.respond(output)
            }
            get {
                call.respondText("123")
            }
        }
    }.start(wait = true)

}

/*fun Application.module() {
    install(CORS) {
        header(HttpHeaders.AccessControlAllowOrigin)
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    routing {
        post("/calc") {
            val input = call.receive<ChemInput>()
            val output = Solver.solve(
                input.n,
                input.a, input.b, input.c, input.d,
                input.h, input.s,
                input.t
            )
            call.respond(output)
        }
        get {
            call.respondText("123")
        }
    }
}*/

data class ChemInput(
    val n: List<List<Double>>,
    val a: List<List<Double>>,
    val b: List<List<Double>>,
    val c: List<List<Double>>,
    val d: List<List<Double>>,
    val h: List<List<Double>>,
    val s: List<List<Double>>,
    val t: Double
)


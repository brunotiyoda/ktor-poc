package com.example.routes

import com.example.models.Customer
import com.example.repositories.Customers
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.customerRouting() {

    /*Database.connect(
        url = "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;",
        driver = "org.h2.Driver"
    )*/

    route("/customers") {
        get {
            /*if (Customers.selectAll().none()) {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }*/

            val allCustomers = transaction {
                Customers.selectAll().map { Customers.toCustomer(it) }
            }
            call.respond(allCustomers)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val customer = transaction {
                Customers.select { Customers.id eq id.toInt() }.map {
                    Customers.toCustomer(it)
                }
            }

            if (customer.isEmpty()) {
                call.respondText(text = "No customer with id $id", status = HttpStatusCode.NotFound)
            }

            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()

            val nameOfNewCustomer = transaction {
                Customers.insert {
                    it[firstName] = customer.firstName
                    it[lastName] = customer.lastName
                    it[email] = customer.email
                }
            }[Customers.firstName]

            call.respondText("Customer stored correctly $nameOfNewCustomer", status = HttpStatusCode.Created)
        }

        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            transaction {
                Customers.deleteWhere { Customers.id eq id.toInt() }
            }

            call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
        }
    }
}


fun Application.registerCustomerRoutes() {
    routing {
        customerRouting()
    }
}

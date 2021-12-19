package com.example.repositories

import com.example.configs.H2DatabaseInit
import com.example.models.Customer
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object Customers : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val firstName: Column<String> = varchar("firstName", 255)
    val lastName: Column<String> = varchar("lastName", 155)
    val email: Column<String> = varchar("email", 155)

    override val primaryKey = PrimaryKey(id, name = "PK_CUSTOMER_ID")

    fun toCustomer(row: ResultRow) = Customer(
        id = row[id],
        firstName = row[firstName],
        lastName = row[lastName],
        email = row[email]
    )

    private fun createCustomers() = transaction(H2DatabaseInit.connectWithH2()) {
        SchemaUtils.create(Customers)

        Customers.insert {
            it[firstName] = "Bruno Yudi"
            it[lastName] = "Tiyoda"
            it[email] = "proton@email.com"
        }
    }
}
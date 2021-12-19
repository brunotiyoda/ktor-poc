package com.example.configs

import org.jetbrains.exposed.sql.Database

class H2DatabaseInit {

    companion object {
        fun connectWithH2() =
            Database.connect(
                url = "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;",
                driver = "org.h2.Driver"
            )
    }
}
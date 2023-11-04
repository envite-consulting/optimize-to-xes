package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.token

internal class TokenRequest(
    val client_id: String,
    val client_secret: String,
    val audience: String = "optimize.camunda.io",
    val grant_type: String = "client_credentials"
)

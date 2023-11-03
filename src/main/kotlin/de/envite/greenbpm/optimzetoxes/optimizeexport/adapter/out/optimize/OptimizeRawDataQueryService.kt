package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize

import de.envite.greenbpm.optimzetoxes.Logging
import de.envite.greenbpm.optimzetoxes.log
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.out.RawDataQuery
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
internal class OptimizeRawDataQueryService(
    @Qualifier("optimize-client")
    private val webClientOptimize: WebClient,
    @Qualifier("login-client")
    private val webClientLogin: WebClient,
    private val optimizeClientProperties: OptimizeClientProperties
): RawDataQuery, Logging {

    @Throws(DataQueryException::class)
    private fun queryToken(): TokenResponse =
        webClientLogin
            .post()
            .body(BodyInserters.fromValue(TokenRequest(optimizeClientProperties.clientId!!, optimizeClientProperties.clientSecret!!)))
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _ ->
                Mono.error { throw DataQueryException("Could not fetch Bearer Token from optimize") }
            }
            .bodyToMono(TokenResponse::class.java)
            .blockOptional()
            .orElseThrow { DataQueryException("Could not parse Bearer Token from optimize") }

    @Throws(DataQueryException::class)
    override fun queryData(): OptimizeData {
        val token = queryToken()

        val uri = "/api/public/export/report/${optimizeClientProperties.reportId}/result/json?paginationTimeout=60&limit=2"

        log().info("Fetch Camunda Optimize Raw Data Export from URI {}'", uri)

        return webClientOptimize
            .get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ${token.access_token}")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _ ->
                Mono.error { throw DataQueryException("Could not fetch data from optimize") }
            }
            .bodyToMono(OptimizeData::class.java)
            .blockOptional()
            .orElseThrow { DataQueryException("Could not parse data from optimize") }
    }
}

private class TokenResponse (
    var access_token: String? = null
)

private data class TokenRequest(
    val client_id: String,
    val client_secret: String,
    val audience: String = "optimize.camunda.io",
    val grant_type: String = "client_credentials"
)
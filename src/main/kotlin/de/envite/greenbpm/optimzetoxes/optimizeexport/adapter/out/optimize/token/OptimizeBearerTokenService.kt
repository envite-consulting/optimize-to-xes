package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.token

import de.envite.greenbpm.optimzetoxes.Logging
import de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.DataQueryException
import de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.OptimizeClientProperties
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


@Service
@EnableConfigurationProperties(OptimizeClientProperties::class)
@RegisterReflectionForBinding(TokenRequest::class, TokenResponse::class)
internal class OptimizeBearerTokenService(
    private val optimizeClientProperties: OptimizeClientProperties
) : Logging {

    @Throws(DataQueryException::class)
    fun queryToken(): String? = WebClient.builder()
        .baseUrl(optimizeClientProperties.tokenBaseUrl!!)
        .build()
        .post()
        .body(
            BodyInserters.fromValue(
                TokenRequest(
                    optimizeClientProperties.clientId!!,
                    optimizeClientProperties.clientSecret!!
                )
            )
        )
        .retrieve()
        .onStatus(HttpStatusCode::isError) { _ ->
            Mono.error { throw DataQueryException("Could not fetch Bearer Token from optimize") }
        }
        .bodyToMono(TokenResponse::class.java)
        .map { it.access_token }
        .blockOptional()
        .orElseThrow { DataQueryException("Could not parse Bearer Token from optimize") }
}
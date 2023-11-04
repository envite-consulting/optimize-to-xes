package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize

import de.envite.greenbpm.optimzetoxes.Logging
import de.envite.greenbpm.optimzetoxes.log
import de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.token.OptimizeBearerTokenService
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.out.RawDataQuery
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
@EnableConfigurationProperties(OptimizeClientProperties::class)
@RegisterReflectionForBinding(OptimizeData::class)
internal class OptimizeRawDataQueryService(
    private val optimizeClientProperties: OptimizeClientProperties,
    private val optimizeBearerTokenService: OptimizeBearerTokenService,
): RawDataQuery, Logging {

    @Throws(DataQueryException::class)
    override fun queryData(): OptimizeData {
        val token = optimizeClientProperties.bearerToken ?: optimizeBearerTokenService.queryToken()

        val uri = "/api/public/export/report/${optimizeClientProperties.reportId}/result/json?paginationTimeout=60&limit=2"

        log().info("Fetch Camunda Optimize Raw Data Export from URI {}'", uri)

        return WebClient.builder()
            .baseUrl(optimizeClientProperties.baseUrl!!)
            .build()
            .get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _ ->
                Mono.error { throw DataQueryException("Could not fetch data from optimize") }
            }
            .bodyToMono(OptimizeData::class.java)
            .blockOptional()
            .orElseThrow { DataQueryException("Could not parse data from optimize") }
    }
}

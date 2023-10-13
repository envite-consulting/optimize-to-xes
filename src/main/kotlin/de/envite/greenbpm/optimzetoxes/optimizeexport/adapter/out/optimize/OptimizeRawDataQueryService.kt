package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.out.RawDataQuery
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class OptimizeRawDataQueryService(
    private val webClient: WebClient,
    private val optimizeClientProperties: OptimizeClientProperties
): RawDataQuery {

    @Throws(DataQueryException::class)
    override fun queryData(): OptimizeData = webClient
            .get()
            .uri("/api/public/export/report/${optimizeClientProperties.reportId}/result/json?paginationTimeout=60&limit=2")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _ ->
                Mono.error { throw DataQueryException("Could not fetch data from optimize") }
            }
            .bodyToMono(OptimizeData::class.java)
            .blockOptional()
            .orElseThrow { DataQueryException("Could not parse data from optimize") }

}
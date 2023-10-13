package de.luc.weinbrecht.optimzetoxes.optimizeexport.adapter.out.optimize

import de.luc.weinbrecht.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.luc.weinbrecht.optimzetoxes.optimizeexport.usecase.out.RawDataQuery
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OptimizeRawDataQueryService(
    private val webClient: WebClient,
    private val optimizeClientProperties: OptimizeClientProperties
): RawDataQuery {

    override fun queryData(): OptimizeData = webClient
            .get()
            .uri("/api/public/export/report/${optimizeClientProperties.reportId}/result/json?paginationTimeout=60&limit=2")
            .retrieve()
            .bodyToMono(OptimizeData::class.java)
            .blockOptional()
            .orElseThrow { RuntimeException("Could not parse data from optimize") }

}
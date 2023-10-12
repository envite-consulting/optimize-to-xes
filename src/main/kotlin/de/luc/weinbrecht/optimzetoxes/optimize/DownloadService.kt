package de.luc.weinbrecht.optimzetoxes.optimize

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class DownloadService(
    private val webClient: WebClient,
    private val optimizeClientProperties: OptimizeClientProperties
) {

    // https://docs.camunda.io/optimize/apis-tools/optimize-api/report/get-data-export/
    fun fetchData(): OptimizeData = webClient
            .get()
            .uri("/api/public/export/report/${optimizeClientProperties.reportId}/result/json?paginationTimeout=60&limit=2")
            .retrieve()
            .bodyToMono(OptimizeData::class.java)
            .blockOptional()
            .orElseThrow { RuntimeException("Could not parse data from optimize") }
}
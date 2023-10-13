package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient


@Configuration
@EnableConfigurationProperties(OptimizeClientProperties::class)
private class OptimizeClient {

    // TODO: Get token by e.g. using Spring Boot OAuth Client
    @Bean
    fun configureWebClient(optimizeClientProperties: OptimizeClientProperties): WebClient {
        return WebClient.builder()
            .baseUrl(optimizeClientProperties.baseUrl!!)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ${optimizeClientProperties.bearerToken}")
            .build()
    }
}
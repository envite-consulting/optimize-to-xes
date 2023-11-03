package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
@EnableConfigurationProperties(OptimizeClientProperties::class)
private class OptimizeClient {

    @Bean("optimize-client")
    fun configureOptimizeWebClient(optimizeClientProperties: OptimizeClientProperties): WebClient =
        WebClient.builder()
            .baseUrl(optimizeClientProperties.baseUrl!!)
            .build()
    @Bean("login-client")
    fun configureLoginWebClient(optimizeClientProperties: OptimizeClientProperties): WebClient =
        WebClient.builder()
            .baseUrl(optimizeClientProperties.tokenBaseUrl!!)
            .build()
}
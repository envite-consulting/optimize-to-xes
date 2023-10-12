package de.luc.weinbrecht.optimzetoxes.optimize

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "optimize")
data class OptimizeClientProperties(
    var baseUrl: String? = null,
    var bearerToken: String? = null,
    var reportId: String? = null,
)

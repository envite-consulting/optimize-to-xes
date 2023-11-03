package de.envite.greenbpm.optimzetoxes.xesmapping

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("xes-mapping")
class XesMappingConfigurationProperties(
    val filename: String,
)
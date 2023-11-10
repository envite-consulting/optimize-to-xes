package de.envite.greenbpm.optimzetoxes

import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import de.envite.greenbpm.optimzetoxes.xesmapping.XesMappingConfigurationProperties
import de.envite.greenbpm.optimzetoxes.xesmapping.toXMLFile
import de.envite.greenbpm.optimzetoxes.xesmapping.toXes
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(XesMappingConfigurationProperties::class)
class OptimizeToXesApplication(
    private val optimizeDataQuery: OptimizeDataQuery,
    private val xesMappingConfigurationProperties: XesMappingConfigurationProperties
) : CommandLineRunner, Logging {
    override fun run(vararg args: String?) =
        optimizeDataQuery.fetchData()
            .toXes(log())
            .forEach{processDefinition ->
                processDefinition.toXMLFile(xesMappingConfigurationProperties.basePath, log())
            }
}

fun main(args: Array<String>) {
    runApplication<OptimizeToXesApplication>(*args)
}

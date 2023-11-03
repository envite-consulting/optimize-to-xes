package de.envite.greenbpm.optimzetoxes

import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import de.envite.greenbpm.optimzetoxes.xesmapping.XesMappingConfigurationProperties
import de.envite.greenbpm.optimzetoxes.xesmapping.toXes
import de.envite.greenbpm.optimzetoxes.xesmapping.writeXESLogToXML
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
	init {
        require(xesMappingConfigurationProperties.filename.endsWith(".xml")) {
            "You must provide a filename for the resulting XML which ends with '.xml'"
        }
	}
    override fun run(vararg args: String?) =
        writeXESLogToXML(optimizeDataQuery.fetchData().toXes(), xesMappingConfigurationProperties.filename)
}

fun main(args: Array<String>) {
    runApplication<OptimizeToXesApplication>(*args)
}

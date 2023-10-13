package de.luc.weinbrecht.optimzetoxes

import de.luc.weinbrecht.optimzetoxes.optimizeexport.adapter.out.optimize.OptimizeRawDataQueryService
import de.luc.weinbrecht.optimzetoxes.xesmapping.toXes
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ConverterRunner(
    private val optimizeRawDataQueryService: OptimizeRawDataQueryService,
): CommandLineRunner, Logging {

    override fun run(vararg args: String?) {
        val result = optimizeRawDataQueryService.queryData()
        log().info("Fetch data {}", result)
        log().info("Converting raw data to XES: {}", result.toXes())
    }
}
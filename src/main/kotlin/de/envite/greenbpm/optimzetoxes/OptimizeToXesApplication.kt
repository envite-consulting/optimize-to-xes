package de.envite.greenbpm.optimzetoxes

import de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.OptimizeRawDataQueryService
import de.envite.greenbpm.optimzetoxes.xesmapping.toXes
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OptimizeToXesApplication(
	private val optimizeRawDataQueryService: OptimizeRawDataQueryService,
): CommandLineRunner, Logging {
	override fun run(vararg args: String?) {
		val result = optimizeRawDataQueryService.queryData()
		log().info("Fetch data {}", result)
		log().info("Converting raw data to XES: {}", result.toXes())
	}
}

fun main(args: Array<String>) {
	runApplication<OptimizeToXesApplication>(*args)
}

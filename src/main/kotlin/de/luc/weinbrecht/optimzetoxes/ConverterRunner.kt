package de.luc.weinbrecht.optimzetoxes

import de.luc.weinbrecht.optimzetoxes.optimize.DownloadService
import de.luc.weinbrecht.optimzetoxes.optimize.toXes
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ConverterRunner(
    private val downloadService: DownloadService,
): CommandLineRunner {

    override fun run(vararg args: String?) {
        val result = downloadService.fetchData()
        result.toXes()
        println(result)
    }
}
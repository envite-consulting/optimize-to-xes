package de.envite.greenbpm.optimzetoxes

import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import de.envite.greenbpm.optimzetoxes.xesmapping.toXes
import org.deckfour.xes.model.XLog
import org.deckfour.xes.out.XSerializer
import org.deckfour.xes.out.XesXmlSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

@SpringBootApplication
class OptimizeToXesApplication(
	private val optimizeDataQuery: OptimizeDataQuery,
	@Value("\${filename}")
	private val filename: String? = null
): CommandLineRunner, Logging {
	override fun run(vararg args: String?) {
		val resultFilename: String = filename ?: throw IllegalArgumentException("You must provide a filename for the resulting XML.")
		val result = optimizeDataQuery.fetchData()
		log().info("Fetch data {}", result)
		val xes = result.toXes()
		log().info("Converting raw data to XES: {}", xes)
		writeXESLogToXML(xes, resultFilename)
	}

	@Throws(Exception::class)
	private fun writeXESLogToXML(log: XLog, filename: String) {
		log().trace("Writing XES to file {}", filename)
		val file = File(filename)
		val fileOutput = FileOutputStream(file)
		val bufferedOutput = BufferedOutputStream(fileOutput)
		val logSerializer: XSerializer = XesXmlSerializer()
		logSerializer.serialize(log, bufferedOutput)
		bufferedOutput.close()
		fileOutput.close()
	}
}

fun main(args: Array<String>) {
	runApplication<OptimizeToXesApplication>(*args)
}

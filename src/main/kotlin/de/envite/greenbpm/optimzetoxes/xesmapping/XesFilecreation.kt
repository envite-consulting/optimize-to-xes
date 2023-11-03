package de.envite.greenbpm.optimzetoxes.xesmapping

import org.deckfour.xes.model.XLog
import org.deckfour.xes.out.XSerializer
import org.deckfour.xes.out.XesXmlSerializer
import org.slf4j.Logger
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

@Throws(Exception::class)
fun writeXESLogToXML(log: XLog, filename: String, logger: Logger? = null) {
    logger?.debug("Writing XES to file {}", filename)
    val fileOutput = FileOutputStream(File(filename))
    val bufferedOutput = BufferedOutputStream(fileOutput)
    val logSerializer: XSerializer = XesXmlSerializer()
    logSerializer.serialize(log, bufferedOutput)
    bufferedOutput.close()
    fileOutput.close()
}
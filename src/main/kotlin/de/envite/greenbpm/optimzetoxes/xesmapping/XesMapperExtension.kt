package de.envite.greenbpm.optimzetoxes.xesmapping

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.FlowNodeInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.ProcessInstance
import org.deckfour.xes.classification.XEventAttributeClassifier
import org.deckfour.xes.factory.XFactoryRegistry
import org.deckfour.xes.model.XAttributeMap
import org.deckfour.xes.model.XEvent
import org.deckfour.xes.model.XLog
import org.deckfour.xes.model.XTrace
import org.deckfour.xes.model.impl.XAttributeLiteralImpl
import org.deckfour.xes.model.impl.XAttributeMapImpl
import org.slf4j.Logger
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*

private const val CONCEPT_NAME = "concept:name"
private const val ORG_GROUP = "org:group"

private val factory = XFactoryRegistry.instance().currentDefault()

// TODO: Restriction: Single Process-Definition
fun OptimizeData.toXes(logger: Logger? = null): XLog {
    logger?.debug("Converting JSON Data to XES")

    val log = factory.createLog()
    val logAttributes: XAttributeMap = XAttributeMapImpl()
    log.attributes = logAttributes

    log.classifiers.add(XEventAttributeClassifier(CONCEPT_NAME, CONCEPT_NAME))
    log.classifiers.add(XEventAttributeClassifier(ORG_GROUP, ORG_GROUP))

    data[0].let {
        val processDefinitionKeyAttribute = XAttributeLiteralImpl("processDefinitionKey", data[0].processDefinitionKey)
        val processDefinitionIdAttribute = XAttributeLiteralImpl("processDefinitionId", data[0].processDefinitionId)

        log.globalTraceAttributes.add(processDefinitionKeyAttribute)
        log.globalTraceAttributes.add(processDefinitionIdAttribute)
    }

    val traces = data.map { it.toXes() }

    log.addAll(traces)
    return log
}

fun ProcessInstance.toXes(): XTrace {
    val trace = factory.createTrace()
    trace.attributes[CONCEPT_NAME] = factory.createAttributeLiteral(CONCEPT_NAME, processInstanceId, null)
    val events = flowNodeInstances
        .sortedBy { it.startDate }
        .map { it.toXes(processInstanceId, variables) }
    trace.addAll(events.flatten())
    return trace
}

fun FlowNodeInstance.toXes(processInstanceId: String, variables: Map<String, String>): List<XEvent> {
    val result: MutableList<XEvent> = mutableListOf()
    if (startDate != endDate) {
        val startEvent: XEvent = createEvent(name, processInstanceId, variables, Lifecycle.START, startDate)
        val endEvent: XEvent? = endDate?.let { createEvent(name, processInstanceId, variables, Lifecycle.COMPLETE, it) }
        result.add(startEvent)
        endEvent?.let { result.add(it) }
    } else {
        result.add(createEvent(name, processInstanceId, variables, Lifecycle.UNKNOWN, startDate))
    }
    return result
}

private enum class Lifecycle { START, COMPLETE, UNKNOWN }

private fun createEvent(
    name: String,
    processInstanceId: String,
    variables: Map<String, String>,
    type: Lifecycle,
    timestamp: String
): XEvent {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val date: Date = dateFormat.parse(timestamp)

    val event = factory.createEvent()
    addVariablesToEvent(event, variables)

    event.attributes[CONCEPT_NAME] = factory.createAttributeLiteral(CONCEPT_NAME, name, null)
    event.attributes[ORG_GROUP] = factory.createAttributeLiteral(ORG_GROUP, processInstanceId, null)
    event.attributes["lifecycle:transition"] =
        factory.createAttributeLiteral("lifecycle:transition", type.name.lowercase(), null)
    event.attributes["time:timestamp"] = factory.createAttributeTimestamp("time:timestamp", date.time, null)
    return event
}

private fun addVariablesToEvent(event: XEvent, variables: Map<String, String>) {
    variables.keys
        .filter { key -> variables[key] != "<<OBJECT_VARIABLE_VALUE>>" }
        .filter { key -> variables[key]!!.isNotEmpty() }
        .forEach { key ->
            val value: String = variables[key]!!

            if (value.toBigDecimalOrNull() != null) {
                event.attributes[key] = factory.createAttributeBoolean(key, value.toBoolean(), null)
            } else if (value.toDoubleOrNull() != null) {
                event.attributes[key] = factory.createAttributeContinuous(key, value.toDouble(), null)
            } else if (value.toLongOrNull() != null) {
                event.attributes[key] = factory.createAttributeDiscrete(key, value.toLong(), null)
            } else if (value.toDateOrNull() != null) {
                event.attributes[key] = factory.createAttributeTimestamp(key, value.toDate(), null)
            } else {
                event.attributes[key] = factory.createAttributeLiteral(key, value.lines().joinToString(""), null)
            }
        }
}

fun String.toDateOrNull(): Long? = runCatching { OffsetDateTime.parse(this).toEpochSecond() }.getOrNull()


fun String.toDate(): Long = this.toDateOrNull()!!
package de.envite.greenbpm.optimzetoxes.xesmapping

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.FlowNodeInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.ProcessInstance
import org.deckfour.xes.factory.XFactoryRegistry
import org.deckfour.xes.model.XAttributeMap
import org.deckfour.xes.model.XEvent
import org.deckfour.xes.model.XLog
import org.deckfour.xes.model.XTrace
import org.deckfour.xes.model.impl.XAttributeLiteralImpl
import org.deckfour.xes.model.impl.XAttributeMapImpl
import java.text.SimpleDateFormat
import java.util.*


private val factory = XFactoryRegistry.instance().currentDefault()

// TODO: Restriction: Single Process-Definition
fun OptimizeData.toXes(): XLog {
    val log = factory.createLog()
    val logAttributes: XAttributeMap = XAttributeMapImpl()
    log.attributes = logAttributes

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
    val events = flowNodeInstances.map { it.toXes() }
    // TODO: Add variables
    trace.addAll(events.flatten())
    return trace
}

fun FlowNodeInstance.toXes(): List<XEvent> {
    val startEvent: XEvent = createEvent(name, Lifecyle.start, startDate)
    val endEvent: XEvent? = endDate?.let { createEvent(name, Lifecyle.complete, it) }
    val result = mutableListOf(startEvent)
    endEvent?.let { result.add(it) }
    return result
}

private enum class Lifecyle {
    start, complete
}

private fun createEvent(name: String, type: Lifecyle, timestamp: String): XEvent {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val date: Date = dateFormat.parse(timestamp)

    val event = factory.createEvent()
    event.attributes["concept:name"] = factory.createAttributeLiteral("concept:name", name, null)
    event.attributes["lifecycle:transition"] = factory.createAttributeLiteral("lifecycle:transition", type.name, null)
    event.attributes["time:timestamp"] = factory.createAttributeTimestamp("time:timestamp", date.time, null)
    return event
}
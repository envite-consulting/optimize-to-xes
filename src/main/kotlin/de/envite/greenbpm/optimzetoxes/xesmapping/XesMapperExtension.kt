package de.envite.greenbpm.optimzetoxes.xesmapping

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.FlowNodeInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.ProcessInstance
import org.deckfour.xes.model.XAttributeMap
import org.deckfour.xes.model.XLog
import org.deckfour.xes.model.impl.XAttributeMapImpl
import org.deckfour.xes.model.impl.XLogImpl

fun OptimizeData.toXes(): XLog {
    val processInstances = data.map { it.toXes() }
    val attributeMap: XAttributeMap = XAttributeMapImpl()
    val xesLog = XLogImpl(attributeMap)
    TODO("Not yet implemented")
}

fun ProcessInstance.toXes(): XAttributeMap {
    val attributeMap: XAttributeMap = XAttributeMapImpl()
    TODO("Not yet implemented")
}

fun FlowNodeInstance.toXes(): XAttributeMap = TODO("Not yet implemented")
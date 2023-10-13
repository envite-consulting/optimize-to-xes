package de.luc.weinbrecht.optimzetoxes.optimizeexport.domain.model

data class OptimizeData(
    var data: List<ProcessInstance> = listOf()
)

data class ProcessInstance(
    var processDefinitionKey: String = "",
    var processDefinitionId: String = "",
    var processInstanceId: String = "",
    var businessKey: String = "",
    var variables: Map<String, String> = mapOf(),
    var flowNodeInstances: List<FlowNodeInstance> = listOf(),
)

data class FlowNodeInstance(
    var id: String = "",
    var name: String = "",
    var startDate: String = "",
    var endDate: String? = "",
    var duration: Number? = null,
)
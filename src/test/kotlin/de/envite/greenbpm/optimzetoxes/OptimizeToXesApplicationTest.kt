package de.envite.greenbpm.optimzetoxes

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.FlowNodeInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.ProcessInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OptimizeToXesApplicationTest {

    private val optimizeDataQueryMock: OptimizeDataQuery = mockk()

    private lateinit var classUnderTest: OptimizeToXesApplication
    @BeforeEach
    fun setUp() {
        classUnderTest = OptimizeToXesApplication(optimizeDataQueryMock)
    }

    @Test
    fun should_convert_to_xes() {
        mockDataQuery()

        classUnderTest.run()

    }

    private fun mockDataQuery() {
        every { optimizeDataQueryMock.fetchData() } returns OptimizeData(
            listOf(
                ProcessInstance(
                    "definition-key-1",
                    "definition-id-1",
                    "instance-id-1",
                    "business-key-1A",
                    variables = mapOf(),
                    flowNodeInstances = listOf(
                        FlowNodeInstance(
                            "11",
                            "node-name-11",
                            "2023-09-17T16:42:23.397+0000",
                            "2023-09-17T16:43:23.397+0000"
                        ),
                        FlowNodeInstance(
                            "22",
                            "node-name-22",
                            "2023-09-17T16:43:24.397+0000",
                            "2023-09-17T16:44:24.397+0000"
                        ),
                    )
                )
            )
        )
    }
}
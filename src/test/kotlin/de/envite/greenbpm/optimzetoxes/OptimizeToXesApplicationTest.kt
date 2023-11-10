package de.envite.greenbpm.optimzetoxes

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.FlowNodeInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.ProcessInstance
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import de.envite.greenbpm.optimzetoxes.xesmapping.XesMappingConfigurationProperties
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class OptimizeToXesApplicationTest {

    private val optimizeDataQueryMock: OptimizeDataQuery = mockk()

    private lateinit var classUnderTest: OptimizeToXesApplication

    @Nested
    inner class ETL {

        @BeforeEach
        fun setUp() {
            classUnderTest = OptimizeToXesApplication(optimizeDataQueryMock, XesMappingConfigurationProperties("target"))
        }

        @Test
        fun should_convert_to_xes() {
            mockDataQuery()

            classUnderTest.run()
        }
    }

    private fun mockDataQuery() {
        every { optimizeDataQueryMock.fetchData() } returns OptimizeData(
            listOf(
                ProcessInstance(
                    "definition-key-1",
                    "definition-id-1",
                    "instance-id-1",
                    "business-key-1A",
                    variables = mockVariableDataQuery(),
                    flowNodeInstances = listOf(
                        FlowNodeInstance(
                            "11",
                            "node-name-11",
                            "2023-09-17T16:42:23.397+0000",
                            "2023-09-17T16:43:23.397+0000"
                        ),
                        FlowNodeInstance(
                            "12",
                            "node-name-12",
                            "2023-09-17T16:43:24.397+0000",
                            "2023-09-17T16:44:24.397+0000"
                        ),
                    )
                ),
                ProcessInstance(
                    "definition-key-2",
                    "definition-id-2",
                    "instance-id-2",
                    "business-key-2B",
                    variables = mockVariableDataQuery(),
                    flowNodeInstances = listOf(
                        FlowNodeInstance(
                            "11",
                            "node-name-11",
                            "2023-09-17T16:42:23.397+0000",
                            "2023-09-17T16:43:23.397+0000"
                        ),
                        FlowNodeInstance(
                            "12",
                            "node-name-12",
                            "2023-09-17T16:43:24.397+0000",
                            "2023-09-17T16:44:24.397+0000"
                        ),
                        FlowNodeInstance(
                            "21",
                            "node-name-21",
                            "2023-09-17T16:44:25.397+0000",
                            "2023-09-17T16:45:25.397+0000"
                        ),
                        FlowNodeInstance(
                            "22",
                            "node-name-22",
                            "2023-09-17T16:45:26.397+0000",
                            "2023-09-17T16:46:27.397+0000"
                        ),
                    )
                )
            )
        )
    }

    private fun mockVariableDataQuery(): Map<String, String> = mapOf(
        "applicantName" to "Yankee Russell",
        "application" to "<<OBJECT_VARIABLE_VALUE>>",
        "application.applicant.age" to "36.0",
        "application.applicant.birthday" to "5.10797457438E11",
        "application.applicant.email" to "yankeerussell@hotmail.com",
        "application.applicant.gender" to "male",
        "application.applicant.name" to "Yankee Russell",
        "application.applicant.score" to "97.0",
        "application.applicationNumber" to "A-6932197",
        "application.category" to "Premium Package",
        "application.contractNumber" to "2022-03-10T00 to10:57.447+0000",
        "application.corporation" to "Camunbankia",
        "application.employment" to "Salaried",
        "application.premium" to "0,00 €",
        "application.premiumInCent" to "0.0",
        "application.priceIndication" to "320,00 €",
        "application.priceIndicationInCent" to "32000.0",
        "application.product" to "super cool product",
        "applicationNumber" to "A-6932197",
        "approved" to "",
        "category" to "Premium Package",
        "corporation" to "Camunbankia",
        "documentReferenceId" to "",
        "mailBody" to "Dear Yankee Russell.\n\nWe happily inform you that your request was accepted.\n\nPlease check the key facts: \n- Application Number: A-6932197\n- Premium per anno: 0,00 €\n- Product: super cool product\n\nKind regards,\nCamunbankia.",
        "mailSubject" to "Your request was issued",
        "requestedDocumentDescription" to "",
        "riskLevel" to "green",
        "risks" to "<<OBJECT_VARIABLE_VALUE>>",
        "risks._listSize" to "0",
        "score" to "97",
        "uiBaseUrl" to ""
    )
}
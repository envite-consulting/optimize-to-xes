package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient

class OptimizeRawDataQueryServiceTest {

    companion object {

        lateinit var mockWebServer: MockWebServer

        @JvmStatic
        @BeforeAll
        fun setUp() {
            mockWebServer = MockWebServer()
            mockWebServer.start()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            mockWebServer.shutdown()
        }
    }

    private lateinit var classUnderTest: OptimizeRawDataQueryService
    private val optimizeClientProperties: OptimizeClientProperties = OptimizeClientProperties().apply { reportId = "1" }

    @BeforeEach
    fun setUpClassUnderTest() {
        val webclient = WebClient.builder()
            .baseUrl("http://localhost:${mockWebServer.port}")
            .build()
        classUnderTest = OptimizeRawDataQueryService(webclient, optimizeClientProperties)
    }

    @Test
    fun should_query_data() {
        enqueueToMock(mockWebServer, "/optimize-response/optimize-sample-export-response.json")

        val expectedProcessDefinitionKey = "customer_onboarding_en"

        val result = classUnderTest.queryData()

        result shouldNotBe null
        result.data shouldHaveSize  2
        result.data.map { it.processDefinitionKey }.shouldContainOnly(expectedProcessDefinitionKey)
        assertSoftly {
            result.data[0].businessKey shouldBe "A-07022"
            result.data[1].businessKey shouldBe "A-07018"
            result.data.map { it.flowNodeInstances } shouldNotHaveSize 0
            result.data.map { it.variables } shouldNotHaveSize 0
        }
        // TODO: Test more fields
    }

    @Test
    fun should_throw_on_404() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val exception = shouldThrow<DataQueryException> {
            classUnderTest.queryData()
        }
        exception.message shouldContain "Could not fetch data from optimize"
    }

    @Test
    fun should_throw_on_500() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val exception = shouldThrow<DataQueryException> {
            classUnderTest.queryData()
        }
        exception.message shouldContain "Could not fetch data from optimize"
    }

    private fun enqueueToMock(mockWebServer: MockWebServer, filePath: String) {
        val content = this.javaClass.getResourceAsStream(filePath)?.bufferedReader() ?:
        throw IllegalArgumentException("File $filePath not found")

        mockWebServer.enqueue(
            MockResponse()
                .setBody(content.readText())
                .addHeader("Content-Type", "application/json")
        )
    }
}

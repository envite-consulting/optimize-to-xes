package de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.token

import de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.DataQueryException
import de.envite.greenbpm.optimzetoxes.optimizeexport.adapter.out.optimize.OptimizeClientProperties
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OptimizeBearerTokenServiceTest {

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

    private lateinit var classUnderTest: OptimizeBearerTokenService
    private val optimizeClientProperties: OptimizeClientProperties = OptimizeClientProperties().apply {
        clientId = "7"
        clientSecret = "secret"
    }

    @BeforeEach
    fun setUpClassUnderTest() {
        optimizeClientProperties.tokenBaseUrl = "http://localhost:${mockWebServer.port}"
        classUnderTest = OptimizeBearerTokenService(optimizeClientProperties)
    }

    @Test
    fun should_query_data() {
        enqueueToMock(mockWebServer, "/optimize-response/optimize-sample-token-response.json")

        val result = classUnderTest.queryToken()

        result shouldBe "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlFVVXdPVFpDUTBVM01qZEVRME0wTkRFelJrUkJORFk0T0RZeE1FRTBSa1pFUlVWRVF6bERNZyJ9.eyJodHRwczovL2NhbXVuZGEuY29tL29yZ0lkIjoiZjFhMDgyZTgtNjcyOC00NDJkLTlkZjAtMzlmZmNjMzlmNWVlIiwiaXNzIjoiaHR0cHM6Ly93ZWJsb2dpbi5jbG91ZC5jYW11bmRhLmlvLyIsInN1YiI6Im13bzkwdDJyMzE2MDd6MzZCTkg2OXRXRktCWDU1ajFXQGNsaWVudHMiLCJhdWQiOiJvcHRpbWl6ZS5jYW11bmRhLmlvIiwiaWF0IjoxNjk5MDA0NTg4LCJleHAiOjE2OTkwOTA5ODgsImF6cCI6Im13bzkwdDJyMzE2MDd6MzZCTkg2OXRXRktCWDU1ajFXIiwic2NvcGUiOiI1YTM1YmY0ZC1lNWI0LTQyMDItYmFkMi0xMTA1MjNiOTBhNTAiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.W7waSsKU120ZcnbBqi_VNMJjlmrJ2m9LnbQ4pK6pPddkDGtRnyoE7qYqpp0u_iN1kW5bmDwru9WA6ulVNjxxG_QqR0veVQAdW01dq4kBBsbth8a_f2zKL4FV2_qGwV7HFE0JK4tgf_nLTIbu4JYbMfmu3s7U7BDC4n-43K1gdzM3UZw3Cy4mxRwTy-e09Z7WDrijmzAWVfeCf2O6nsFVw8RPi1EdGdN7u-5tPb7EK5NHn4WCMj7yhEV06R1DNu7QF2m1Ww905xxbYBRtJD4Tpv1Gr6zLd82rmXdVVJ3RK7QygfsCZiQfhQY-CLpQwBTnlSviIpNei4S-0vZ8NwsgXQ"
        mockWebServer.takeRequest().body.readUtf8() shouldBe "{\"client_id\":\"7\",\"client_secret\":\"secret\",\"audience\":\"optimize.camunda.io\",\"grant_type\":\"client_credentials\"}"
    }

    @Test
    fun should_throw_on_404() {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val exception = shouldThrow<DataQueryException> {
            classUnderTest.queryToken()
        }
        exception.message shouldContain "Could not fetch Bearer Token from optimize"
    }

    @Test
    fun should_throw_on_500() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val exception = shouldThrow<DataQueryException> {
            classUnderTest.queryToken()
        }
        exception.message shouldContain "Could not fetch Bearer Token from optimize"
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

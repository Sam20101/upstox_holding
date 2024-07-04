package com.example.upstox_holding

import com.example.upstox_holding.API.UpstoxAPIService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class UpstoxApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var upstoxAPIService: UpstoxAPIService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        upstoxAPIService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UpstoxAPIService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getUserHoldingsreturnscorrectdata() = runBlocking {

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                  "data": {
                    "userHolding": [
                      {
                        "symbol": "MAHABANK",
                        "quantity": 990,
                        "ltp": 38.05,
                        "avgPrice": 35,
                        "close": 40
                      },
                      {
                        "symbol": "ICICI",
                        "quantity": 100,
                        "ltp": 118.25,
                        "avgPrice": 110,
                        "close": 105
                      }
                    ]
                  }
                }
                """
            )

        // Enqueue the mock response
        mockWebServer.enqueue(mockResponse)

        // Make the API call
        val response = upstoxAPIService.getHoldingStocks()

        // Assert the response
        assertEquals(2, response.body()?.holdingResponseData?.userHoldingModelList?.size)

        // Verify the request
        val request = mockWebServer.takeRequest()
        assertEquals("/", request.path)
        assertEquals("GET", request.method)
    }


    @Test
    fun `getUserHoldings handles error response`() = runBlocking {

        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Internal Server Error")


        mockWebServer.enqueue(mockResponse)

        try {

            upstoxAPIService.getHoldingStocks()
        } catch (e: Exception) {

            assertEquals(retrofit2.HttpException::class.java, e::class.java)
        }


        val request = mockWebServer.takeRequest()
        assertEquals("/", request.path)
        assertEquals("GET", request.method)
    }
}
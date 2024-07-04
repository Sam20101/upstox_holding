package com.example.upstox_holding.Repository

import com.example.upstox_holding.API.UpstoxAPIService
import com.example.upstox_holding.Model.HoldingResponseData
import com.example.upstox_holding.Model.HoldingResponseList
import com.example.upstox_holding.Model.UserHoldingModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserHoldingRepositoryTest {
    @Mock
    private lateinit var upstoxAPIService: UpstoxAPIService

    private lateinit var repository: UserHoldingRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = UserHoldingRepository(upstoxAPIService)
    }

    @Test
    fun `getUserHoldings_correct_successful response`() = runBlockingTest {
        val mockUserHoldings = listOf(
            UserHoldingModel(38.05, 35.0, 40.0, 990, "MAHABANK"),
            UserHoldingModel(38.05, 35.0, 40.0, 990, "MAHABANK")

        )
        val mockApiResponse = HoldingResponseData(HoldingResponseList(mockUserHoldings))
        val mockResponse = Response.success(mockApiResponse)


        `when`(upstoxAPIService.getHoldingStocks()).thenReturn(mockResponse)


        val result = repository.getUserHoldings()


        assertEquals(mockUserHoldings, result)
    }

}
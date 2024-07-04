package com.example.upstox_holding.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.upstox_holding.Model.UserHoldingModel
import com.example.upstox_holding.Repository.UserHoldingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserHoldingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: UserHoldingRepository

    private lateinit var viewModel: UserHoldingViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = UserHoldingViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetchUserHoldings_updates_onsuccess`() = runBlocking {
        val mockUserHoldings = listOf(
            UserHoldingModel(38.05, 35.0, 40.0, 990, "MAHABANK"),
            UserHoldingModel(38.05, 35.0, 40.0, 990, "MAHABANK"),
        )
        `when`(repository.getUserHoldings()).thenReturn(mockUserHoldings)

        viewModel.fetchUserHolding()

        testDispatcher.advanceUntilIdle()
        assert(viewModel.userHoldingList.value?.size == mockUserHoldings.size)

    }
}
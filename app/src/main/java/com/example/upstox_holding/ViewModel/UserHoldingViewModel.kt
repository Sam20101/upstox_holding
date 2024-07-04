package com.example.upstox_holding.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upstox_holding.Model.UserHoldingModel
import com.example.upstox_holding.Repository.UserHoldingRepository
import kotlinx.coroutines.launch
import java.io.IOException

class UserHoldingViewModel(private val userHoldingRepository: UserHoldingRepository) :ViewModel(){
    private val mUserHoldingList= MutableLiveData<List<UserHoldingModel>>()
     val userHoldingList:LiveData<List<UserHoldingModel>> = mUserHoldingList

    private val mUserHoldingTotalCurrentValue=MutableLiveData<Double>()
    val userHoldingTotalCurrentValue:LiveData<Double> = mUserHoldingTotalCurrentValue

    private val mUserHoldingTotalInvestment=MutableLiveData<Double>()
    val userHoldingTotalInvestment:LiveData<Double> = mUserHoldingTotalInvestment

    private val mUserHoldingTodayPL=MutableLiveData<Double>()
    val userHoldingTodayPL:LiveData<Double> = mUserHoldingTodayPL

    private val mUserHoldingTotalPL=MutableLiveData<Double>()
    val userHoldingTotalPL:LiveData<Double> = mUserHoldingTotalPL

    private val mUserHoldingTotalPLPercentage=MutableLiveData<Double>()
    val userHoldingTotalPLPercentage:LiveData<Double> = mUserHoldingTotalPLPercentage

    private val mIsLoading=MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> = mIsLoading


    private val mErrorMessage=MutableLiveData<String>()
    val errorMessage:LiveData<String> = mErrorMessage
    fun fetchUserHolding(){

        viewModelScope.launch {
            try {
                mIsLoading.value = true
                val holdings = userHoldingRepository.getUserHoldings()
                mUserHoldingList.value = holdings
                fetchUserHoldingTotalCurrentValue(holdings)
                mIsLoading.value=false
                mErrorMessage.value="Successful"
            } catch (e: IOException) {
                mIsLoading.value=false
                mErrorMessage.value="Internet Connection"
            }
            catch (e :Exception)
            {
                mErrorMessage.value="Can not fetch data"
                mIsLoading.value=false
            }finally {
                mIsLoading.value=false
            }
        }
    }

    fun fetchUserHoldingTotalCurrentValue(userHolding:List<UserHoldingModel>)
    {

        var totalCurrentValue=0.00
        var totalInvestment=0.00
        var todayPL=0.00
        for (holding in userHolding) {
            totalCurrentValue += holding.ltp * holding.quantity
            totalInvestment+=holding.avgPrice*holding.quantity
            todayPL+=(holding.close-holding.ltp)*holding.quantity
        }

        mUserHoldingTotalCurrentValue.value=totalCurrentValue
        mUserHoldingTotalInvestment.value=totalInvestment
        mUserHoldingTodayPL.value=todayPL
        mUserHoldingTotalPL.value=totalCurrentValue-totalInvestment
        mUserHoldingTotalPLPercentage.value=((totalCurrentValue-totalInvestment)/totalInvestment)*100


    }




}
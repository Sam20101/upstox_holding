package com.example.upstox_holding.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.upstox_holding.API.RetrofitClient
import com.example.upstox_holding.ViewModel.UserHoldingViewModelFactory
import com.example.upstox_holding.Adapter.UserHoldingApdapter
import com.example.upstox_holding.R
import com.example.upstox_holding.Repository.UserHoldingRepository
import com.example.upstox_holding.ViewModel.UserHoldingViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.math.RoundingMode
import java.text.DecimalFormat


class HoldingFragment : Fragment() {
    private lateinit var holdingBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var userHoldingViewModel: UserHoldingViewModel
    private lateinit var userHoldingApdapter: UserHoldingApdapter


    lateinit var llytTotalPL: LinearLayout
    lateinit var llytBottomExpandDetail: LinearLayout
    lateinit var bottomSheet: LinearLayout

    lateinit var tvCurrentValue: TextView
    lateinit var tvTotalInvestmentValue: TextView
    lateinit var tvTodayPLValue: TextView
    lateinit var tvTotalPLValue: TextView
    lateinit var tvInternetNotConnected: TextView

    lateinit var ivUpArrow: ImageView
    lateinit var ivDownArrow: ImageView
    lateinit var progressbar: ProgressBar
    lateinit var rvUserHolding: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_holding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN


        bottomSheet = view.findViewById(R.id.holdingBottomSheet)
        llytTotalPL = view.findViewById(R.id.llytTotalPL)
        llytBottomExpandDetail = view.findViewById(R.id.llytBottomExpandDetail)
        bottomSheet = view.findViewById(R.id.holdingBottomSheet);
        tvCurrentValue = view.findViewById(R.id.tvCurrentValue)
        tvTotalInvestmentValue = view.findViewById(R.id.tvTotalInvestmentValue)
        tvTodayPLValue = view.findViewById(R.id.tvTodayPLValue)
        tvTotalPLValue = view.findViewById(R.id.tvTotalPLValue)
        ivDownArrow = view.findViewById(R.id.ivDownArrow)
        ivUpArrow = view.findViewById(R.id.ivUpArrow)
        tvInternetNotConnected = view.findViewById(R.id.tvInternetNotConnected)
        progressbar = view.findViewById(R.id.progressBar)


        val userHoldingRepository = UserHoldingRepository(RetrofitClient.instance)
        userHoldingViewModel =
            ViewModelProvider(this, UserHoldingViewModelFactory(userHoldingRepository)).get(
                UserHoldingViewModel::class.java
            )
        userHoldingApdapter = UserHoldingApdapter()
        rvUserHolding= view.findViewById<RecyclerView>(R.id.rvUserHolding)
        rvUserHolding.layoutManager = LinearLayoutManager(context)
        rvUserHolding.adapter = userHoldingApdapter

        userHoldingViewModel.userHoldingList.observe(viewLifecycleOwner) { holding ->
            userHoldingApdapter.setUserHoldings(
                holding
            )
        }
        userHoldingViewModel.errorMessage.observe(viewLifecycleOwner){
            errormsg->rvContent(errormsg)
        }
        userHoldingViewModel.userHoldingTotalCurrentValue.observe(viewLifecycleOwner) { total ->
            tvCurrentValue.text = "₹ " + df.format(total)
        }
        userHoldingViewModel.userHoldingTotalInvestment.observe(viewLifecycleOwner) { total ->
            tvTotalInvestmentValue.text = "₹ " + df.format(total)
        }

        userHoldingViewModel.userHoldingTodayPL.observe(viewLifecycleOwner) { total ->
            updatePL(tvTodayPLValue, total)
        }
        userHoldingViewModel.userHoldingTotalPL.observe(viewLifecycleOwner) { total ->
            updatePL(
                tvTotalPLValue, total
            )
        }
        userHoldingViewModel.userHoldingTotalPLPercentage.observe(viewLifecycleOwner) { total ->
            setTotalPLWithPercentage(total)
        }
        userHoldingViewModel.isLoading.observe(viewLifecycleOwner) { isprogressBar ->
            progressbar.visibility = if (isprogressBar) View.VISIBLE else View.GONE
        }



        userHoldingViewModel.fetchUserHolding()
        bottomSheetExpandable()


    }

    fun bottomSheetExpandable() {
        holdingBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        llytBottomExpandDetail.visibility = View.GONE
        llytTotalPL.visibility = View.VISIBLE
        ivUpArrow.visibility = View.VISIBLE
        ivDownArrow.visibility = View.GONE

        llytTotalPL.setOnClickListener(View.OnClickListener {
            if (holdingBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) holdingBottomSheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
            else holdingBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        holdingBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        llytBottomExpandDetail.visibility = View.VISIBLE
                        llytTotalPL.visibility = View.VISIBLE
                        ivUpArrow.visibility = View.GONE
                        ivDownArrow.visibility = View.VISIBLE
                    }

                    else -> {
                        llytBottomExpandDetail.visibility = View.GONE
                        llytTotalPL.visibility = View.VISIBLE
                        ivUpArrow.visibility = View.VISIBLE
                        ivDownArrow.visibility = View.GONE
                    }

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }


        })


    }


    fun updatePL(textValue: TextView, total: Double) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        if (total < 0) {
            textValue.text = "-₹" + df.format(total * -1)
            textValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.loss_red))
        } else {
            textValue.text = "₹" + df.format(total)
            textValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.profit_green))

        }

    }


   fun rvContent(msg:String)
   {
       if(msg.equals("Successful"))
       {
          rvUserHolding.visibility=View.VISIBLE
          tvInternetNotConnected.visibility=View.GONE
       }else
       {
           rvUserHolding.visibility=View.VISIBLE
           tvInternetNotConnected.visibility=View.VISIBLE
       }
   }

    fun setTotalPLWithPercentage(total: Double) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        var temp = tvTotalPLValue.text
        var per = temp
        tvTotalPLValue.text = temp.toString() + "(" + df.format(total) + "%)"
    }

}
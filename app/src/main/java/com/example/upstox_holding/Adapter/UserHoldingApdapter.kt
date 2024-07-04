package com.example.upstox_holding.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.upstox_holding.Model.UserHoldingModel
import com.example.upstox_holding.R
import com.google.android.material.navigation.NavigationBarItemView
import java.math.RoundingMode
import java.text.DecimalFormat

class UserHoldingApdapter:RecyclerView.Adapter<UserHoldingApdapter.ViewHolder>() {
    private var userHoldingModelList:List<UserHoldingModel> = emptyList()

    fun setUserHoldings(holdings: List<UserHoldingModel>) {
        userHoldingModelList = holdings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHoldingApdapter.ViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.row_user_holding,parent,false)
        return ViewHolder(view);

    }

    override fun onBindViewHolder(holder: UserHoldingApdapter.ViewHolder, position: Int) {
        val userHoldingItem=userHoldingModelList[position]

        var currentValue=(userHoldingItem.ltp)*(userHoldingItem.quantity)
        var totalInvest=(userHoldingItem.avgPrice)*(userHoldingItem.quantity)
        var totalPNL=currentValue-totalInvest

        val df= DecimalFormat("#.##")
        df.roundingMode= RoundingMode.DOWN
        holder.tvStockNameValue.setText(userHoldingItem.symbol)
        holder.tvLTPValue.setText("₹"+userHoldingItem.ltp.toString())
        holder.tvQuantityValue.setText(userHoldingItem.quantity.toString())

        if(totalPNL<0){
            var PNL=df.format(totalPNL*-1)
            holder.tvPLValue.setText("- ₹"+PNL.toString())
            holder.tvPLValue.setTextColor(Color.parseColor("#D22B2B"))
        }
        else{
            var PNL=df.format(totalPNL)
            holder.tvPLValue.setText("₹"+PNL.toString())
            holder.tvPLValue.setTextColor(Color.parseColor("#50C878"))
        }

    }

    override fun getItemCount(): Int {
        return userHoldingModelList.size
    }


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val tvStockNameValue: TextView =itemView.findViewById(R.id.tvStockName)
        var tvQuantityValue: TextView =itemView.findViewById(R.id.tvQuantityValue)
        var tvLTPValue: TextView =itemView.findViewById(R.id.tvLTPValue)
        var tvPLValue: TextView =itemView.findViewById(R.id.tvPLValue)

    }




}
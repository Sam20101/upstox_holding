package com.example.upstox_holding.Utils

import android.content.Context
import android.widget.Toast

class CommonUtils {
    fun showNoDataToast(context: Context)
    {
        Toast.makeText(context,"Data not Available", Toast.LENGTH_SHORT).show()

    }
}
package com.example.upstox_holding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.upstox_holding.Fragment.HoldingFragment
import com.example.upstox_holding.Utils.CommonUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity() {
    var context: Context=this@MainActivity

    lateinit var bottomNavigation:BottomNavigationView
    lateinit var llytPostions: LinearLayout
    lateinit var llytHoldings: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation=findViewById(R.id.bottomNavigationView) as BottomNavigationView
        llytPostions=findViewById(R.id.llytPositions)
        llytHoldings=findViewById(R.id.llytHolding)
        llytPostions.setOnClickListener(View.OnClickListener {
            CommonUtils().showNoDataToast(context)
        })
          bottomNavigation.selectedItemId=R.id.ic_portfolio
        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.ic_portfolio->{
                    true
                }
                else -> {CommonUtils().showNoDataToast(context)
                false}
            }
        }

        loadFragment(HoldingFragment())
    }

    private fun loadFragment(fragment:Fragment)
    {

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.stock_container, fragment)
            transaction.disallowAddToBackStack()
            transaction.commit()

    }
}
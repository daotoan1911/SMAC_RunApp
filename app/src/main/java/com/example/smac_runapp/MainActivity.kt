package com.example.smac_runapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smac_runapp.databinding.ActivityMainBinding
import com.example.smac_runapp.fragment.EventFragment
import com.example.smac_runapp.fragment.HomeFragment
import com.example.smac_runapp.fragment.fragAwards.AwardFragment
import com.example.smac_runapp.interfaces.HomeBack

class MainActivity : AppCompatActivity(), HomeBack {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        reFragment(HomeFragment(this))

        mBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> reFragment(HomeFragment(this))
                R.id.action_event -> reFragment(EventFragment(this))
                else -> {
                }
            }
            true
        }

        mBinding.backToolbar.back.setOnClickListener{
            val f = supportFragmentManager.findFragmentById(R.id.frame)
                reFragment(HomeFragment(this))
        }
    }

    private fun reFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransient = fragmentManager.beginTransaction()

        fragmentTransient.replace(R.id.frame, fragment)
        fragmentTransient.commit()
    }

    override fun replaceReceive(fragment: Fragment) {
        super.replaceReceive(fragment)
        reFragment(AwardFragment())
    }

}
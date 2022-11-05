package com.example.smac_runapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smac_runapp.R
import com.example.smac_runapp.TAG
import com.example.smac_runapp.adapter.ReceiveAdapter
import com.example.smac_runapp.adapter.TabLayoutAdapter
import com.example.smac_runapp.customviews.SpacesItemDecoration
import com.example.smac_runapp.databinding.FragmentHomeBinding
import com.example.smac_runapp.fragment.fragAwards.AwardFragment
import com.example.smac_runapp.interfaces.HomeInterface
import com.example.smac_runapp.logger.Log
import com.example.smac_runapp.models.Receive
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeFragment(private val goToHome: HomeInterface) : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private var myAdapter = ReceiveAdapter(arrayListOf(),0)
    private var lsReceive: ArrayList<Receive> = ArrayList()
    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.seekbar.indicatorPositions = listOf(0F, 0.2F, 0.4F, 0.85F)
        mBinding.seekbar.indicatorText = listOf("0", "500", "1000", "4000")
        setupViewPager()
        setTabLayout()
        setReceive()
        setUpRcv()
        readData()
        mBinding.viewAll.setOnClickListener {
            goToHome.replaceReceive(AwardFragment())
//            val fragmentManager = activity?.supportFragmentManager
//            fragmentManager?.beginTransaction()
//                ?.replace(R.id.frame, AwardFragment())
//                ?.addToBackStack(null)
//                ?.commit()
        }

    }

    private fun setUpRcv() {
        mBinding.rcv.apply {
            adapter = myAdapter
            addItemDecoration(SpacesItemDecoration(10))
            setHasFixedSize(true)
        }
    }

    private fun setReceive() {

        lsReceive.add(Receive(R.drawable.huy_chuong2, "Spectacular Breakout","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_huong1, "October Challenger","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_chuong3, "Step to Mars ","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_chuong4, "August Challenger","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_chuong2, "Spectacular Breakout","17/10/2022", true))
        lsReceive.add(Receive(R.drawable.huy_huong1, "October Challenger","17/10/2022",true))
        lsReceive.add(Receive(R.drawable.huy_chuong3, "Step to Mars ","17/10/2022",true))
        lsReceive.add(Receive(R.drawable.huy_chuong4, "August Challenger","17/10/2022", true))

        myAdapter.addData(lsReceive)
    }

    private fun setTabLayout() {
        TabLayoutMediator(
            mBinding.layoutTab, mBinding.viewpage
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.day)
                }
                1 -> {
                    tab.text = resources.getString(R.string.week)
                }
                2 -> {
                    tab.text = resources.getString(R.string.month)
                }
            }
        }.attach()
    }

    private fun setupViewPager() {
        val adapter = activity?.let { TabLayoutAdapter(it) }
        mBinding.viewpage.adapter = adapter
    }

    //Đọc tổng số bước hàng ngày hiện tại.
    private fun readData() {
        val cal: Calendar = Calendar.getInstance()
        val now = Date()
        cal.time = Date()
        val endtime: Long = cal.timeInMillis
        cal.add(Calendar.DATE, -1)
        val starttime: Long = cal.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(starttime, endtime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(this.requireActivity().applicationContext, GoogleSignIn.getAccountForExtension(this.requireActivity().applicationContext, fitnessOptions))
            .readData(readRequest)
            .addOnSuccessListener { response ->
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    for (dp in dataSet.dataPoints) {
                        for (field in dp.dataType.fields) {
                            val value = dp.getValue(field).asInt().toString()
                            numSteps.text = value
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was an error reading data from Google Fit", e)
            }

    }

}
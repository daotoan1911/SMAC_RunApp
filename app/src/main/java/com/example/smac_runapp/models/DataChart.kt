package com.example.smac_runapp.models

import com.github.mikephil.charting.data.BarEntry

data class DataChart(
    val lsAxis: ArrayList<String>,
    val lsBarEntry: ArrayList<BarEntry>
)

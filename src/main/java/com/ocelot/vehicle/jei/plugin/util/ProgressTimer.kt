package com.ocelot.vehicle.jei.plugin.util

import mezz.jei.api.gui.ITickTimer

class ProgressTimer(private val maxTime: Int) : ITickTimer {

    val invertedValue: Int get() = maxTime - value
    val percentage: Double get() = value.toDouble() / maxTime.toDouble()
    val invertedPercentage: Double get() = invertedValue.toDouble() / maxTime.toDouble()

    private var startTime = System.currentTimeMillis()

    override fun getValue(): Int = ((System.currentTimeMillis() - startTime) / NS_TO_TICK % maxTime).toInt()

    override fun getMaxValue(): Int = maxTime

    companion object {
        const val NS_TO_TICK = 1000 / 20
    }
}
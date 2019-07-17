package com.ocelot.vehicle.jei.plugin.util;

import mezz.jei.api.gui.ITickTimer;

public class ProgressTimer implements ITickTimer {

	public static final int NS_TO_TICK = 1000 / 20;

	private final int maxTime;
	private long startTime = System.currentTimeMillis();

	public ProgressTimer(int maxTime) {
		this.maxTime = maxTime;
	}

	@Override
	public int getValue() {
		return (int) ((System.currentTimeMillis() - startTime) / NS_TO_TICK % maxTime);
	}

	public int getInvertedValue() {
		return this.getMaxValue() - this.getValue();
	}

	@Override
	public int getMaxValue() {
		return maxTime;
	}

	public double getPercentage() {
		return (double) this.getValue() / (double) this.getMaxValue();
	}

	public double getInvertedPercentage() {
		return (double) this.getInvertedValue() / (double) this.getMaxValue();
	}
}

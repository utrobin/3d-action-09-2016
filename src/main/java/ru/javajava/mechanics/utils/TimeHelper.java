package ru.javajava.mechanics.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class TimeHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeHelper.class);
	private TimeHelper() { }
	public static void sleep(long period){
		if (period <= 0) {
			return;
		}
		try{
			Thread.sleep(period);
		} catch (InterruptedException e) {
			LOGGER.debug("We were interrupted!");
		}
	}
}

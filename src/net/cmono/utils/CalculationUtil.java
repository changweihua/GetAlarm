package net.cmono.utils;

public class CalculationUtil {

	public static void calculate(int sleepSeconds) {
		try {
			Thread.sleep(sleepSeconds * 1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
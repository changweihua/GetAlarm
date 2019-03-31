package net.cmono.dtos;

import android.content.Intent;
import android.os.BatteryManager;

public class BatteryInfo {
	int health;
	int batteryIconId;
	int plugged;
	boolean batteryPresent;
	int status;
	String technology;
	int temperature;
	int voltage;
	int level;
	int scale;

	public BatteryInfo(Intent intent) {
		String action = intent.getAction();
		if (!Intent.ACTION_BATTERY_CHANGED.equals(action))
			return;
		health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,
				BatteryManager.BATTERY_HEALTH_UNKNOWN);
		batteryIconId = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, -1);
		plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		batteryPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT,
				false);
		status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
				BatteryManager.BATTERY_STATUS_UNKNOWN);
		technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
		temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
		voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
		level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
	}

	@Override
	public String toString() {
		StringBuffer strBuffer = new StringBuffer(50);
		strBuffer.append("information for battery\n");
		strBuffer.append("health :");
		switch (health) {
		case BatteryManager.BATTERY_HEALTH_DEAD:
			strBuffer.append("BATTERY_HEALTH_DEAD");
			break;
		case BatteryManager.BATTERY_HEALTH_GOOD:
			strBuffer.append("BATTERY_HEALTH_GOOD");
			break;
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			strBuffer.append("BATTERY_HEALTH_OVER_VOLTAGE");
			break;
		case BatteryManager.BATTERY_HEALTH_UNKNOWN:
			strBuffer.append("BATTERY_HEALTH_UNKNOWN");
			break;
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
			strBuffer.append("BATTERY_HEALTH_UNSPECIFIED_FAILURE");
			break;
		default:
			strBuffer.append("BATTERY_HEALTH_UNKNOWN");
			break;
		}
		strBuffer.append("\n");
		strBuffer.append("iconId:" + batteryIconId);
		strBuffer.append("\n");
		strBuffer.append("Power source:");
		switch (plugged) {
		case 0:
			strBuffer.append("batter");
			break;
		case BatteryManager.BATTERY_PLUGGED_AC:
			strBuffer.append("AC charger");
			break;
		case BatteryManager.BATTERY_PLUGGED_USB:
			strBuffer.append("USB charger");
			break;
		default:
			strBuffer.append("unkonow charger");
			break;
		}
		strBuffer.append("\n");
		strBuffer.append("present:" + batteryPresent);
		strBuffer.append("\n");
		strBuffer.append("status: ");
		switch (status) {
		case BatteryManager.BATTERY_STATUS_CHARGING:
			strBuffer.append("charging");
			break;
		case BatteryManager.BATTERY_STATUS_DISCHARGING:
			strBuffer.append("discharging");
			break;
		case BatteryManager.BATTERY_STATUS_FULL:
			strBuffer.append("full");
			break;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
			strBuffer.append("not charing");
			break;
		}
		strBuffer.append("\n");
		strBuffer.append("technology:" + technology);
		strBuffer.append("\n");
		strBuffer.append("温度： " + temperature + "\n");
		strBuffer.append("电压：" + voltage + "\n");
		strBuffer.append("剩余电量：" + String.valueOf(level * 100 / scale) + "%");
		return strBuffer.toString();
	}

}

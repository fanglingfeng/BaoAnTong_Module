package com.tjsoft.webhall.db;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if("android.alarm.demo.action".equals(intent.getAction())){
			System.out.println("fuchl   android.alarm.demo.action ");
		}
		
	}
	public boolean exist(String packgeName, Context context) {
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> list = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
		for (int i = 0; i < list.size(); i++) {
			if (packgeName.equals(list.get(i).packageName)) {
				return true;
			}

		}
		return false;
	}

}

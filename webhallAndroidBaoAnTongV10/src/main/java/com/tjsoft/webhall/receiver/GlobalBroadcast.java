package com.tjsoft.webhall.receiver;

import com.tjsoft.webhall.entity.TransportEntity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * 全局广播
 * @author Administrator
 *
 */
public class GlobalBroadcast extends BroadcastReceiver {

	private static final String TAG = GlobalBroadcast.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		TransportEntity transportEntity = (TransportEntity) intent.getSerializableExtra("TransportEntity");
		if (transportEntity != null) {
			Log.d(TAG, "----------------------------account=" + transportEntity.getName());
//			Log.d(TAG, "----------------------------LoginStatus=" + transportEntity.getLoginStatus());
			Log.d(TAG, "----------------------------OperateStatus=" + transportEntity.getToken());
//			Log.d(TAG, "----------------------------Password=" + transportEntity.getPassword());
		}
	}

}

package com.tjsoft.webhall;

import com.tjsoft.util.ThreadPoolManager;
import com.tjsoft.webhall.constants.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class AutoDialogActivity extends Activity {
	protected ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Constants.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);

	}


	@Override
	protected void onDestroy() {
		if(null != dialog){
			dialog.dismiss();
		}
		super.onDestroy();
	}
}

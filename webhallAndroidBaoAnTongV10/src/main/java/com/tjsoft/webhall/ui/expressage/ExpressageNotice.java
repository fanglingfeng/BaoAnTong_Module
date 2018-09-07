package com.tjsoft.webhall.ui.expressage;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;

/**
 * 邮递说明 com.tjsoft.webhall.ui.expressage.ExpressageAdd
 * 
 * @author 傅成龙 <br/>
 *         create at 2016-6-3 下午5:09:49
 */
public class ExpressageNotice extends AutoDialogActivity {
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "expressage_notice"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

}

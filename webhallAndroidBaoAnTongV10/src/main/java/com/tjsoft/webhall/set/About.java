package com.tjsoft.webhall.set;



import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 关于页面
 * @author Administrator
 *
 */
public class About extends AutoDialogActivity {
	private Button back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_about"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				About.this.finish();
			}
		});
	}
	
}

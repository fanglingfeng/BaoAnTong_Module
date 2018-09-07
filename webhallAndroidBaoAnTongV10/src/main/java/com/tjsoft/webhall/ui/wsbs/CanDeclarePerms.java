package com.tjsoft.webhall.ui.wsbs;

import android.os.Bundle;
import android.widget.GridView;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;

public class CanDeclarePerms extends AutoDialogActivity{
	private GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "can_declare_perms"));
		initView();
		
	}

	private void initView() {
		gridView = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "gridView"));
		
		
		
	}

}

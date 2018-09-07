package com.tjsoft.webhall.ui.work;

import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.tjsoft.util.FileUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.ui.bsdt.ReserveSubmit;

/**
 * 温馨提示
 * 
 * @author Administrator
 * 
 */
public class Notice extends AutoDialogActivity implements OnClickListener {
	private Button back;
	private Button ok;
	private Intent intent;
	private Permission permission;
	private String BSNUM;
	private String P_GROUP_ID;
	private String P_GROUP_NAME;
	private String mark;
	private CheckBox cb1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_notice"));

		permission = (Permission) getIntent().getSerializableExtra("permission");

		BSNUM = getIntent().getStringExtra("BSNUM");
		P_GROUP_ID = getIntent().getStringExtra("P_GROUP_ID");
		P_GROUP_NAME = getIntent().getStringExtra("P_GROUP_NAME");
		mark = getIntent().getStringExtra("mark");

		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "tj_back"));
		ok = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "tj_ok"));
		cb1 = (CheckBox) findViewById(MSFWResource.getResourseIdByName(this, "id", "cb1"));
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					FileUtil.Write(Notice.this, "notice", "1");
				} else {
					FileUtil.Write(Notice.this, "notice", "");
				}
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goNext();
			}
		});
		
		goNext();

//		if (!TextUtils.isEmpty(FileUtil.Load(Notice.this, "notice"))) {
//			goNext();
//		}

	}

	/**
	 * 
	 */
	protected void  goNext() {
		if (null != mark) {
			intent = new Intent();
			intent.putExtra("permission", permission);
			intent.putExtra("STATUS", -1);// 新申报
			intent.putExtra("BSNUM", BSNUM);
			intent.putExtra("P_GROUP_ID", P_GROUP_ID);
			intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
			intent.putExtra("mark", "6");
			intent.setClass(Notice.this, HistoreShareNoPre.class);
		} else {
			intent = new Intent();
			intent.setClass(Notice.this, ReserveSubmit.class);
			intent.putExtra("permission", permission);

		}
		startActivity(intent);
		finish();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	@Override
	public void onClick(View v) {
	}

}

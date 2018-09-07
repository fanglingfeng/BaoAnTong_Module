package com.tjsoft.webhall;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.tjsoft.util.FormFile;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.SocketHttpRequester;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class MainActivityTest extends AutoDialogActivity {
	private static final String TAG = "MainActivity";
	private Button test, select;
	private String uri = "";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_activity_main"));
		test = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "test"));
		select = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "select"));
		test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("fuchl path    "+Environment.getExternalStorageDirectory().getAbsolutePath());
						String str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/2014.jpg";
						File file = new File(str);
						uploadFile(file);
					}
				}).start();
			}
		});
		select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showFileChooser();
			}
		});
	}

	public void uploadFile(File imageFile) {
		Log.i(TAG, "upload start");
		try {
			String requestUrl = "http://192.9.207.178:8080/servlet/uploadMobileFileServlet";
			Map<String, String> params = new HashMap<String, String>();
			params.put("SXID", "100");
			params.put("FILENAME", "1.jpg");
			params.put("FILEID", "fileid");
			params.put("USERID", "userid");
			// 上传文件
			FormFile formfile = new FormFile(imageFile.getName(), imageFile, "image", "application/octet-stream");
			SocketHttpRequester.post(requestUrl, params, formfile);
			Log.i(TAG, "upload success");
		} catch (Exception e) {
			Log.i(TAG, "upload error");
			e.printStackTrace();
		}
		Log.i(TAG, "upload end");
	}

	protected String getAbsoluteImagePath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor managedQuery = managedQuery(uri, proj, null, null, null);
		Cursor cursor = managedQuery;

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uri = getAbsoluteImagePath(data.getData());
		if (null == data || null == data.getData()) {
		} else {
			System.out.println(getAbsoluteImagePath(data.getData()));
		}
	}

	/** 调用文件选择软件来选择文件 **/
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"), 1);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivityTest.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
		}
	}

}

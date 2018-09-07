package com.tjsoft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.Version;

public class UpdateManager {

	private Context mContext;
	private boolean auto = true;


	private Dialog noticeDialog;

	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = "/sdcard/tjsoft/";

	private static final String saveFileName = savePath + "webhall.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;
	private static final int SHOW_UPDATE = 3;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Version version;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			case SHOW_UPDATE:
				showNoticeDialog();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context, Boolean auto) {
		this.mContext = context;
		this.auto = auto;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo() {
		new Thread(GetFormByBsNo).start();
	}

	final Runnable GetFormByBsNo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("APPTYPE", "1");
				String response = HTTP.excute("getLatestVersion", "RestMobileVersionService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					version = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), Version.class);
					System.out.println("fuchl      "+version.getLATESTVERSION()+"                    "+GetClientVersion(mContext));
					if (null != version && null != version.getLATESTVERSION() && !version.getLATESTVERSION().equals(GetClientVersion(mContext))) {
						
						mHandler.sendEmptyMessage(SHOW_UPDATE);
					} else {
						if (!auto) {
							DialogUtil.showUIToast(mContext, "当前已经是最新版本");
						}
					}
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	public static String GetClientVersion(Context context) {
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if (i.versionName != null) {
				return i.versionName;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(version.getDESCRIPTION());
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_progress"), null);
		mProgress = (ProgressBar) v.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "progress"));

		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(version.getURL());

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);

	}
}

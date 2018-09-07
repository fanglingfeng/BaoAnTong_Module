package com.tjsoft.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件下载管理
 * @author S_Black
 *
 */
public class DownloadManager {
	private Context mContext;
	
	private Dialog noticeDialog;

	private Dialog downloadDialog;
	private String URL="";//下载地址
	/* 下载文件保存路径 */
	private static final String savePath = "/sdcard/tjsoft/";

	private String saveFileName ="";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;
	private static final int SHOW_UPDATE = 3;
	private static final int SHOW_DOWN_PROGRESS=4;
	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;


	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				//打开文件
				openFile();
				downloadDialog.dismiss();
				break;
			case SHOW_UPDATE:
				showNoticeDialog();
				break;
			case SHOW_DOWN_PROGRESS:
				showDownloadDialog();
				break;
			default:
				break;
			}
		};
	};

	public DownloadManager(Context context, String url,String fileName) {
		this.mContext = context;		
		this.URL=url;
		this.saveFileName=fileName;
	}

	// 外部接口让主Activity调用
	public void checkFileIsExists() {
		File file=new File(savePath+saveFileName);
		if(file.exists()){
			Log.e("sps", "文件存在了！！！！！");
			openFile();
		}else{
			if(NetworkUtils.isAvailable(mContext)){
				if(NetworkUtils.isWifiConnected(mContext)){
					mHandler.sendEmptyMessage(SHOW_UPDATE);
				}else{
					new AlertDialog.Builder(mContext).setMessage("您当前不是Wifi环境，是否下载？").setTitle(mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mHandler.sendEmptyMessage(SHOW_DOWN_PROGRESS);
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					}).show();
				}			
			}else{
				DialogUtil.showUIToast(mContext, "当前无可用网络！");
			}
			
			
		}

	}



	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("提示");
		builder.setMessage("是否下载此文件？");
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
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
		builder.setTitle("文件下载");

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

		downloadFile();
	}

	private Runnable mdownFileRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(URL);
				Log.e("sps", "url==========="+URL);
				Log.e("sps", "filename==========="+saveFileName);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String Filepath = savePath+saveFileName;
				File FileName = new File(Filepath);
				FileOutputStream fos = new FileOutputStream(FileName);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知打开
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				if(interceptFlag){
					deleteFile();
				}
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
	 * 下载文件
	 * 
	 */

	private void downloadFile() {
		downLoadThread = new Thread(mdownFileRunnable);
		downLoadThread.start();
	}
	/**
	 * 打开文件
	 */
	private void openFile(){
		Intent intent=FileUtil.openFile(savePath+saveFileName,null);
		mContext.startActivity(intent);
	}
	/**
	 * 文件删除
	 */
	private void deleteFile(){
		Log.e("sps", "删除文件！");
		File file = new File(savePath+saveFileName);
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

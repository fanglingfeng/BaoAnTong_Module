package com.tjsoft.util;




import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class Background {
	
	public static ProgressDialog Process(Context context,final Runnable run){
		return Process(context,run,false,null);
	}
		
	public static ProgressDialog Process(Context context,final Runnable run,String loadingComment){
		return Process(context,run,true,loadingComment);
	}
	public static ProgressDialog SingleProcess(Context context,final Runnable run,String loadingComment){
		return SingleProcess(context,run,true,loadingComment);
	}
	
	@SuppressWarnings("deprecation")
	public static ProgressDialog Process(Context context, final Runnable run, final boolean showDialog,
			String loadingComment) {

		ProgressDialog progressdialog = DialogUtil.creativeProgressBar(context, loadingComment);
		progressdialog.setCancelable(true);
		progressdialog.setCanceledOnTouchOutside(false);

		final ProgressDialog progressdialog2 = progressdialog;

		try {
			if (showDialog)
				progressdialog.show();

			Runnable wrapper = new Runnable() {
				public void run() {
					run.run();
					try {
						if (showDialog) {
							if (progressdialog2 != null)
								progressdialog2.dismiss();
						}
					} catch (Exception e) {
						Log.d("TAG", e.getMessage());
					}
				}
			};
			ThreadPoolManager.getInstance().addAsyncTask(wrapper);
//			Thread thread = new Thread(wrapper);
//			thread.setDaemon(true);
//			thread.start();
		} catch (Exception ex) {
			return null;
		}

		return progressdialog;
	}
	
	@SuppressWarnings("deprecation")
	public static ProgressDialog SingleProcess(Context context, final Runnable run, final boolean showDialog,
			String loadingComment) {

		ProgressDialog progressdialog = DialogUtil.creativeProgressBar(context, loadingComment);
		progressdialog.setCancelable(true);
		progressdialog.setCanceledOnTouchOutside(false);

		final ProgressDialog progressdialog2 = progressdialog;

		try {
			if (showDialog)
				progressdialog.show();
				
			Runnable wrapper = new Runnable() {
				public void run() {
					run.run();
					try {
						if (showDialog) {
							if (progressdialog2 != null)
								progressdialog2.dismiss();
						}
					} catch (Exception e) {
						Log.d("TAG", e.getMessage());
					}
				}
			};

			ThreadPoolManager.getInstance().addSingleAsyncTask(wrapper);
//			Thread thread = new Thread(wrapper);
//			thread.setDaemon(true);
//			thread.start();
		} catch (Exception ex) {
			return null;
		}

		return progressdialog;
	}

	
	
	public static ProgressDialog Process2(Context context,final Runnable run,final boolean showDialog,String loadingComment) {
		
		final ProgressDialog progressdialog = DialogUtil.creativeProgressBar(context,loadingComment);

		
			try {		
			
				if(showDialog) progressdialog.show();
				
				Runnable wrapper =new Runnable() {
					public void run() {
						run.run();
						if(showDialog)
							progressdialog.dismiss();
					}
				};
				Thread thread = new Thread(wrapper);
				thread.setDaemon(true);
				thread.start();
				
			} catch(Exception ex) {
			}

		return progressdialog;
	}	

}

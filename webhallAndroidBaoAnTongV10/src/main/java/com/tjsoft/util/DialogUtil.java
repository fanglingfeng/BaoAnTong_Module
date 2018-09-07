package com.tjsoft.util;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
public class DialogUtil {
	
	public static void showToast(Context context,String title){
		Toast toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
		toast.setGravity( Gravity.CENTER , 0, 0 );
		toast.show();
	}
	public static void showUIToast(final Context context ,final String title){
			((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
				toast.setGravity( Gravity.CENTER , 0, 0 );
				toast.show();
				
			}
		});
	}
	public static void showToast( Context context,View view){
		 Toast toast = new Toast(context);
	        toast.setView(view);
	        toast.setGravity( Gravity.CENTER , 0, 0 );
	        toast.setDuration(Toast.LENGTH_LONG);
	        toast.show();
	}
	
	public static void showToast(Context context,String title, boolean flag)
	{
		int len = Toast.LENGTH_SHORT;
		
		if (true == flag)
		{
			len = Toast.LENGTH_LONG;
		}
		
		Toast toast = Toast.makeText(context, title, len);		
		toast.show();
	}
	
	public static void showMyToast(Context context, String msg)
	{
		Toast toast = new Toast(context);
		TextView view = new TextView(context);
		view.setBackgroundColor(0x00ffffff);
		view.setTextColor(0xffff0000);
		view.setText(msg);
		view.getPaint().setFakeBoldText(true);
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		toast.setDuration(Toast.LENGTH_LONG);	
		toast.setView(view);
		toast.show();
	}
	

	public static AlertDialog.Builder creativeAlertDialog(Context context){
		AlertDialog.Builder alert = new AlertDialog.Builder(context);  
		return alert;
	}	
	
	public static  ProgressDialog creativeProgressBar(Context context){
        return creativeProgressBar(context,null);
	}
	public static  ProgressDialog creativeProgressBar(Context context,String comment){
		ProgressDialog dialog = new ProgressDialog(context);
		if(comment==null)
			dialog.setMessage("Please wait while loading...");
		else
			dialog.setMessage(comment);
			
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		return dialog;
	}

}

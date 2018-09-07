package com.tjsoft.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtil {
	// private final static String TAG="FileHandler";
	// private final static String
	// PATH="/data/data/"+Constants.PACKAGE_NAME+"/files/";
	public static File getSaveFile(Context context) {
		File file = new File(context.getFilesDir(), "pic.jpg");
		return file;
	}

	public static boolean DownloadImage(Context context, String Url,
			String FileName) {

		URL imageurl;
		int Read;

		try {
			File file = new File(context.getFilesDir().getAbsolutePath() + "/"
					+ FileName);
			if (!file.exists()) {
				imageurl = new URL(Url);

				HttpURLConnection conn = (HttpURLConnection) imageurl
						.openConnection();

				int len = conn.getContentLength();
				byte[] raster = new byte[len];

				InputStream is = conn.getInputStream();
				FileOutputStream fos = context.openFileOutput(FileName, 0);

				for (;;) {
					Read = is.read(raster);
					if (Read <= 0) {
						break;
					}
					fos.write(raster, 0, Read);
				}

				is.close();
				fos.close();
				conn.disconnect();
			}
		} catch (Exception e) {
			return false;
		}
		return true;

	}
	public static void saveDownloadFile(InputStream inputStream, File file) {
		FileOutputStream fos = null;
		if (inputStream != null) {
			try {
				fos = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int slice;
				while ((slice = inputStream.read(buf)) != -1) {
					fos.write(buf, 0, slice);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static Bitmap getBitmap(Context context, String FileName) {

		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeFile(context.getFilesDir()
					.getAbsolutePath() + "/" + FileName);
		} catch (Exception e) {
		}

		return bitmap;
	}
	
	public static Bitmap getBitmapByPath(Context context, String PathName) {

		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeFile(PathName);
		} catch (Exception e) {
		}

		return bitmap;
	}

	public static int Write(Context context, String filename, String data) {
		try {
			FileOutputStream fos = context.openFileOutput(filename,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
		} catch (Exception e) {
			return 0;
		}

		return 1;
	}

	public static String Load(Context context, String filename) {
		String szresult = "";

		try {
			FileInputStream fis = context.openFileInput(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			szresult = (String) ois.readObject();
			ois.close();
		} catch (Exception e) {
		}

		return szresult;
	}
	public static double LoadInt(Context context, String filename) {
		String szresult = "";

		try {
			FileInputStream fis = context.openFileInput(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			szresult = (String) ois.readObject();
			ois.close();
			if(null==szresult||szresult.equals("")){
				szresult = 0+"";
			}
		} catch (Exception e) {
			szresult = 0+"";
		}

		return Double.parseDouble(szresult.trim());
	}

	public static long getFileSize(Context context, String FileName) {

		File file = new File(context.getFilesDir().getAbsolutePath() + "/"
				+ FileName);

		try {
			if (file.exists()) {
				return file.length();
			}
		} catch (Exception e) {
		}

		return 0;

	}

	public static String getFromAssets(Context context, String fileName) throws IOException {

		InputStreamReader inputReader = new InputStreamReader(context
				.getResources().getAssets().open(fileName));
		BufferedReader bufReader = new BufferedReader(inputReader);
		String line = "";
		String Result = "";
		while ((line = bufReader.readLine()) != null) {
			Result += line;
		}
		return Result;

	}

	public static boolean FileDelete(Context context, String FileName) {
		File file = new File(context.getFilesDir().getAbsolutePath() + "/"
				+ FileName);

		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public void saveToSDCard(String name, String content) {

		 FileOutputStream fos = null;
		 
		try{
			
			//Environment.getExternalStorageDirectory()。获取sd卡的路径
		File file = new File(Environment.getExternalStorageDirectory(),name);
	    fos = new FileOutputStream(file);
	    
	    fos.write(content.getBytes());
		}catch(Exception e){
			
			e.printStackTrace();
			
		}finally{
			
			
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setRequestProperty("Accept-Encoding", "identity");
//			conn.setRequestProperty(field, newValue);
			conn.setDoInput(true);
			conn.connect();
			int length = conn.getContentLength();
			System.out.println("--------------length--------------" + length);
			InputStream is = conn.getInputStream();
			// bitmap = BitmapFactory.decodeStream(is);
			BitmapFactory.Options options = new Options();
			options.inDither = false; /* 不进行图片抖动处理 */
			options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
			options.inSampleSize = 4; /* 图片长宽方向缩小倍数 */
			bitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
	 * 打开文件
	 * @param filePath
	 * @return
	 */
	public static Intent openFile(String filePath,Context context){
		File file = new File(filePath);
		if(!file.exists()) return null;
		/* 取得扩展名 */
		String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase(); 
		/* 依扩展名的类型决定MimeType */
		if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
				end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
			return getAudioFileIntent(filePath);
		}else if(end.equals("3gp")||end.equals("mp4")){
			return getAudioFileIntent(filePath);
		}else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
				end.equals("jpeg")||end.equals("bmp")){
			return getImageFileIntent(filePath);
		}else if(end.equals("apk")){
			return getApkFileIntent(filePath);
		}else if(end.equals("ppt")){
			return getPptFileIntent(filePath);
		}else if(end.equals("xls")|| "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end)){
			return getExcelFileIntent(filePath);
		}else if(end.equals("doc")||end.equals("docx")|| "docm".equals(end) || "dotx".equals(end) || "dotx".equals(end) || "dotm".equals(end)){
			return getWordFileIntent(filePath);
		}else if(end.equals("pdf")){
			return getPdfFileIntent(filePath,context);
		}else if(end.equals("chm")){
			return getChmFileIntent(filePath);
		}else if(end.equals("txt")){
			return getTextFileIntent(filePath,false);
		}else{
			return getAllIntent(filePath);
		}
	}
	
	//Android获取一个用于打开APK文件的intent
	public static Intent getAllIntent( String param ) {

		Intent intent = new Intent();  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"*/*"); 
		return intent;
	}
	//Android获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent( String param ) {

		Intent intent = new Intent();  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/vnd.android.package-archive"); 
		return intent;
	}

	//Android获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	//Android获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent( String param ){

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	//Android获取一个用于打开Html文件的intent   
	public static Intent getHtmlFileIntent( String param ){

		Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	//Android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	//Android获取一个用于打开PPT文件的intent   
	public static Intent getPptFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");   
		return intent;   
	}   

	//Android获取一个用于打开Excel文件的intent   
	public static Intent getExcelFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-excel");   
		return intent;   
	}   

	//Android获取一个用于打开Word文件的intent   
	public static Intent getWordFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/msword");   
		return intent;   
	}   

	//Android获取一个用于打开CHM文件的intent   
	public static Intent getChmFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/x-chm");   
		return intent;   
	}   

	//Android获取一个用于打开文本文件的intent   
	public static Intent getTextFileIntent( String param, boolean paramBoolean){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		if (paramBoolean){   
			Uri uri1 = Uri.parse(param );   
			intent.setDataAndType(uri1, "text/plain");   
		}else{   
			Uri uri2 = Uri.fromFile(new File(param ));   
			intent.setDataAndType(uri2, "text/plain");   
		}   
		return intent;   
	}  
	//Android获取一个用于打开PDF文件的intent   
	public static Intent getPdfFileIntent( String param ,Context context){

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri ;

		if (Build.VERSION.SDK_INT >= 24) {
			String packageName = context.getPackageName();
			uri  = FileProvider.getUriForFile(context.getApplicationContext(), packageName +".fileProvider",new File(param ));
		} else {
			uri = Uri.fromFile(new File(param ));
		}


		intent.setDataAndType(uri, "application/pdf");
		return intent;   
	}
	
	
	
	
}

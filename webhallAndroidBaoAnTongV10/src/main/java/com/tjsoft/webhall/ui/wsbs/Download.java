package com.tjsoft.webhall.ui.wsbs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileCache;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.lib.PhotoView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 文件下载
 * 
 * @author yifan
 * @version 1.0
 * @date 2014-08-13
 * 
 */
public class Download extends Activity {

	private static final int WHAT = 1; // 更新进度条进度
	private Button back;
	private TextView title;
	private PhotoView image;
	private ProgressBar progress; // 进度条
	private int fileSize;
	private String fileName;
	private TextView filePath; // 文件路径
	private Bitmap bitmap; // 图片文件
	private int RID; // 资源id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_download"));
		Constants.getInstance().addActivity(this);

		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
		title.setText("文件下载");
		filePath = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "file_path"));
		image = (PhotoView) findViewById(MSFWResource.getResourseIdByName(this, "id", "image"));
		progress = (ProgressBar) findViewById(MSFWResource.getResourseIdByName(this, "id", "progress"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		new Thread().start();
		preView();

	}

	private void preView() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean isExist = returnBitMap(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + getIntent().getStringExtra("FILEID"));
				if (isExist) {
					final File file = new FileCache(Download.this).getByFileName(fileName);
					/* 取得扩展名 */
					String end = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase(Locale.US);
					if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(end)) {
						BitmapFactory.Options options = new Options();
						options.inDither = false; /* 不进行图片抖动处理 */
						options.inPreferredConfig = null; /* 设置让解码器以最佳方式解码 */
						bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

					} else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(end) || "dotx".equals(end) || "dotm".equals(end)) {
						RID = MSFWResource.getResourseIdByName(Download.this, "drawable", "tj_ic_word"); // word文档文件
					} else if ("pdf".equals(end)) {
						RID = MSFWResource.getResourseIdByName(Download.this, "drawable", "tj_ic_pdf"); // pdf文件
					} else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end)) {
						RID = MSFWResource.getResourseIdByName(Download.this, "drawable", "tj_ic_excel"); // excel文件
					} else {
						RID = MSFWResource.getResourseIdByName(Download.this, "drawable", "tj_ic_file_unknown");
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (bitmap != null) {
								image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
								image.setImageBitmap(bitmap);
							} else {
								image.setImageResource(RID);
							}
							filePath.setText("文件所在路径：" + file.getAbsolutePath());
							// download.setVisibility(View.INVISIBLE);

						}
					});
				} else {
					DialogUtil.showUIToast(Download.this, "文件不存在");
				}

			}
		}).start();

	}

	/**
	 * 根据url获取图片
	 * 
	 * @param url
	 *            访问的url
	 * @return 要显示的图片
	 */
	public boolean returnBitMap(String url) {
		boolean isExist = false;
		URL myFileUrl = null;
		String filename = "";
		boolean isok = false;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(true);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			final int length = conn.getContentLength();

			if (length <= 0) {
				return isExist;
			}

			// 获取文件的名称
			Map<String, List<String>> hf = conn.getHeaderFields();
			if (hf == null) {
				return isExist;
			}
			Set<String> key = hf.keySet();
			if (key == null) {
				return isExist;
			}
			for (String skey : key) {
				List<String> values = hf.get(skey);
				for (String value : values) {
					String result;
					try {
						result = new String(value.getBytes("ISO-8859-1"), "GBK");
						int location = result.indexOf("filename");
						if (location >= 0) {
							result = result.substring(location + "filename".length());
							filename = result.substring(result.indexOf("=") + 2, result.length() - 1);
							isok = true;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if (isok) {
					break;
				}
			}

			fileName = filename;
			File file = new FileCache(Download.this).getByFileName(fileName);
			// 如果存在直接返回
			if (file.length() > 0) {
				return true;
			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					progress.setMax(length);
					progress.setVisibility(View.VISIBLE);
				}
			});

			OutputStream os = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				fileSize += len;
				handler.sendEmptyMessage(WHAT);
			}
			isExist = true;
			is.close();
			os.close();
			return isExist;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	// 更新进度条
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progress.setProgress(fileSize);
			if (progress.getMax() == fileSize) {
				progress.setVisibility(View.INVISIBLE);
				
			}
		}
	};
}

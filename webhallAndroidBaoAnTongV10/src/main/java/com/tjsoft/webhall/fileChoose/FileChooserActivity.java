package com.tjsoft.webhall.fileChoose;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FormFile;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.NetworkUtils;
import com.tjsoft.util.SocketHttpRequester;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.fileChoose.FileChooserAdapter.FileInfo;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 文件选择页面
 * @author Administrator
 *
 */
public class FileChooserActivity extends AutoDialogActivity {

	private GridView mGridView;
	private View mBackView;
	private View mBtExit;
	private TextView mTvPath;

	private String mSdcardRootPath; // sdcard 根路径
	private String mLastFilePath; // 当前显示的路径
	private String PERMID;
	private String CLMC;
	private ApplyBean FILE;

	private ArrayList<FileInfo> mFileLists;
	private FileChooserAdapter mAdatper;
	private String TYPE;
	private int position = -1;
	private String idString = "";
	private String startTimeString = "";
	private String endTimeString = "";
	private SharedPreferences sp;


	protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_filechooser_show"));
		idString = getIntent().getStringExtra("idString");
		startTimeString = getIntent().getStringExtra("startTimeString");
		endTimeString = getIntent().getStringExtra("endTimeString");
		PERMID = getIntent().getStringExtra("PERMID");
		CLMC = getIntent().getStringExtra("CLMC");
		TYPE = getIntent().getStringExtra("TYPE");
		FILE = (ApplyBean) getIntent().getSerializableExtra("FILE");
		position = getIntent().getIntExtra("position", -1);

		mSdcardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		sp=this.getSharedPreferences("File", MODE_PRIVATE);
		String path=sp.getString("filePath", mSdcardRootPath);
		mBackView = findViewById(MSFWResource.getResourseIdByName(this, "id", "imgBackFolder"));
		mBtExit = findViewById(MSFWResource.getResourseIdByName(this, "id", "btExit"));

		mTvPath = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "tvPath"));

		mGridView = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "gvFileChooser"));
		mGridView.setEmptyView(findViewById(MSFWResource.getResourseIdByName(this, "id", "tvEmptyHint")));
		mGridView.setOnItemClickListener(mItemClickListener);
		setGridViewAdapter(path);
		setOnListener();
	}

	private void setOnListener() {
		mBackView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				backProcess();
			}
		});
		mBtExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	// 配置适配器
	private void setGridViewAdapter(String filePath) {
		updateFileItems(filePath);
		mAdatper = new FileChooserAdapter(this, mFileLists,mGridView);
		mGridView.setAdapter(mAdatper);
	}

	// 根据路径更新数据，并且通知Adatper数据改变
    
	private void updateFileItems(String filePath) {
		mLastFilePath = filePath;
		mTvPath.setText(mLastFilePath);

		if (mFileLists == null)
			mFileLists = new ArrayList<FileInfo>();
		if (!mFileLists.isEmpty())
			mFileLists.clear();

		File[] files = folderScan(filePath);
		if (files == null)
			return;

		for (int i = 0; i < files.length; i++) {
			if (files[i].isHidden()) // 不显示隐藏文件
				continue;

			String fileAbsolutePath = files[i].getAbsolutePath();
			String fileName = files[i].getName();
			boolean isDirectory = false;
			if (files[i].isDirectory()) {
				isDirectory = true;
			}
			FileInfo fileInfo = new FileInfo(fileAbsolutePath, fileName, isDirectory);
			mFileLists.add(fileInfo);
		}
		// When first enter , the object of mAdatper don't initialized
		if (mAdatper != null)
			mAdatper.notifyDataSetChanged(); // 重新刷新
	}

	// 获得当前路径的所有文件

	private File[] folderScan(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		return files;
	}



	// 上传
	public void uploadFile(File file) {
		try {
			String requestUrl = Constants.DOMAIN + "servlet/uploadMobileFileServlet";
			Map<String, String> params = new HashMap<String, String>();
			params.put("USERCODE", Constants.user.getCODE());
			params.put("CERTCODE", idString);
			params.put("CERTSTARTTIME", startTimeString);
			params.put("CERTENDTIME", endTimeString);
			params.put("TYPE", TYPE);//证照1 ,附件2
			if(null != FILE){
				params.put("ATTACHCODE", FILE.getCLBH());
				params.put("ATTACHNAME", FILE.getCLMC());
			}
			params.put("SXID", PERMID);
			params.put("FILENAME", file.getName());
			if (null == FILE.getFILEID()) {
				params.put("FILEID", "");
			} else {
				params.put("FILEID", FILE.getFILEID());
			}
			params.put("USERID", Constants.user.getUSER_ID());
			// 上传文件
			FormFile formfile = new FormFile(file.getName(), file, "image", "application/octet-stream");
			String json = SocketHttpRequester.post(requestUrl, params, formfile);

			String fileId = new JSONObject(new JSONObject(json).getString("ReturnValue")).getString("FILEID");
			String filePath = new JSONObject(new JSONObject(json).getString("ReturnValue")).getString("FILEPATH");
			if (null != fileId && !fileId.equals("") && null != filePath && !filePath.equals("")) {
				DialogUtil.showUIToast(this, "上传文件" + file.getName() + "成功");
				
				ATTBean att = new ATTBean(fileId, file.getName(), filePath,"");
				if (null == Constants.material.get(FILE.getCLBH())) {
					Constants.material.put(FILE.getCLBH(), new ArrayList<ATTBean>());
				}
				Constants.material.get(FILE.getCLBH()).add(att);// 将新增文件添加到map

				if (position != -1 && TYPE.equals("1")) {// 证照信息数据状态改变
//					HistoreShare_v2.licenseDate.get(position).setSTATUS("1");

				} else if (position != -1 && TYPE.equals("2")) {// 大附件数据状态改变
					HistoreShare_v2.bigFileDate.get(position).setSTATUS("1");
				}
				Editor editor = sp.edit();
				editor.putString("filePath", mLastFilePath);
				editor.commit();
				
				Intent intent = getIntent();
				Bundle bundle = new Bundle();				
				bundle.putSerializable("apply", FILE);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
//				setResult(RESULT_OK);			
				finish();
			} else {
				DialogUtil.showUIToast(this, "上传文件" + file.getName() + "失败，请确保网络环境良好重试，或者到电脑上传！");
			}
		} catch (Exception e) {
			DialogUtil.showUIToast(this, "上传文件" + file.getName() + "失败，请确保网络环境良好重试，或者到电脑上传！");
			e.printStackTrace();
		}
	}

	private AdapterView.OnItemClickListener mItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			FileInfo fileInfo = (FileInfo) (((FileChooserAdapter) adapterView.getAdapter()).getItem(position));
			if (fileInfo.isDirectory()) { // 点击项为文件夹, 显示该文件夹下所有文件
				updateFileItems(fileInfo.getFilePath());
			}

			else if (fileInfo.isPPTFile() || fileInfo.isExcelFile() || fileInfo.isPdfFile() || fileInfo.isPICFile() || fileInfo.isWordFile()) { // 可上传文件.....
				final File file = new File(fileInfo.getFilePath());
				if (file.length() > 20*1024*1024) {
					DialogUtil.showUIToast(FileChooserActivity.this, "文件大小大于20M，暂不支持上传。请重新选择文件！");
					return;
				}
				if(NetworkUtils.isAvailable(FileChooserActivity.this)){
					if(NetworkUtils.isWifiConnected(FileChooserActivity.this)){
						new AlertDialog.Builder(FileChooserActivity.this).setMessage("是否确定上传？").setTitle(getString(MSFWResource.getResourseIdByName(FileChooserActivity.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog = Background.Process(FileChooserActivity.this, new Runnable() {
									public void run() {
										uploadFile(file);
									}
								}, "正在上传...");
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).show();
					}else{
						new AlertDialog.Builder(FileChooserActivity.this).setMessage("当前不是Wifi环境，是否确定上传？").setTitle(getString(MSFWResource.getResourseIdByName(FileChooserActivity.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog = Background.Process(FileChooserActivity.this, new Runnable() {
									public void run() {
										uploadFile(file);
									}
								}, "正在上传...");
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).show();
					}
				}else {
					DialogUtil.showUIToast(FileChooserActivity.this, "当前无可用网络！");
				}
				

			} else {
				DialogUtil.showUIToast(FileChooserActivity.this, "不支持上传的文件类型！");
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			backProcess();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 返回上一层目录的操作
	public void backProcess() {
		// 判断当前路径是不是sdcard路径 ， 如果不是，则返回到上一层。
		if (!mLastFilePath.equals(mSdcardRootPath)) {
			File thisFile = new File(mLastFilePath);
			String parentFilePath = thisFile.getParent();
			updateFileItems(parentFilePath);
		} else { // 是sdcard路径 ，直接结束
			Editor editor = sp.edit();
			editor.putString("filePath", mSdcardRootPath);
			editor.commit();
			setResult(RESULT_CANCELED);
			finish();
		}
	}

}
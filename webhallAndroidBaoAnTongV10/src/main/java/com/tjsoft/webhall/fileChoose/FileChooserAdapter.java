package com.tjsoft.webhall.fileChoose;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.BitmapUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.MemoryCache;
import com.tjsoft.webhall.entity.News;

/**
 * 文件选择器适配器
 * @author Administrator
 *
 */
public class FileChooserAdapter extends BaseAdapter {

	private ArrayList<FileInfo> mFileLists;
    private GridView gridView;  
    private int scrollStauts=0;  
    public static MemoryCache cache=new MemoryCache();
	private LayoutInflater mLayoutInflater = null;
	private Context mContext;
    private static DisplayImageOptions options = new DisplayImageOptions.Builder()  
            .showImageForEmptyUri(R.drawable.tj_ic_pic) // image连接地址为空时  
            .showImageOnFail(R.drawable.tj_ic_pic) // image加载失败  
            .cacheInMemory(true) // 加载图片时会在内存中加载缓存  
            .cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存  
             .displayer(new FadeInBitmapDisplayer(500))
             .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//            .displayer(new RoundedBitmapDisplayer(20)) // 设置用户加载图片task(这里是圆角图片显示)  
            .build();  
	private static ArrayList<String> PPT_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> PIC_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> WORD_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> EXCEL_SUFFIX = new ArrayList<String>();
	private static ArrayList<String> PDF_SUFFIX = new ArrayList<String>();





	static {
		PIC_SUFFIX.add(".jpg");
		PIC_SUFFIX.add(".JPG");
		PIC_SUFFIX.add(".png");
		PIC_SUFFIX.add(".jpeg");
		WORD_SUFFIX.add(".doc");
		WORD_SUFFIX.add(".docx");
		WORD_SUFFIX.add(".docm");
		WORD_SUFFIX.add(".dotx");
		WORD_SUFFIX.add(".dotm");
		EXCEL_SUFFIX.add(".xls");
		EXCEL_SUFFIX.add(".xlsx");
		EXCEL_SUFFIX.add(".xlsm");
		EXCEL_SUFFIX.add(".xltx");
		EXCEL_SUFFIX.add(".xltm");
		EXCEL_SUFFIX.add(".xlsb");
		EXCEL_SUFFIX.add(".xlam");
		PDF_SUFFIX.add(".pdf");
	}

	public FileChooserAdapter(Context context, ArrayList<FileInfo> fileLists,GridView gridView) {
		super();
		mFileLists = fileLists;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		this.gridView=gridView;
		this.gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止  0  
                    scrollStauts=0;  
//                    ImageLoader.getInstance().resume();
                    updateUI();  
//                    System.out.println("停止");  
                    break;  
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸滑动  1  
                    scrollStauts=1;  
//                    ImageLoader.getInstance().pause();
//                    System.out.println("触摸滑动");  
                    break;  
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING://快速滑动    2  
                    scrollStauts=2;  
//                    ImageLoader.getInstance().pause();
//                    System.out.println("快速滑动");  
                    break;  
                default:  
                    break;                   
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
    public void updateUI() {  
        this.notifyDataSetChanged();  
    }  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFileLists.size();
	}

	@Override
	public FileInfo getItem(int position) {
		// TODO Auto-generated method stub
		return mFileLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null ) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_filechooser_gridview_item"),
					null);
			holder.imgFileIcon = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "imgFileIcon"));
			holder.tvFileName = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "tvFileName"));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FileInfo fileInfo = getItem(position);
        //TODO 
		
		holder.tvFileName.setText(fileInfo.getFileName());
			
		if(fileInfo.isDirectory()){      //文件夹
			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_folder"));
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		else if(fileInfo.isPPTFile()){   //PPT文件
			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_ppt"));
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		else if(fileInfo.isPICFile()){ // Pic文件
//			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_pic"));
			holder.tvFileName.setTextColor(Color.GRAY);
//			ImageLoader.getInstance().displayImage("file://"+fileInfo.filePath, holder.imgFileIcon);
			Bitmap myBitmap=cache.get(fileInfo.filePath);
			if (scrollStauts == 0) {				
				if(myBitmap!=null){
					holder.imgFileIcon.setImageBitmap(myBitmap);
					holder.tvFileName.setTextColor(Color.GRAY);
				}else{
					Bitmap bitmap = getImageThumbnail(mContext, mContext.getContentResolver(), fileInfo.getFilePath());
					cache.put(fileInfo.filePath, bitmap);
					holder.imgFileIcon.setImageBitmap(bitmap);
					holder.tvFileName.setTextColor(Color.GRAY);
				}				
				// ImageSize imageSize=new ImageSize(80, 80);
				// bitmap=ImageLoader.getInstance().loadImageSync("file://"+fileInfo.getFilePath(),imageSize,options);
				//// holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext,	
//				 ImageLoader.getInstance().displayImage("file://"+fileInfo.filePath,
//				 holder.imgFileIcon,options);
//				 holder.tvFileName.setTextColor(Color.GRAY);
				
			} else {
				if(myBitmap!=null){
					holder.imgFileIcon.setImageBitmap(myBitmap);
					holder.tvFileName.setTextColor(Color.GRAY);
				}else{
					holder.imgFileIcon
					.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "ti_pic_default"));
					holder.tvFileName.setTextColor(Color.GRAY);
				}
				
			}
		}
		else if(fileInfo.isWordFile()){   //word文件
			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_word"));
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		else if(fileInfo.isExcelFile()){   //excel文件
			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_excel"));
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		else if(fileInfo.isPdfFile()){   //pdf文件
			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_pdf"));
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		else {                           //未知文件
			holder.imgFileIcon.setImageResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_file_unknown"));
			holder.tvFileName.setTextColor(Color.GRAY);
		}
		
		return convertView;
	}

	static class ViewHolder {
		ImageView imgFileIcon;
		TextView tvFileName;

//		public ViewHolder(View view) {
//			imgFileIcon = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "imgFileIcon")R.id.imgFileIcon);
//			tvFileName = (TextView) view.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "tvFileName")R.id.tvFileName);
//		}
	}

	
	enum FileType {
		FILE , DIRECTORY;
	}

	// =========================
	// Model
	// =========================
	static class FileInfo {
		private FileType fileType;
		private String fileName;
		private String filePath;

		public FileInfo(String filePath, String fileName, boolean isDirectory) {
			this.filePath = filePath;
			this.fileName = fileName;
			fileType = isDirectory ? FileType.DIRECTORY : FileType.FILE;
		}

		public boolean isPPTFile(){
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && PPT_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}
		public boolean isPICFile(){
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && PIC_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}
		public boolean isWordFile(){
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && WORD_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}
		public boolean isExcelFile(){
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && EXCEL_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}
		public boolean isPdfFile(){
			if(fileName.lastIndexOf(".") < 0)  //Don't have the suffix 
				return false ;
			String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
			if(!isDirectory() && PDF_SUFFIX.contains(fileSuffix))
				return true ;
			else
				return false ;
		}
  
		public boolean isDirectory(){
			if(fileType == FileType.DIRECTORY)
				return true ;
			else
				return false ;
		}
		
		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		@Override
		public String toString() {
			return "FileInfo [fileType=" + fileType + ", fileName=" + fileName
					+ ", filePath=" + filePath + "]";
		}
	}

	/** 
	    *  
	    * @param context 
	    * @param cr
	    * @param Imagepath 
	    * @return 
	    */ 
	   public static Bitmap getImageThumbnail(Context context, ContentResolver cr, String Imagepath) {  
	           ContentResolver testcr = context.getContentResolver();  
	           String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, };  
	           String whereClause = MediaStore.Images.Media.DATA + " = '" + Imagepath + "'";  
	           Cursor cursor = testcr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,  
	                           null, null);  
	           int _id = 0;  
	           String imagePath = "";  
	           if (cursor == null || cursor.getCount() == 0) {  
	                   return null;  
	           }  
	           if (cursor.moveToFirst()) {  

	                   int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);  
	                   int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);  

	                   do {  
	                           _id = cursor.getInt(_idColumn);  
	                           imagePath = cursor.getString(_dataColumn);  
	                   } while (cursor.moveToNext());  
	           }  
	           cursor.close(); 	         
	           BitmapFactory.Options options = new BitmapFactory.Options();  
	           options.inDither = false;  
	           options.inPreferredConfig = Bitmap.Config.RGB_565;  
	           Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, _id, Images.Thumbnails.MINI_KIND,  
	                           options);  
	           	   			
	           return bitmap;  
	   }
}

package com.tjsoft.webhall.ui.work;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.DownloadManager;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.FormFile;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.MyDatePickerDialog;
import com.tjsoft.util.SocketHttpRequester;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATT;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.fileChoose.FileChooserActivity;
import com.tjsoft.webhall.ui.wsbs.ImagePreActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Administrator 多附件管理页面
 */
public class MaterialManage extends AutoDialogActivity implements OnCheckedChangeListener {

    private LayoutInflater layoutInflater;
    private RelativeLayout tip;
    private Button add, ok, sure;
    private RelativeLayout back;
    private GridView listView;
//	private  listView;


    private ApplyBean applyBean;
    private String PERMID = "";
    private int position;
    private int TYPE;
    public static List<ATTBean> atts;// 文件集合
    private FileAdapter adapter = new FileAdapter();
    private String ID = "";
    private int deletePosition;
    private int STATUS;
    private EditText mCertificateid;


    private TextView CLnum;
    private FloatingActionButton fab1;
    private FloatingActionButton fab3;
    private Button tvPrecious;
    private Button tvNext;
    private CheckBox mCheckBox;
    private MyDatePickerDialog mStartTimeDialog;
    private MyDatePickerDialog mEndTimeDialog;
    private LinearLayout mCheckShow;
    private int flag;
    private String mark;
    private String idString = "";
    private String startTimeString = "";
    private String endTimeString = "";
    private Context mContext;
    private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).build();
    private ApplyBean FILE;
    private String type;
    private File file;
    private ArrayList<String> files;
    private String from;
    private Bitmap myBitmap;
    private File photo = null;

    List<ApplyBean> applyBeanList;
    private TextView materialName;
    private TextView mStartTime;
    private TextView mEndTime;
    // private TextView licenseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_material_manage"));
        flag = getIntent().getIntExtra("flag", 1);
        mark = getIntent().getStringExtra("mark");
        init();
        initSetOnListener();
        if (TextUtils.equals(from, "1")) {
            startUpload(file);
        } else if (TextUtils.equals(from, "2")) {
            for (int i = 0; i < files.size(); i++) {
                File muiltfile = new File(files.get(i));
                startUpload(muiltfile);
            }
        }
    }

    private void startUpload(final File file) {


//        final ATTBean att = new ATTBean("", file.getName(), "", "", "1");
//        att.setStatus("1");
        if (null == Constants.material.get(FILE.getCLBH())) {
            Constants.material.put(FILE.getCLBH(), new ArrayList<ATTBean>());
        }
//        Constants.materials.get(FILE.getCLBH()).add(att);// 将新增文件添加到map
        if (null != applyBean) {
            atts = Constants.material.get(applyBean.getCLBH());

            if (null != atts && atts.size() > 0) {
                setEdite(false);
            } else {
                setEdite(true);

            }
        }
        adapter.notifyDataSetChanged();
        new Thread(new Runnable() {
            String fileId;
            String filePath;

            @Override
            public void run() {


                try {
                    String requestUrl = Constants.DOMAIN + "servlet/uploadMobileFileServlet";
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("USERCODE", Constants.user.getCODE());
                    params.put("TYPE", TYPE + "");//
                    params.put("ATTACHCODE", FILE.getCLBH());
                    params.put("FILENAME", file.getName());

                    // 上传文件

                    FormFile formfile = new FormFile(file.getName(), file, "image", "application/octet-stream");
                    String json = SocketHttpRequester.post(requestUrl, params, formfile);
                    fileId = new JSONObject(new JSONObject(json).getString("ReturnValue")).getString("FILEID");
                    filePath = new JSONObject(new JSONObject(json).getString("ReturnValue")).getString("FILEPATH");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != fileId && !fileId.equals("") && null != filePath && !filePath.equals("")) {

                            DialogUtil.showUIToast(MaterialManage.this, "上传成功！");
                            ATT attSuccess = new ATT(fileId, file.getName(), filePath, "", "2");
                            attSuccess.setStatus("2");
                            if (null == Constants.material.get(FILE.getCLBH())) {
                                Constants.material.put(FILE.getCLBH(), new ArrayList<ATTBean>());
                            }
//                            Constants.materials.get(FILE.getCLBH()).remove(att);// 将新增文件添加到map
//                            Constants.materials.get(FILE.getCLBH()).add(attSuccess);// 将新增文件添加到map
                            if (null != applyBean) {
                                atts = Constants.material.get(applyBean.getCLBH());

                                if (null != atts && atts.size() > 0) {
                                    setEdite(false);
                                } else {
                                    setEdite(true);

                                }
                            }
                            adapter.notifyDataSetChanged();
                            if (position != -1 && type.equals("1")) {// 证照信息数据状态改变
                                // HistoreShare.licenseDate.get(position).setSTATUS("1");

                            } else if (position != -1 && type.equals("2")) {// 大附件数据状态改变
                                HistoreShare_v2.bigFileDate.get(position).setSTATUS("1");
                            }
                        } else {
                            ATT attSuccess = new ATT(fileId, file.getName(), filePath, "", "3");
                            if (null == Constants.material.get(FILE.getCLBH())) {
                                Constants.material.put(FILE.getCLBH(), new ArrayList<ATTBean>());
                            }

//                            Constants.materials.get(FILE.getCLBH()).remove(att);// 将新增文件添加到map
//                            Constants.materials.get(FILE.getCLBH()).add(attSuccess);// 将新增文件添加到map
                            if (null != applyBean) {
                                atts = Constants.material.get(applyBean.getCLBH());

                                if (null != atts && atts.size() > 0) {
                                    setEdite(false);
                                } else {
                                    setEdite(true);

                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        }).start();


    }

    public File saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void initSetOnListener() {
//        //上一份
//        tvPrecious.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position >= 1) {
//                    applyBean = applyBeanList.get(position - 1);
//                    atts = Constants.material.get(applyBean.getCLBH());
//                    adapter.notifyDataSetChanged();
//                    if (null != atts && atts.size() > 0) {
//                        setEdite(false);
//                    } else {
//                        setEdite(true);
//
//                    }
//                }
//
//            }
//        });
//        //下一份
//        tvNext.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position <= applyBeanList.size() - 2) {
//                    applyBean = applyBeanList.get(position - 1);
//                    atts = Constants.material.get(applyBean.getCLBH());
//                    adapter.notifyDataSetChanged();
//                    if (null != atts && atts.size() > 0) {
//                        setEdite(false);
//                    } else {
//                        setEdite(true);
//
//                    }
//                }
//            }
//        });
        fab1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent();
                    intent.setClass(MaterialManage.this, TakePhotos.class);
                    intent.putExtra("PERMID", PERMID);
                    intent.putExtra("FILEID", applyBean.getFILEID());
                    intent.putExtra("CLMC", applyBean.getCLMC());
                    intent.putExtra("FILE", applyBean);
                    intent.putExtra("position", position);
                    intent.putExtra("TYPE", TYPE + "");
                    startActivity(intent);
                    MaterialManage.this.finish();
                } else {
                    DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_sdcard_unmonted_hint")));
                }
            }
        });
        fab3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent fileChooserIntent = new Intent(MaterialManage.this, FileChooserActivity.class);
                    fileChooserIntent.putExtra("PERMID", PERMID);
                    fileChooserIntent.putExtra("FILEID", applyBean.getFILEID());
                    fileChooserIntent.putExtra("CLMC", applyBean.getCLMC());
                    fileChooserIntent.putExtra("FILE", applyBean);
                    fileChooserIntent.putExtra("position", position);
                    fileChooserIntent.putExtra("TYPE", TYPE + "");
                    startActivity(fileChooserIntent);
                    MaterialManage.this.finish();

                } else {
                    DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_sdcard_unmonted_hint")));
                }
            }
        });
        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				finish();
                returnValue();
            }
        });

        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (flag == 2 && !checkParams()) {// 证照信息完整性检测
                    return;
                }

                if (null != atts && atts.size() > 9) {
                    DialogUtil.showUIToast(MaterialManage.this, "上传材料不能超过10个");
                } else {
                    uploadFile("");
                }
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                returnValue();
//				if (atts != null && atts.size() > 0 && flag == 2 && !"4".equals(mark)) {
//					if (checkParams()) {
//						returnValue();
//					}
//				} else {
//					MaterialManage.this.finish();
//				}
            }
        });
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tip.setVisibility(View.GONE);
                FileUtil.Write(MaterialManage.this, "tip1", "1");
            }
        });
        mStartTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mStartTimeDialog.show();
            }
        });
        mEndTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEndTimeDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        if (flag == 2) {
            mCheckShow.setVisibility(View.VISIBLE);
            if (applyBean != null) {
                String id = applyBean.getCERTIFICATEID();
                String startString = applyBean.getCERTIFICATESTARTDATE();
                String endString = applyBean.getCERTIFICATEENDDATE();
                if (id != null && !"".equals(id.trim())) {
                    mCertificateid.setText(id);
                } else {

                }
                if (startString != null) {
                    mStartTime.setText(startString);
                }
                if (endString != null) {
                    mEndTime.setText(endString.trim());
                    if ("4000-01-01".equals(endString.trim())) {
                        mCheckBox.setChecked(true);
                    } else {
                        mCheckBox.setChecked(false);
                    }
                }

            }
        } else {
            mCheckShow.setVisibility(View.GONE);
        }
        if (null != applyBean) {
            atts = Constants.material.get(applyBean.getCLBH());

            if (null != atts && atts.size() > 0) {
                setEdite(false);
            } else {
                setEdite(true);

            }
        }

        System.out.println("------------------------mark=" + mark);

        // 查看申报详情时不可操作
        if ("4".equals(mark)) {
            setEdite(false);
        }
        adapter.notifyDataSetChanged();
        super.onResume();
    }


    /**
     * 设置控件是否能够编辑
     *
     * @param b
     */
    private void setEdite(Boolean b) {
        mCheckBox.setClickable(b);
        mStartTime.setFocusable(b);
        mStartTime.setClickable(b);
        mEndTime.setFocusable(b);
        mEndTime.setClickable(b);
        // mCertificateid.setFocusable(b);
        // mCertificateid.setClickable(b);
        mCertificateid.setEnabled(b);

    }

    private void init() {
        // 值传递
        layoutInflater = getLayoutInflater();
        applyBean = (ApplyBean) getIntent().getSerializableExtra("applyBean");
        PERMID = getIntent().getStringExtra("PERMID");
        position = getIntent().getIntExtra("position", 0);
        TYPE = getIntent().getIntExtra("TYPE", 0);
        STATUS = getIntent().getIntExtra("STATUS", 0);
        applyBeanList = (List<ApplyBean>) getIntent().getSerializableExtra("CLBHList");
//applyBeanList = Upload.applyBeans;
        //从相机过来的
        PERMID = getIntent().getStringExtra("PERMID");
        FILE = (ApplyBean) getIntent().getSerializableExtra("FILE");
        type = getIntent().getStringExtra("TYPE");
        position = getIntent().getIntExtra("position", -1);
        file = (File) getIntent().getSerializableExtra("file");//单文件传过来的
        files = (ArrayList<String>) getIntent().getSerializableExtra("files");//单文件传过来的
        from = getIntent().getStringExtra("FROM");
        add = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "add"));
        back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        ok = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "ok"));
        sure = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "sure"));
        tip = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "tip"));
        if (null != FileUtil.Load(this, "tip1") && FileUtil.Load(this, "tip1").equals("1")) {
            tip.setVisibility(View.GONE);// 实用悬浮框隐藏
        }

        if (STATUS != -1 && STATUS != 4 && STATUS != 9) {
            add.setVisibility(View.GONE);
        }
        listView = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
        listView.setAdapter(adapter);

        mCertificateid = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "certificateid"));
        materialName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "materialName"));
        materialName.setText(applyBean.getCLMC());
        mStartTime = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "certificatestartdate"));
        mEndTime = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "certificateenddate"));

        //第几个材料
        CLnum = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "tv_page_info"));
        tvPrecious = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btn_previous"));
        tvNext = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btn_next"));
//        CLnum.setText("(" + (position+1 )+ "/" + applyBeanList.size() + ")");
        fab1 = (FloatingActionButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "fab1"));
        fab3 = (FloatingActionButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "fab3"));

        mCertificateid.setText(applyBean.getCERTCODE());
        mStartTime.setText(applyBean.getCERTSTARTTIME());
        mEndTime.setText(applyBean.getCERTENDTIME());

        mCheckBox = (CheckBox) findViewById(MSFWResource.getResourseIdByName(this, "id", "checkbox"));
        mCheckBox.setOnCheckedChangeListener(this);
        mCheckShow = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "check_show"));

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mStartTimeDialog = new MyDatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder sb = new StringBuilder();

                sb.append(year + "-");
                if (monthOfYear >= 9) {
                    sb.append(++monthOfYear + "-");
                } else {
                    sb.append("0" + (++monthOfYear) + "-");
                }
                if (dayOfMonth > 9) {
                    sb.append(dayOfMonth + "");
                } else {
                    sb.append("0" + dayOfMonth);
                }
                if (!sb.toString().equals(mStartTime.getText().toString().trim())) {
                    mStartTime.setText("");
                }
                mStartTime.setText(sb.toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        mEndTimeDialog = new MyDatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder sb = new StringBuilder();
                if (year > calendar.get(Calendar.YEAR)) {

                } else if (year == calendar.get(Calendar.YEAR) && monthOfYear > calendar.get(Calendar.MONTH)) {

                } else if (monthOfYear == calendar.get(Calendar.MONTH) && dayOfMonth >= calendar.get(Calendar.DAY_OF_MONTH)) {

                } else {
                    year = calendar.get(Calendar.YEAR);
                    monthOfYear = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }

                sb.append(year + "-");
                if (monthOfYear >= 9) {
                    sb.append(++monthOfYear + "-");
                } else {
                    sb.append("0" + (++monthOfYear) + "-");
                }
                if (dayOfMonth > 9) {
                    sb.append(dayOfMonth + "");
                } else {
                    sb.append("0" + dayOfMonth);
                }
                String startTime = mStartTime.getText().toString().trim();
                try {
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
                    Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(sb.toString());
                    if (startDate.getTime() > endDate.getTime()) {
                        DialogUtil.showUIToast(MaterialManage.this, "起始日期不能大于结束日期");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mEndTime.setText(sb.toString());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    class FileAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return null == atts ? 0 : atts.size();
        }

        @Override
        public Object getItem(int position) {
            return null == atts ? 0 : atts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            FileItem item;
            if (null == convertView) {
                item = new FileItem();
                convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_new_edit_file_item"), parent, false);
                item.item_bg = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "item_bg"));
                item.imgPreView = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "imgPreView"));
                item.fileName = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "fileName"));
                item.edit = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "edit"));
                item.delete = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "delete"));
                item.download = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "download"));
                convertView.setTag(item);
            } else {
                item = (FileItem) convertView.getTag();
            }

//			if (STATUS != -1 && STATUS != 4 && STATUS != 9) {
//				item.delete.setVisibility(View.GONE);
//			}

            final ATTBean att = atts.get(position);
            final String end = att.getATTACHNAME().substring(att.getATTACHNAME().lastIndexOf(".") + 1, att.getATTACHNAME().length()).toLowerCase(Locale.US);

//            if (TextUtils.equals(att.getStatus(), "1")) {
//                item.fileName.setText("上传中");
//                Picasso.with(MaterialManage.this).load(MSFWResource.getResourseIdByName(MaterialManage.this, "drawable", "uploading")).into(item.imgPreView);
//            } else if (TextUtils.equals(att.getStatus(), "3")) {
//                item.fileName.setText("上传失败");
//                Picasso.with(MaterialManage.this).load(MSFWResource.getResourseIdByName(MaterialManage.this, "drawable", "uploadfail")).into(item.imgPreView);
//            } else {
                item.fileName.setText(att.getATTACHNAME());
                if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(end)) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + att.getID(), item.imgPreView, options);

                } else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(end) || "dotx".equals(end) || "dotm".equals(end)) {

                    item.imgPreView.setImageResource(MSFWResource.getResourseIdByName(MaterialManage.this, "drawable", "tj_ic_word")); // word文档文件
                } else if ("pdf".equals(end)) {
                    item.imgPreView.setImageResource(MSFWResource.getResourseIdByName(MaterialManage.this, "drawable", "tj_ic_pdf")); // pdf文件
                } else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end)) {
                    item.imgPreView.setImageResource(MSFWResource.getResourseIdByName(MaterialManage.this, "drawable", "tj_ic_excel")); // excel文件
                } else {
                    item.imgPreView.setImageResource(MSFWResource.getResourseIdByName(MaterialManage.this, "drawable", "tj_ic_file_unknown"));
                }
//            }



            item.edit.setOnClickListener(new OnClickListener() {// 编辑，重新上传

                @Override
                public void onClick(View v) {
                    if (null != att.getID()) {
                        uploadFile(att.getID());
                    } else {
                        DialogUtil.showUIToast(MaterialManage.this, "材料id不能为空");
                    }
                }
            });
            item.delete.setOnClickListener(new OnClickListener() {// 删除

                @Override
                public void onClick(View v) {
//							deletePosition = position;
//							ID = att.getID();
//							Background.Process(MaterialManage.this, Delete, "正在操作...");
                    new AlertDialog.Builder(MaterialManage.this).setMessage("是否删除该文件").setTitle(getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deletePosition = position;
                            ID = att.getID();
                            Background.Process(MaterialManage.this, Delete, "正在操作...");
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();
                }
            });
            item.item_bg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(end)) {
                            Intent intent = new Intent();
                            intent.setClass(MaterialManage.this, ImagePreActivity.class);
                            intent.putExtra("index", position);
                            startActivity(intent);
                        } else {
                            DownloadManager mDownloadManager = new DownloadManager(mContext, Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + att.getID(), att.getATTACHNAME());
                            mDownloadManager.checkFileIsExists();
//	                        Uri uri = Uri.parse(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + att.getID());
//	                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//	                        startActivity(intent);
                        }

                    } else {
                        DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_sdcard_unmonted_hint")));
                    }
                }
            });
/*			item.item_bg.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {

					new AlertDialog.Builder(MaterialManage.this).setMessage("是否删除该文件").setTitle(getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							deletePosition = position;
							ID = att.getID();
							Background.Process(MaterialManage.this, Delete, "正在操作...");
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						}
					}).show();
					return true;

				}
			});*/

            return convertView;
        }

        class FileItem {
            ImageView imgPreView;// 材料名称
            TextView fileName;// 材料名称
            Button edit;// 重新上传
            Button delete;// 删除
            Button download;// 下载
            RelativeLayout item_bg;
        }

    }

    /**
     * 删除文件
     */
    final Runnable Delete = new Runnable() {
        @Override
        public void run() {
            try {

                JSONObject param = new JSONObject();
                param.put("ID", ID);
                param.put("TYPE", TYPE + "");
                if (TextUtils.isEmpty(ID)) {
                    atts.remove(deletePosition);
                    adapter.notifyDataSetChanged();
                } else {
                    String response = HTTP.excute("attachDelete", "SpaceAttachInfoService ", param.toString());
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    if (code.equals("200")) {
                        DialogUtil.showUIToast(MaterialManage.this, "删除成功！");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                atts.remove(deletePosition);
                                adapter.notifyDataSetChanged();
                                if (atts.size() == 0) {// 更新材料上传页面共享状态
                                    setEdite(true);
                                    if (TYPE == 1) {// 证照信息数据状态改变
//									HistoreShare_v2.licenseDate.get(position).setSTATUS("0");
                                    } else if (TYPE == 2) {// 大附件数据状态改变
                                        HistoreShare_v2.bigFileDate.get(position).setSTATUS("0");
                                    }
                                }

                            }
                        });

                    } else {
                        String error = json.getString("error");
                        DialogUtil.showUIToast(MaterialManage.this, error);
                    }
                }


            } catch (Exception e) {
                DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    private void uploadFile(String id) {
        applyBean.setFILEID(id);
        new AlertDialog.Builder(MaterialManage.this).setTitle("请选择上传方式").setItems(new String[]{"拍照上传", "选择文件"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int index) {
                switch (index) {
                    case 0:// 拍照上传
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Intent intent = new Intent();
                            intent.setClass(MaterialManage.this, TakePhotos.class);
                            intent.putExtra("PERMID", PERMID);
                            intent.putExtra("CLMC", applyBean.getCLMC());
                            intent.putExtra("position", position);
                            intent.putExtra("TYPE", TYPE + "");
                            intent.putExtra("FILE", applyBean);
                            intent.putExtra("idString", idString);
                            intent.putExtra("startTimeString", startTimeString);
                            intent.putExtra("endTimeString", endTimeString);

                            startActivityForResult(intent, 2);
                        } else {
                            DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_sdcard_unmonted_hint")));
                        }

                        break;
                    case 1:// 选择文件上传

                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Intent fileChooserIntent = new Intent();
                            fileChooserIntent = new Intent(MaterialManage.this, FileChooserActivity.class);
                            fileChooserIntent.putExtra("PERMID", PERMID);
                            fileChooserIntent.putExtra("CLMC", applyBean.getCLMC());
                            fileChooserIntent.putExtra("position", position);
                            fileChooserIntent.putExtra("TYPE", TYPE + "");
                            fileChooserIntent.putExtra("FILE", applyBean);
                            fileChooserIntent.putExtra("idString", idString);
                            fileChooserIntent.putExtra("startTimeString", startTimeString);
                            fileChooserIntent.putExtra("endTimeString", endTimeString);

                            startActivityForResult(fileChooserIntent, 1);
                        } else {
                            DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_sdcard_unmonted_hint")));
                        }

                        break;

                    default:
                        break;
                }

            }
        }).setNegativeButton("取消", null).show();
    }

    private void returnValue() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        ApplyBean apply = new ApplyBean();
        apply.setID(applyBean.getID());
        apply.setCERTIFICATEID(mCertificateid.getText().toString().trim());
        apply.setCERTIFICATESTARTDATE(mStartTime.getText().toString().trim());
        apply.setCERTIFICATEENDDATE(mEndTime.getText().toString().trim());
        bundle.putSerializable("apply", apply);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        returnValue();
//		if (atts != null && atts.size() > 0 && flag == 2 && !"4".equals(mark)) {
//			if (checkParams())
//				returnValue();
//		} else {
//			finish();
//		}

    }

    private boolean checkParams() {

        idString = mCertificateid.getText().toString().trim();
        if (TextUtils.isEmpty(idString)) {
            DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_certificateid")));
            return false;
        }

        startTimeString = mStartTime.getText().toString().trim();
        if (TextUtils.isEmpty(startTimeString)) {
            DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_certificatestartdate")));
            return false;
        }

        endTimeString = mEndTime.getText().toString().trim();
        if (TextUtils.isEmpty(endTimeString)) {
            DialogUtil.showUIToast(MaterialManage.this, getString(MSFWResource.getResourseIdByName(MaterialManage.this, "string", "tj_certificateenddate")));
            return false;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mEndTime.setText("4000-01-01");
            mEndTime.setFocusable(false);
            mEndTime.setClickable(false);
        } else {
            mEndTime.setText("");
            mEndTime.setFocusable(true);
            mEndTime.setClickable(true);
        }
    }

}

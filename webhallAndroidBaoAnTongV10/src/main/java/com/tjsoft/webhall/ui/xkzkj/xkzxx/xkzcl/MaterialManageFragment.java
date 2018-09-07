package com.tjsoft.webhall.ui.xkzkj.xkzxx.xkzcl;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.intsig.scanner.ScannerSDK;
import com.tjsoft.camera.TakePhotoActivity;
import com.tjsoft.msfw.guangdongshenzhenbaoan.BR;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.ImageLoadUtils2;
import com.tjsoft.webhall.afilechoose.afilechooser.utils.FileUtils;
import com.tjsoft.webhall.constants.AppKey;
import com.tjsoft.webhall.constants.BusConstant;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.constants.PermissionCode;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.entity.PictureEvent;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.materialmanage.MaterialManageContract;
import com.tjsoft.webhall.ui.materialmanage.MaterialManagePresenter;
import com.tjsoft.webhall.ui.work.TakePhotos;
import com.tjsoft.webhall.ui.wsbs.ImagePreActivity;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.FileUploadCallBack;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.ShiliActivity;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.XKZXXActivity;
import com.tjsoft.webhall.widget.UILImageLoader;
import com.tjsoft.webhall.widget.XKZShiliDialog;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.BaseFragment;
import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.utils.NetworkUtils;
import net.liang.appbaselibrary.utils.SPUtils;
import net.liang.appbaselibrary.utils.ToastUtils;
import net.liang.appbaselibrary.widget.dialog.DialogHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * by lianghuiyong@outlook.com on 2017/1/23.
 * 多附件管理页面
 */

public class MaterialManageFragment extends BaseFragment implements MaterialManageContract_xkz.View, View.OnClickListener {

    TextView rightTv;
    TextView tvPageInfo;
    Button btnPrevious;
    Button btnNext;
    TextView materialName;
    RecyclerView mRecyclerView;
    FloatingActionMenu menuFab;
    public String status;
    public String certcode;

    FloatingActionButton fab0;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;

    public static MaterialManageContract_xkz.Presenter presenter;
    Button autoDelete;

    boolean isOpenSDK = false;

    private ItemDragAdapter mAdapter;
    private ItemDragAndSwipeCallback itemDragAndSwipeCallback;
    private ItemTouchHelper itemTouchHelper;

    public static List<ATTBean> listData;
    private DialogHelper dialogHelper;
    private List<ATTBean> atts;// 文件集合

    //多图请求码
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_TAKEPHOTO = 1002;
    private static final int REQUEST_CHOOSER = 1234;
    public int TYPE;
    private String uploadType;

    //判断切边跳转的参数
    private ScannerSDK mScannerSDK;
    private static final String APPKEY = "K2XWPEQABP11PafD1JRQQr8D";
    private int mEngineContext;
    boolean boolClick = true;
    int errorcode = 0;
    private Toast mToast;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_material_manage;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusConstant busConstant) {
        switch (busConstant) {
            case UPDATA_ATTS_STUTAS:
                mAdapter.notifyDataSetChanged();
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PictureEvent pictureEvent) {
        if (pictureEvent.isSingle()) {

            presenter.addAtt(pictureEvent.getFileUri());
        } else {
            List<String> resultList = pictureEvent.getFileUris();
            if (resultList != null) {
                for (String fileAddress : resultList) {
                    //添加到列表
                    presenter.addAtt(fileAddress);
                }

                if (!NetworkUtils.isConnected(getActivity())) {
                    showToast("当前网络不可用");
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        status = getArguments().getString("status");
        certcode = getArguments().getString("certcode");

        mScannerSDK = new ScannerSDK();

        new Thread(new Runnable() {

            @Override
            public void run() {
                int code = mScannerSDK.initSDK(getActivity(),
                        APPKEY);
                mEngineContext = mScannerSDK.initThreadContext();
                mHandler.sendEmptyMessage(code);
            }
        }).start();

        tvPageInfo = (TextView) getActivity().findViewById(R.id.tv_page_info);
        btnPrevious = (Button) getActivity().findViewById(R.id.btn_previous);
        btnNext = (Button) getActivity().findViewById(R.id.btn_next);
        materialName = (TextView) getActivity().findViewById(R.id.materialName);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list);
        menuFab = (FloatingActionMenu) getActivity().findViewById(R.id.menu_fab);
        fab0 = (FloatingActionButton) getActivity().findViewById(R.id.fab0);
        fab1 = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) getActivity().findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) getActivity().findViewById(R.id.fab3);
        autoDelete = (Button) getActivity().findViewById(R.id.auto_delete);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        fab0.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        autoDelete.setOnClickListener(this);

        materialName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isOpenSDK = !isOpenSDK;
                if (isOpenSDK) {
                    ToastUtils.showToast("打开新的SDK相机");
                } else {
                    ToastUtils.showToast("用原来的相机");
                }
                return false;
            }
        });
        menuFab.setClosedOnTouchOutside(true);
        menuFab.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFab.toggle(true);
            }
        });
        ThemeConfig theme = new ThemeConfig.Builder()
//                .setTitleBarTextColor(getResources().getColor(color))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        //配置imageloader
        ImageLoader imageloader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(getActivity(), imageloader, theme)
//                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
//                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        dialogHelper = new DialogHelper(getActivity());
        mAdapter = new ItemDragAdapter(null);
//        TYPE = getIntent().getIntExtra(AppKey.TYPE, 2);
        TYPE = 2;
        //直接添加跳转过来的
//        uploadType = getIntent().getStringExtra("uploadType");


        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        presenter = new MaterialManagePresenter_xkz(this,getActivity());
        reView();
        if (null == listData || listData.size() == 0) {//如果列表为空，默认展开上传方式
            menuFab.toggle(true);
        }

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
//
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//
//                    Intent intent = new Intent();
//                    intent.setClass(getActivity(), ImagePreActivity.class);
//                    intent.putExtra("index", position);
//                    startActivity(intent);
//                } else {
//                    showToast(getString(R.string.sdcard_unmonted_hint));
//                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ATTBean attBean = (ATTBean) adapter.getData().get(position);
                attBean.setTYPE("1");
                if (view.getId() == R.id.delete_iv) {
                    switch (attBean.getUpStatus()) {
                        case UPLOADING:
//                            showToast("正在上传，请上传完操作");
                            removeListAtt(attBean);

                            break;
                        case UPLOAD_ERROR:
                            removeListAtt(attBean);
                            break;
                        default:
                            if (!NetworkUtils.isConnected(getActivity())) {
                                showToast("当前网络不可用");
                            } else {
                                showIsDelete(attBean);
                            }
                            break;
                    }
                } else if (view.getId() == R.id.reUpData) {
                    if (!NetworkUtils.isConnected(getActivity())) {
                        showToast("当前网络不可用");
                    } else {
                        presenter.uploadFile(attBean);
                    }
                }
            }
        });

/*        mRecyclerView.addOnScrollListener(new BaseRecyclerListener(new GridLayoutManager(this, 3), mAdapter) {
            @Override
            public void onLoadEnd() {
            }

            @Override
            public void onSlide() {
                if (!presenter.getIsHideEditView()) {
                    menuFab.hideMenu(false);
                }
            }

            @Override
            public void onDescent() {
                if (!presenter.getIsHideEditView()) {
                    menuFab.showMenu(false);
                }
            }
        });*/
        if (TextUtils.equals("1", status)) {//已上传

            GloabDelegete gloabDelegete = Constants.getInstance()
                    .getGloabDelegete();
            TransportEntity transportEntity = gloabDelegete.getUserInfo();
            presenter.getOneCertInfo(transportEntity.getToken(), transportEntity.getINC_ZZJGDM(), certcode, getActivity());


        } else {


        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//暂时屏蔽
            if (msg.what != 0) {
//                errorcode = msg.what;
//                Toast.makeText(
//                        MaterialManageActivity.this,
//                        "授权失败" + "-->" + errorcode + "\nmsg:"
//                                + CommonUtil.commentMsg(errorcode),
//                        Toast.LENGTH_LONG).show();
                boolClick = false;
            }
            if (TextUtils.equals(uploadType, "camera")) {
                takePhotoToUp();
            } else if (TextUtils.equals(uploadType, "choosefile")) {
                chooseFileToUp();
            } else if (TextUtils.equals(uploadType, "choosephoto")) {
                chooseGalleryToUp();
            }
            super.handleMessage(msg);
        }

    };

    @Override
    public void dissMissEdit(boolean isMissEdit) {
        if (isMissEdit) {
//            rightTv.setVisibility(View.GONE);
            menuFab.setVisibility(View.GONE);
        } else {
            itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
            itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

            // 开启拖拽
            mAdapter.enableDragItem(itemTouchHelper, R.id.root_layout, true);
        }
    }

    @Override
    public void attsListNotify() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void dismissDialog() {
        dialogHelper.dismissProgressDialog();
    }

    @Override
    public void showIsDelete(final ATTBean attBean) {
        dialogHelper.alert("提示", "确定删除该材料？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogHelper.showProgressDialog("正在删除...");
                presenter.delete(attBean);
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public void reView() {
        presenter.loadData(getActivity());
        listData = presenter.getAtts();
            for (int i = 0; i < listData.size(); i++) {
                listData.get(i).setTYPE(TYPE + "");
            }
            mAdapter.setNewData(listData);


    }

    @Override
    public void removeListAtt(ATTBean attBean) {
        int position = presenter.getAtts().indexOf(attBean);
        if (position >= 0) {
            mAdapter.remove(position);
        }
    }
    public void getMaterials(String certcode, FileUploadCallBack callBack) {
        try {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);
            serializer.startDocument("utf-8", null);
            serializer.startTag("", "certfiles");


                if (null != Constants.materialXkz.get(certcode)) {// 设置文件节点

                    for (int j = 0; j < Constants.materialXkz.get(certcode).size(); j++) {
                        if (Constants.materialXkz.get(certcode).get(j).getUpStatus() == UploadStatus.UPLOAD_SUCCESS) {//上传成功的材料
                            serializer.startTag("", "file");

                            serializer.startTag("", "fileid");
                            serializer.cdsect(Constants.materialXkz.get(certcode).get(j).getID());
                            serializer.endTag("", "fileid");
                            serializer.startTag("", "certfilename");
                            serializer.cdsect(Constants.materialXkz.get(certcode).get(j).getATTACHNAME());
                            serializer.endTag("", "certfilename");
                            serializer.startTag("", "attrachpath");
                            serializer.cdsect(Constants.materialXkz.get(certcode).get(j).getATTACHURL());
                            serializer.endTag("", "attrachpath");
                            serializer.endTag("", "file");
                        } else {
                            XKZXXActivity.isAllUpload = false;
                        }
                    }
                }



            serializer.endTag("", "certfiles");
            serializer.endDocument();
            callBack.onResponse(writer.toString().replaceAll("<\\?xml.*.\\?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onError(e);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ATTBean attBean) {
        mAdapter.notifyDataSetChanged();

        switch (attBean.getUpStatus()) {
            case UPLOAD_SUCCESS:
                break;
            case UPLOAD_ERROR:
                showToast("上传失败！");
                break;
            case UPLOADING:
                isEnableAutoDelete(presenter.isHasUpload());
                break;
            case ADD_ATT_FOR_MATERIAL_MANAGE:
                presenter.addAtt(attBean.getLocalPath());
                break;
        }
    }

    public class ItemDragAdapter extends BaseItemDraggableAdapter<ATTBean, BindingViewHolder> {
        ItemDragAdapter(List data) {
            super(R.layout.item_new_edit_file, data);
        }

        @Override
        protected View getItemView(int layoutResId, ViewGroup parent) {
            ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
            if (binding == null) {
                return super.getItemView(layoutResId, parent);
            }
            View view = binding.getRoot();
            view.setTag(net.liang.appbaselibrary.R.id.BaseQuickAdapter_databinding_support, binding);
            return view;
        }

        @Override
        protected BindingViewHolder createBaseViewHolder(View view) {
            return new BindingViewHolder(view);
        }

        @Override
        protected void convert(final BindingViewHolder baseViewHolder, final ATTBean attBean) {
            baseViewHolder.addOnClickListener(R.id.delete_iv);
            baseViewHolder.addOnClickListener(R.id.reUpData);

            ViewDataBinding binding = baseViewHolder.getBinding();
            binding.setVariable(BR.attBean, attBean);

            ImageView iv_img = baseViewHolder.getView(R.id.imgPreView);

            String end = attBean.getATTACHNAME().substring(attBean.getATTACHNAME().lastIndexOf(".") + 1, attBean.getATTACHNAME().length()).toLowerCase(Locale.US);
            if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(end)) {

                //如果没id，显示本地图片
                if (TextUtils.isEmpty(attBean.getID())) {
                    ImageLoadUtils2.loadImage(attBean.getLocalPath(), iv_img, R.mipmap.jiazaizhong);
                } else {
                    String path = SPUtils.getString(attBean.getID());

                    // 有id，想判断本地有没有该图片，如果本地图片不存在，请求网络图
                    if (!net.liang.appbaselibrary.utils.FileUtils.isFileExists(path)) {
                        // 则请求网络图片显示
                        ImageLoadUtils2.loadImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + attBean.getID(), iv_img, R.mipmap.jiazaizhong);
                    } else {
                        ImageLoadUtils2.loadImage(path, iv_img, R.mipmap.jiazaizhong);

                    }
                }

            } else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(end) || "dotx".equals(end) || "dotm".equals(end)) {
                iv_img.setImageResource(R.mipmap.tj_ic_word); // word文档文件
            } else if ("pdf".equals(end)) {
                iv_img.setImageResource(R.mipmap.tj_ic_pdf); // pdf文件
            } else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end)) {
                iv_img.setImageResource(R.mipmap.tj_ic_excel); // excel文件
            } else {
                iv_img.setImageResource(R.mipmap.tj_ic_file_unknown);
            }


            //databinding 不起作用的地方

//
//            Button btReupload = baseViewHolder.getView(R.id.reUpData);
//            if (attBean.getUpStatus() == UploadStatus.UPLOAD_ERROR) {
//                btReupload.setVisibility(View.VISIBLE);
//            } else {
//                btReupload.setVisibility(View.GONE);
//            }
//            FrameLayout fmProgress = baseViewHolder.getView(R.id.progress_layout);
//            if (attBean.getUpStatus() == UploadStatus.UPLOADING) {
//                btReupload.setVisibility(View.VISIBLE);
//            } else {
//                btReupload.setVisibility(View.GONE);
//            }
//            TextView tvFileName = baseViewHolder.getView(R.id.fileName);
//            tvFileName.setText(attBean.getATTACHNAME());
//            ImageView ivDelete = baseViewHolder.getView(R.id.delete_iv);
//            if ((attBean.isEdit() && (attBean.getUpStatus() != UploadStatus.UPLOADING))) {
//                ivDelete.setVisibility(View.VISIBLE);
//            } else {
//                ivDelete.setVisibility(View.GONE);
//            }


            binding.executePendingBindings();
        }
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

            if (resultList != null) {
                for (PhotoInfo photoInfo : resultList) {
                    //添加到列表
                    presenter.addAtt(photoInfo.getPhotoPath());

                }

                if (!NetworkUtils.isConnected(getActivity())) {
                    showToast("当前网络不可用");
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        menuFab.close(true);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHOOSER:

//                这里先不加选文件的
                if (resultCode == getActivity().RESULT_OK) {
                    final Uri uri = data.getData();
                    String path = FileUtils.getPath(getActivity(), uri);
                    if (path != null && FileUtils.isLocal(path)) {
                        //添加到列表
                        presenter.addAtt(path);
                    }

                    if (!NetworkUtils.isConnected(getActivity())) {
                        showToast("当前网络不可用");
                    }
                }
                break;
            case REQUEST_CODE_TAKEPHOTO:

//                这里先不加选文件的
                if (resultCode == getActivity().RESULT_OK) {

                }
                break;
        }
    }

    @Override
    public void showIsEdit(boolean isEdit) {
//        rightTv.setText(isEdit ? "完成" : "编辑");
//
        presenter.setAttsEdit(isEdit);
        mAdapter.notifyDataSetChanged();
//
        //是否编辑控制添加按钮的显示与隐藏
        if (isEdit) {
            menuFab.hideMenu(true);
            showAutoDelete(true);
        } else {
            showAutoDelete(false);
            menuFab.showMenu(true);
        }
    }

    @Override
    public void showToolBarTv(boolean isShow) {
//        rightTv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showPageNo(int num, int total) {
        tvPageInfo.setText("(" + num + "/" + total + ")");
    }
    @Override
    public void showOneCertInfo(CertInfoBean returnValue) {



        if (!TextUtils.isEmpty(certcode)) {
            if (null == atts) {
                atts = new ArrayList<>();
                Constants.materialXkz.put(certcode, atts);
            }
        }
        List<CertInfoBean.CertfileBean> certfile = returnValue.getCertfile();
//        if (certfile != null && certfile.size() != 0) {
            for (int i = 0; i < certfile.size(); i++) {
                atts .add( new ATTBean(certfile.get(i).getFileid(),certfile.get(i).getCERTFILENAME(),certfile.get(i).getATTRACHPATH(),""));
            }
            Constants.materialXkz.put(certcode,atts);
//           mAdapter.setNewData(Constants.materialXkz.get(certcode));
            presenter.loadData(getActivity());
            listData = presenter.getAtts();
            for (int i = 0; i < listData.size(); i++) {
                listData.get(i).setTYPE(TYPE + "");
            }
            mAdapter.setNewData(listData);


//        }


    }
    @Override
    public void showIsNext(boolean isAble) {
        btnNext.setEnabled(isAble);
        btnNext.setTextColor(isAble ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.edit_hint_color));
    }

    @Override
    public void showAutoDelete(boolean isShow) {
        autoDelete.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void isEnableAutoDelete(boolean isAble) {
        autoDelete.setEnabled(isAble);
    }

    @Override
    public void showIsPrevious(boolean isAble) {
        btnPrevious.setEnabled(isAble);
    }

    @Override
    public void showMaterialName(String name) {
        materialName.setText(name);
    }

    @Override
    protected MvpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onClick(View view) {
      /*  if (view == rightTv) {
            presenter.setEdit(!presenter.isEdit());
        } else */if (view == btnPrevious) {
            presenter.onPrevious(getActivity());
        } else if (view == btnNext) {
            presenter.onNext(getActivity());
        }else if (view == getActivity().findViewById(R.id.fab0)) {
            if (!TextUtils.isEmpty(Constants.XKZshili1) || !TextUtils.isEmpty(Constants.XKZshili2)) {
                XKZShiliDialog dialog = new XKZShiliDialog(getActivity(), "", "", new XKZShiliDialog.Confirm() {
                    @Override
                    public void onConfirm() {

                    }
                });
            } else {
//                showToast("该许可证暂无示例样表");

//                Toast.makeText(getActivity(),"该许可证暂无示例样表",Toast.LENGTH_SHORT);
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mToast == null) {
                            mToast = new Toast(getActivity());
                            View view = LayoutInflater.from(getActivity()).inflate(
                                    net.liang.appbaselibrary.R.layout.dialog_transient_notification, null);
                            TextView tv = (TextView) view.findViewById(android.R.id.message);
                            tv.setText("该许可证暂无示例样表");
                            mToast.setView(view);
                            mToast.setDuration(Toast.LENGTH_SHORT);

                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            mToast.show();
                        }

                    }
                });
//                dialogHelper.toast("该许可证暂无示例样表",500);
            }





        }  else if (view == getActivity().findViewById(R.id.fab1)) {
            // 拍照上传
            takePhotoToUp();
        } else if (view == getActivity().findViewById(R.id.fab2)) {
            chooseGalleryToUp();

        } else if (view == getActivity().findViewById(R.id.fab3)) {
//            这里先不加选文件的
            // 选择文件上传
            chooseFileToUp();
        } else if (view == autoDelete) {
            //全部删除
            if (!NetworkUtils.isConnected(getActivity())) {
                showToast("当前网络不可用");
            } else {
                dialogHelper.alert("提示", "确定删除全部材料？", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.autoDelete();
                    }
                }, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }
    }

    private void chooseFileToUp() {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    private void chooseGalleryToUp() {
        //多选打开相册 , 带配置
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                FunctionConfig config = new FunctionConfig.Builder()
                        .setMutiSelectMaxSize(99)
                        .build();

                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, mOnHanlderResultCallback);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionCode.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            showToast(getString(R.string.sdcard_unmonted_hint));
        }
    }

    private void takePhotoToUp() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
                Intent intent = new Intent();
                intent.putExtra(AppKey.INTENT_KEY, AppKey.FROM_MATERIAL_MANAGE);
//                if (Constants.isQiebian) {

                //判断切边sdk是否可用
                if (boolClick) {
                    intent.setClass(getActivity(), TakePhotoActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_TAKEPHOTO);
                } else {
                    intent.setClass(getActivity(), TakePhotos.class);
//                    intent.putExtra("PERMID", PERMID);
//                    intent.putExtra("CLMC", applyBean.getCLMC());
//                    intent.putExtra("position", position);
//                    intent.putExtra("TYPE", TYPE + "");
//                    intent.putExtra("FILE", applyBean);
//                    intent.putExtra("idString", idString);
//                    intent.putExtra("startTimeString", startTimeString);
//                    intent.putExtra("endTimeString", endTimeString);

                    startActivityForResult(intent, 2);
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PermissionCode.MY_PERMISSIONS_CAMERA);
            }


        } else {
            showToast(getString(R.string.sdcard_unmonted_hint));
        }
    }

//    @Override
//    public void onBackPressed() {
//
//        if (presenter.isEdit()) {
//            presenter.setEdit(false);
//        } else {
//            super.onBackPressed();
//        }
//
//    }
}

package com.tjsoft.webhall.ui.materialmanage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bean.BaseResponseReturnValue;
import com.bean.GetUploadFileBean;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tjsoft.data.ZhengwuRepository;
import com.tjsoft.data.local.LocalZhengwuDataSource;
import com.tjsoft.data.remote.RemoteZhengwuDataSource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;
import com.tjsoft.webhall.util.NumberUtils;

import net.liang.appbaselibrary.base.mvp.BasePresenter;
import net.liang.appbaselibrary.utils.AppJsonFileReader;
import net.liang.appbaselibrary.utils.NetworkUtils;
import net.liang.appbaselibrary.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.tjsoft.webhall.constants.BusConstant.UPDATA_ATTS_STUTAS;


/**
 * Created by xieguangwei on 2017/1/23.
 * 多附件管理界面presenter、view接口存放
 */

public class MaterialManagePresenter extends BasePresenter implements MaterialManageContract.Presenter {
    @NonNull
    private MaterialManageContract.View view;
    @NonNull
    private ZhengwuRepository zhengwuRepository;

    private volatile boolean isEdit;

    public static String nameEnd = "材料";
    private List<Map<String, String>> materialNameList;
    private int STATUS;
    private ApplyBean applyBean;
    private List<ATTBean> atts;// 文件集合
    private ATTBean addItemBean;
    private int managerPosition = 0;
    private Context context;

    public MaterialManagePresenter(@NonNull MaterialManageContract.View mView) {
        this.view = mView;
        this.context = (Context) mView;

        materialNameList = new ArrayList<>();
        zhengwuRepository = new ZhengwuRepository(RemoteZhengwuDataSource.getInstance(), LocalZhengwuDataSource.getInstance());

        addItemBean = new ATTBean();
        addItemBean.setATTACHNAME("add");
    }
    public MaterialManagePresenter(@NonNull MaterialManageContract.View mView,Context context) {
        this.view = mView;
        this.context = context;

        materialNameList = new ArrayList<>();
        zhengwuRepository = new ZhengwuRepository(RemoteZhengwuDataSource.getInstance(), LocalZhengwuDataSource.getInstance());

        addItemBean = new ATTBean();
        addItemBean.setATTACHNAME("add");
    }

    @Override
    public void loadData(Activity activity) {
        this.applyBean = (ApplyBean) activity.getIntent().getSerializableExtra("applyBean");
        this.managerPosition = activity.getIntent().getIntExtra("position", 0);
        this.STATUS = activity.getIntent().getIntExtra("STATUS", -1);


        //初始化数据
        String names = AppJsonFileReader.getJson(activity, "material_key_words.json");
        if (!TextUtils.isEmpty(names)) {
            materialNameList = AppJsonFileReader.setListData(names);
        }


        if (materialNameList != null && materialNameList.size() > 0) {
            for (int i = 0; i < materialNameList.size(); i++) {
                String name = materialNameList.get(i).get("name");
                if (null != applyBean && !TextUtils.isEmpty(applyBean.getCLMC()) && applyBean.getCLMC().contains(name)) {
                    nameEnd = name;

                    break;
                }
            }
        }

        //初始化att列表
        if (null != applyBean) {
            atts = Constants.material.get(applyBean.getCLBH());
            if (null == atts) {
                atts = new ArrayList<>();
                Constants.material.put(applyBean.getCLBH(), atts);
            }
        }

        //初始化删除状态
        if (atts != null && atts.size() != 0) {
            for (ATTBean attBean : atts) {
                attBean.setDeleting(false);
            }
        }



        //初始化页面
        view.showAutoDelete(isEdit);

        view.isEnableAutoDelete(isHasUpload());

        setEdit(isEdit);
        view.dissMissEdit(getIsHideEditView());
        if (applyBean!=null&&applyBean.getCLMC() != null) {
            view.showMaterialName(applyBean.getCLMC());
        }

        //在办件隐藏编辑按钮
        view.showToolBarTv(!getIsHideEditView());

        //当前页为第一页，设置上一页按钮为灰色，且不可点击
        view.showIsPrevious(managerPosition != 0);

        //当前页为最后一页，设置下一页按钮为灰色，且不可点击
//        view.showIsNext(managerPosition != (FileUploadFragment.bigFileDate.size() - 1));
//
//        view.showPageNo(managerPosition + 1, FileUploadFragment.bigFileDate.size());
        /*新添加的代码start*/

//        view.showIsNext(managerPosition != (Upload.bigFileDate.size() - 1));
//
//        view.showPageNo(managerPosition + 1, Upload.bigFileDate.size());
        if (HistoreShare_v2.bigFileDate != null) {
            view.showIsNext(managerPosition != (HistoreShare_v2.bigFileDate.size() - 1));

            view.showPageNo(managerPosition + 1, HistoreShare_v2.bigFileDate.size());
        }

        /*end*/
    }

    @Override
    public boolean getIsHideEditView() {
        return STATUS != -1 && STATUS != 4 && STATUS != 9 /*&& STATUS != CooperateApplyOnlineActivity.STATUS_CONTINUE_APPLY*/;
    }

    @Override
    public boolean isManagerAtt(int managerPosition) {
        return this.managerPosition == managerPosition;
    }

    @Override
    public void autoDelete() {
        for (ATTBean attBean : getAtts()) {
            if (attBean.getUpStatus() == UploadStatus.UPLOAD_SUCCESS) {
                attBean.setTYPE("1");
                delete(attBean);
            }
        }
    }

    @Override
    public void delete(final ATTBean attBean) {
        attBean.setDeleting(true);
        Disposable disposable = zhengwuRepository.deleteAttach(attBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<String>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<String> value) {
                        KLog.e(new Gson().toJson(value));
                        view.dismissDialog();
                        switch (value.getCode()) {
                            case 200:
                                attBean.setDeleting(false);
                                view.removeListAtt(attBean);
//                                if (getAtts().size() == 0) {
//                                    FileUploadFragment.bigFileDate.get(managerPosition).setSTATUS("0");
//                                }
                                /*新添加的代码start*/
                                if (getAtts().size() == 0) {
//                                    Upload.bigFileDate.get(managerPosition).setSTATUS("0");
                                    HistoreShare_v2.bigFileDate.get(managerPosition).setSTATUS("0");
                                }
                                /*end*/
                                EventBus.getDefault().post(UPDATA_ATTS_STUTAS);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.dismissDialog();
                        attBean.setDeleting(false);
                        view.showToast("删除失败，请重试！");
                    }

                    @Override
                    public void onComplete() {
                        view.dismissDialog();
                        attBean.setDeleting(false);
                    }
                });
        disposables.add(disposable);
    }

    @Override
    public boolean isEdit() {
        return isEdit;
    }

    @Override
    public void setEdit(boolean edit) {
        isEdit = edit;
        view.showIsEdit(edit);
    }

    @Override
    public void onNext(Activity activity) {
        managerPosition++;
//        activity.getIntent().putExtra("applyBean", FileUploadFragment.bigFileDate.get(managerPosition));
        /*新添加的代码start*/
//        activity.getIntent().putExtra("applyBean", Upload.bigFileDate.get(managerPosition));
        activity.getIntent().putExtra("applyBean", HistoreShare_v2.bigFileDate.get(managerPosition));
        /*end*/
        activity.getIntent().putExtra("position", managerPosition);

        view.reView();
    }

    @Override
    public void onPrevious(Activity activity) {
        managerPosition--;
//        activity.getIntent().putExtra("applyBean", FileUploadFragment.bigFileDate.get(managerPosition));
        /*新添加的代码start*/
//        activity.getIntent().putExtra("applyBean", Upload.bigFileDate.get(managerPosition));
        activity.getIntent().putExtra("applyBean", HistoreShare_v2.bigFileDate.get(managerPosition));
        /*end*/
        activity.getIntent().putExtra("position", managerPosition);

        view.reView();
    }

    @Override
    public void addAtt(String path) {
        ATTBean attBean = new ATTBean(path);
        attBean.setATTACHNAME(getAttName(path));
        attBean.setManagerPosition(managerPosition);

        if (!NetworkUtils.isConnected(context)) {
            attBean.setUpStatus(UploadStatus.UPLOAD_ERROR);
        }

        getAtts().add(attBean);
        view.attsListNotify();

        //更新状态
        EventBus.getDefault().post(attBean);

        if (NetworkUtils.isConnected(context)) {
            uploadFile(attBean);
        }
    }

    @Override
    public void uploadFile(final ATTBean attBean) {
        //使用LinkedHashMap，保证文件按顺序上传
        Map<String, RequestBody> params = new LinkedHashMap<>();

        //普通key/value
//        params.put("USERCODE", RequestBody.create(MediaType.parse("multipart/form-data"), AccountHelper.getUser().getCODE()));
        params.put("TYPE", RequestBody.create(MediaType.parse("multipart/form-data"), "2"));
        params.put("ATTACHCODE", RequestBody.create(MediaType.parse("multipart/form-data"), applyBean.getCLBH()));
        params.put("FILENAME", RequestBody.create(MediaType.parse("multipart/form-data"), attBean.getATTACHNAME()));

        //设置上传状态位正在更新
        attBean.setUpStatus(UploadStatus.UPLOADING);
        File file = new File(attBean.getLocalPath());
        RequestBody filebody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //记录文件上传进度
        CountingRequestBody countingRequestBody = new CountingRequestBody(filebody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength) {
                double progress = (double) bytesWritten / (double) contentLength * 100 * 0.99;
                attBean.setProgress(progress);
                EventBus.getDefault().post(attBean);
            }
        });

        params.put("FileName\";filename=\"" + file.getName(), countingRequestBody);

        Disposable disposable = zhengwuRepository.uploadFile(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<GetUploadFileBean>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<GetUploadFileBean> value) {
                        if (value.getCode() == 200) {
                            attBean.setID(value.getReturnValue().getFILEID());
                            attBean.setATTACHURL(value.getReturnValue().getFILEPATH());
                            attBean.setUpStatus(UploadStatus.UPLOAD_SUCCESS);
                            SPUtils.putString(attBean.getID(), attBean.getLocalPath());

//                            FileUploadFragment.bigFileDate.get(managerPosition).setSTATUS("1");
                            /*新添加的代码start*/
//                            if (Upload.bigFileDate.size() > managerPosition) {
//                                Upload.bigFileDate.get(managerPosition).setSTATUS("1");
//                            }
                            if (HistoreShare_v2.bigFileDate.size() > managerPosition) {
                                HistoreShare_v2.bigFileDate.get(managerPosition).setSTATUS("1");
                            }
                            /*end*/
                        } else {
                            attBean.setUpStatus(UploadStatus.UPLOAD_ERROR);
                        }
                        EventBus.getDefault().post(UPDATA_ATTS_STUTAS);
                        EventBus.getDefault().post(attBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.d("ful", "-----------------失败");
                        attBean.setUpStatus(UploadStatus.UPLOAD_ERROR);
                        EventBus.getDefault().post(UPDATA_ATTS_STUTAS);
                        EventBus.getDefault().post(attBean);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(disposable);
    }

    @Override
    public boolean isAllUpload() {
        boolean isUpload = true;
        for (ATTBean attBean : getAtts()) {
            if (attBean.getUpStatus() == UploadStatus.UPLOADING) {
                isUpload = false;
            }
        }
        return isUpload;
    }

    @Override
    public boolean isHasUpload() {
        if (getAtts() != null && getAtts().size() != 0) {
            for (ATTBean attBean : getAtts()) {
                if (attBean.getUpStatus() == UploadStatus.UPLOAD_SUCCESS) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void setAttsEdit(boolean Edit) {
        for (ATTBean attBean : getAtts()) {
            attBean.setEdit(Edit);
        }
    }

    @Override
    public ATTBean getAtt(int position) {
        if (atts.size() != 0 && atts.size() > position) {
            return atts.get(position);
        } else {
            return null;
        }
    }

    @Override
    public List<ATTBean> getAtts() {
        return atts;
    }

    public String getAttName(String path) {
        int indexMaterial = 0;

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < atts.size(); i++) {
            String splitDotStr = atts.get(i).getATTACHNAME().split("\\.")[0];
            String numberStr = splitDotStr.split("_")[splitDotStr.split("_").length - 1];
            if (!TextUtils.isEmpty(numberStr) && splitDotStr.split("_").length == 2) {
                if (NumberUtils.isNumeric(numberStr)) {
                    numbers.add(Integer.parseInt(numberStr));
                }
            }
        }
        if (numbers.size() > 0) {
            indexMaterial = Collections.max(numbers);
        }

        //计数递增1
        indexMaterial++;

        File file = new File(path);
        String lastAttachName = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.US);
        String attachWholeName;
        if (!TextUtils.isEmpty(lastAttachName)) {
            attachWholeName = nameEnd + "_" + indexMaterial + "." + lastAttachName;
        } else {
            attachWholeName = nameEnd + "_" + indexMaterial;
        }

        return attachWholeName;
    }
}

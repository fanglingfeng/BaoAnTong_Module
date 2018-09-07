package com.tjsoft.webhall.ui.dzzzk;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.ui.dzzzk
 *  @文件名:   DZZZKPresenter
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 16:08
 *  @描述：    TODO
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bean.BaseResponseReturnValue;
import com.bean.GetUploadFileBean;
import com.socks.library.KLog;
import com.tjsoft.data.DownloadRepository;
import com.tjsoft.data.ProgressResponseBody;
import com.tjsoft.data.ZhengwuRepository;
import com.tjsoft.data.local.LocalDownloadDataSource;
import com.tjsoft.data.local.LocalZhengwuDataSource;
import com.tjsoft.data.remote.RemoteDownloadDataSource;
import com.tjsoft.data.remote.RemoteZhengwuDataSource;
import com.tjsoft.util.FileUtil;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.constants.PermissionCode;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.ChakanSendbean;
import com.tjsoft.webhall.entity.DZZZKBean;
import com.tjsoft.webhall.entity.DZZZKSendbean;
import com.tjsoft.webhall.entity.DZZZKUrlSendbean;
import com.tjsoft.webhall.entity.DZZZKUrlbean;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.ui.materialmanage.CountingRequestBody;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;

import net.liang.appbaselibrary.base.mvp.BasePresenter;
import net.liang.appbaselibrary.bean.ResponseCode;
import net.liang.appbaselibrary.utils.NetworkUtils;
import net.liang.appbaselibrary.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.tjsoft.webhall.constants.BusConstant.UPDATA_ATTS_STUTAS;

public class DZZZKPresenter
        extends BasePresenter
        implements DZZZKContract.Presenter
{
    private int mProgress = 0;
    private ZhengwuRepository zhengwuRepository;

    @NonNull
    protected DZZZKContract.View mView;
    @NonNull
    protected ZhengwuRepository  repository;
    @NonNull
    private   DownloadRepository downloadRepository;
    private   Context            context;
    private   List<ATTBean>      atts;// 文件集合

    public DZZZKPresenter(DZZZKContract.View mView) {
        this.mView = checkNotNull(mView);
        repository = ZhengwuRepository.getInstance(RemoteZhengwuDataSource.getInstance(),
                                                   LocalZhengwuDataSource.getInstance());
        downloadRepository = DownloadRepository.getInstance(RemoteDownloadDataSource.getInstance(),
                                                            LocalDownloadDataSource.getInstance());
        zhengwuRepository = new ZhengwuRepository(RemoteZhengwuDataSource.getInstance(),
                                                  LocalZhengwuDataSource.getInstance());


    }


    @Override
    public void getDZZZKList(String token, String ZJHM, String SXBM, String SXMC, Context context) {
        this.context = context;
        mView.showProgressDialog("正在加载...");
        DZZZKSendbean dzzzkSendbean = new DZZZKSendbean();
        dzzzkSendbean.setToken(token);
        dzzzkSendbean.setSFZJHM(ZJHM);
        dzzzkSendbean.setSXBM(SXBM);
        dzzzkSendbean.setSXMC(SXMC);


        Disposable disposable = repository.getDZZZKList(dzzzkSendbean, context)
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribeWith(new DisposableObserver<BaseResponseReturnValue<DZZZKBean>>() {
                                              @Override
                                              public void onNext(BaseResponseReturnValue<DZZZKBean> value) {
                                                  if (value != null) {
                                                      if (value.getCode() == ResponseCode.success) {
                                                          if (value.getReturnValue() != null) {
                                                              mView.dismissProgressDialog();

                                                              mView.showDZZZKList(value.getReturnValue()
                                                                                       .getZZDATA());
                                                          } else {

                                                              mView.showToast("未获取到电子证照列表！");
                                                              mView.dismissProgressDialog();

                                                          }
                                                      } else {
                                                          mView.dismissProgressDialog();

                                                          mView.showToast(value.getError());
                                                      }
                                                  } else {

                                                      mView.showToast("未获取到电子证照列表！");
                                                      mView.dismissProgressDialog();

                                                  }

                                              }

                                              @Override
                                              public void onError(Throwable e) {
                                                  mView.dismissProgressDialog();

                                              }

                                              @Override
                                              public void onComplete() {

                                              }
                                          });
        disposables.add(disposable);


    }

    @Override
    public void downloadUrl(final ChakanResponsebean chakanResponsebean,
                            ApplyBean applyBean,
                            int position,
                            String authcode)
    {
        mView.showProgressDialog("正在加载...");
        //        String externalStoragePermissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //        RxPermissions.getInstance(AppConfig.getInstance()).request(externalStoragePermissions)
        //                     .subscribeWith(new DisposableObserver<Boolean>() {
        //                         @Override
        //                         public void onNext(Boolean value) {
        //                             if (value) {
        //                                 startDownload(chakanResponsebean);
        //                             } else {
        //                                 mView.showSetPermissionDialog("读写sd卡");
        //                                 mView.dismissProgressDialog();
        //
        //
        //                             }
        //                         }
        //
        //                         @Override
        //                         public void onError(Throwable e) {
        //                            mView.dismissProgressDialog();
        //                         }
        //
        //                         @Override
        //                         public void onComplete() {
        //
        //                         }
        //                     });
        if (Environment.getExternalStorageState()
                       .equals(Environment.MEDIA_MOUNTED))
        {


            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                startDownload(chakanResponsebean, applyBean, position, authcode);
            } else {
                ActivityCompat.requestPermissions((DZZZKActivity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionCode.MY_PERMISSIONS_WRITE);
            }



        } else {
            mView.showToast("请先挂载sd卡");
        }
        mView.dismissProgressDialog();


    }

    private void startDownload(ChakanResponsebean chakanResponsebean,
                               final ApplyBean applyBean,
                               final int position,
                               final String authcode)
    {
        File fileTemp = null;

        fileTemp = new File(Environment.getExternalStorageDirectory()
                                       .getPath() + "/download",
                            chakanResponsebean.getZZDATA()
                                              .getFILENAME());

        if (fileTemp.exists() && fileTemp.length() > 0) {
            handlerDownResult(fileTemp, applyBean, position, authcode);
            return;
        }
        String url;
        url = Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + chakanResponsebean.getZZDATA()
                                                                                           .getFILEID();
        final File finalFileTemp = fileTemp;
        Disposable disposable = downloadRepository.downloadAttach(url,
                                                                  new ProgressResponseBody.ProgressListener() {
                                                                      @Override
                                                                      public void update(long bytesRead,
                                                                                         long contentLength,
                                                                                         boolean done)
                                                                      {
                                                                          KLog.e("update mProgress ==" + mProgress);
                                                                          if (contentLength <= 0) {
                                                                              mView.showToast(
                                                                                      "文件不存在！");
                                                                              return;
                                                                          }
                                                                          int progress = (int) (bytesRead * 100 / contentLength);
                                                                          mProgress = progress;
                                                                      }
                                                                  })
                                                  .map(new Function<ResponseBody, File>() {
                                                      @Override
                                                      public File apply(ResponseBody responseBody)
                                                              throws Exception
                                                      {


                                                          FileUtil.saveDownloadFile(responseBody.byteStream(),
                                                                                    finalFileTemp);
                                                          return finalFileTemp;
                                                      }
                                                  })
                                                  .subscribeOn(Schedulers.io())
                                                  .observeOn(AndroidSchedulers.mainThread())
                                                  .subscribeWith(new DisposableObserver<File>() {
                                                      @Override
                                                      public void onNext(File value) {
                                                          KLog.e("onnext mProgress ==" + mProgress);
                                                          mView.dismissProgressDialog();

                                                          if (value != null && value.isFile() && mProgress == 100) {
                                                              handlerDownResult(value,
                                                                                applyBean,
                                                                                position,
                                                                                authcode);
                                                          } else {

                                                          }
                                                      }

                                                      @Override
                                                      public void onError(Throwable e) {
                                                          mView.showNetworkFail();
                                                      }

                                                      @Override
                                                      public void onComplete() {

                                                      }
                                                  });
        disposables.add(disposable);


    }

    private void handlerDownResult(File value, ApplyBean applyBean, int position, String authcode) {
        //        String fileName = value.getName();
        //                            /* 取得扩展名 */
        //        String end = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase(
        //                Locale.US);//
        //        boolean fileClickable = true;
        //        if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(end)) {
        //            //                                fileClickable = true;
        //            mView.showPreviewLayout(value.getAbsolutePath());
        //        } else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(end) || "dotx".equals(end) || "dotm".equals(end)) {
        //            mView.showPreviewLayout(R.mipmap.ic_word);
        //        } else if ("pdf".equals(end)) {
        //            mView.showPreviewLayout(R.mipmap.ic_pdf);
        //        } else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end) || "xltm".equals(end) || "xlsb".equals(end)
        //                || "xlam".equals(end)) {
        //            mView.showPreviewLayout(R.mipmap.ic_excel);
        //        } else {
        //            mView.showPreviewLayout(R.mipmap.ic_file_unknown);
        //        }
        if (applyBean == null) {
            mView.showPreviewLayout(value.getAbsolutePath(), applyBean);
            mView.showToast("下载成功！");

        } else {
            addAtt(value.getAbsolutePath(), applyBean, position, authcode);
        }

    }

    public void addAtt(String path, ApplyBean applyBean, int managerPosition, String authcode) {
        ATTBean attBean = new ATTBean(path);
        attBean.setATTACHNAME(getAttName(path));
        attBean.setManagerPosition(managerPosition);

        if (!NetworkUtils.isConnected(context)) {
            attBean.setUpStatus(UploadStatus.UPLOAD_ERROR);
        }

        getAtts(applyBean).add(attBean);

        //更新状态
        EventBus.getDefault()
                .post(attBean);

        if (NetworkUtils.isConnected(context)) {
            uploadFile(attBean, applyBean, managerPosition, authcode);
        }
    }

    public List<ATTBean> getAtts(ApplyBean applyBean) {
        if (null != applyBean) {
            atts = Constants.material.get(applyBean.getCLBH());
            if (null == atts) {
                atts = new ArrayList<>();
                Constants.material.put(applyBean.getCLBH(), atts);
            }
        }


        return atts;
    }


    private String getAttName(String path) {

        File   file           = new File(path);
        String lastAttachName = file.getName();
        return lastAttachName;
    }

    public void uploadFile(final ATTBean attBean,
                           final ApplyBean applyBean,
                           final int managerPosition,
                           final String authcode)
    {
        //使用LinkedHashMap，保证文件按顺序上传
        Map<String, RequestBody> params = new LinkedHashMap<>();

        //普通key/value
        //        params.put("USERCODE", RequestBody.create(MediaType.parse("multipart/form-data"), AccountHelper.getUser().getCODE()));
        params.put("TYPE", RequestBody.create(MediaType.parse("multipart/form-data"), "2"));
        params.put("ATTACHCODE",
                   RequestBody.create(MediaType.parse("multipart/form-data"), applyBean.getCLBH()));
        params.put("FILENAME",
                   RequestBody.create(MediaType.parse("multipart/form-data"),
                                      attBean.getATTACHNAME()));

        //设置上传状态位正在更新
        attBean.setUpStatus(UploadStatus.UPLOADING);
        File        file     = new File(attBean.getLocalPath());
        RequestBody filebody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //记录文件上传进度
        CountingRequestBody countingRequestBody = new CountingRequestBody(filebody,
                                                                          new CountingRequestBody.Listener() {
                                                                              @Override
                                                                              public void onRequestProgress(
                                                                                      long bytesWritten,
                                                                                      long contentLength)
                                                                              {
                                                                                  double progress = (double) bytesWritten / (double) contentLength * 100 * 0.99;
                                                                                  attBean.setProgress(
                                                                                          progress);
                                                                                  EventBus.getDefault()
                                                                                          .post(attBean);
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
                                                             attBean.setID(value.getReturnValue()
                                                                                .getFILEID());
                                                             attBean.setATTACHURL(value.getReturnValue()
                                                                                       .getFILEPATH());
                                                             attBean.setUpStatus(UploadStatus.UPLOAD_SUCCESS);
                                                             SPUtils.putString(attBean.getID(),
                                                                               attBean.getLocalPath());

                                                             //                            FileUploadFragment.bigFileDate.get(managerPosition).setSTATUS("1");
                            /*新添加的代码start*/
                                                             //                            if (Upload.bigFileDate.size() > managerPosition) {
                                                             //                                Upload.bigFileDate.get(managerPosition).setSTATUS("1");
                                                             //                            }
                                                             if (HistoreShare_v2.bigFileDate.size() > managerPosition) {
                                                                 HistoreShare_v2.bigFileDate.get(
                                                                         managerPosition)
                                                                                            .setSTATUS(
                                                                                                    "1");
                                                             }
                            /*end*/
                                                             List<ApplyBean> myapplybeans = new ArrayList<>();



                                                             for (int i = 0; i < HistoreShare_v2.bigFileDate.size(); i++)
                                                             {
                                                                 myapplybeans.add(HistoreShare_v2.bigFileDate.get(i));
                                                             }
                                                             myapplybeans.get(managerPosition)
                                                                         .setIsselected(true);
                                                             DZZZKActivity.applybeans.put(authcode,
                                                                                          myapplybeans);
                                                             mView.refreshList();
                                                             mView.showToast("添加证照成功");
                                                         } else {
                                                             attBean.setUpStatus(UploadStatus.UPLOAD_ERROR);
                                                             mView.showToast("添加证照失败");

                                                         }
                                                         EventBus.getDefault()
                                                                 .post(UPDATA_ATTS_STUTAS);
                                                         EventBus.getDefault()
                                                                 .post(attBean);
                                                     }

                                                     @Override
                                                     public void onError(Throwable e) {
                                                         KLog.d("ful", "-----------------失败");
                                                         mView.showToast("添加证照失败");

                                                         attBean.setUpStatus(UploadStatus.UPLOAD_ERROR);
                                                         EventBus.getDefault()
                                                                 .post(UPDATA_ATTS_STUTAS);
                                                         EventBus.getDefault()
                                                                 .post(attBean);
                                                     }

                                                     @Override
                                                     public void onComplete() {

                                                     }
                                                 });
        disposables.add(disposable);
    }

    @Override
    public void chakan(String token,
                       final String AUTHCODE,
                       final String filename,
                       Context context,
                       final ApplyBean applyBean,
                       final int position)
    {
        mView.showProgressDialog("正在加载...");
        ChakanSendbean chakanSendbean = new ChakanSendbean();
        chakanSendbean.setToken(token);
        chakanSendbean.setAUTHCODE(AUTHCODE);


        Disposable disposable = repository.chakan(chakanSendbean, context)
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribeWith(new DisposableObserver<BaseResponseReturnValue<ChakanResponsebean>>() {
                                              @Override
                                              public void onNext(BaseResponseReturnValue<ChakanResponsebean> value) {
                                                  if (value != null) {
                                                      if (value.getCode() == ResponseCode.success) {
                                                          if (value.getReturnValue() != null) {
                                                              mView.dismissProgressDialog();


                                                              mView.showChakan(value.getReturnValue(),
                                                                               applyBean,
                                                                               position,
                                                                               AUTHCODE);

                                                          } else {

                                                              mView.showToast("未获取到电子证照信息！");
                                                              mView.dismissProgressDialog();

                                                          }
                                                      } else {
                                                          mView.dismissProgressDialog();

                                                          mView.showToast(value.getError());
                                                      }
                                                  } else {

                                                      mView.showToast("未获取到电子证照信息！");
                                                      mView.dismissProgressDialog();

                                                  }

                                              }

                                              @Override
                                              public void onError(Throwable e) {
                                                  mView.dismissProgressDialog();

                                              }

                                              @Override
                                              public void onComplete() {

                                              }
                                          });
        disposables.add(disposable);


    }

    @Override
    public void getUrl(String token, ZZDATABean zzdataBean, Context context) {

        DZZZKUrlSendbean dzzzkUrlSendbean = new DZZZKUrlSendbean();
        dzzzkUrlSendbean.setToken(token);
        dzzzkUrlSendbean.setAUTHCODE(zzdataBean.getAUTHCODE());


        Disposable disposable = repository.getUrl(dzzzkUrlSendbean, context)
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribeWith(new DisposableObserver<BaseResponseReturnValue<DZZZKUrlbean>>() {
                                              @Override
                                              public void onNext(BaseResponseReturnValue<DZZZKUrlbean> value) {
                                                  if (value != null) {
                                                      if (value.getCode() == ResponseCode.success) {
                                                          if (value.getReturnValue() != null) {

                                                              mView.showUrl(value.getReturnValue()
                                                                                 .getONEOFFURL());
                                                          } else {

                                                              mView.showToast("未获取到查看地址！");
                                                          }
                                                      } else {

                                                          mView.showToast(value.getError());
                                                      }
                                                  } else {

                                                      mView.showToast("未获取到查看地址！");
                                                  }

                                              }

                                              @Override
                                              public void onError(Throwable e) {


                                              }

                                              @Override
                                              public void onComplete() {

                                              }
                                          });
        disposables.add(disposable);


    }
}

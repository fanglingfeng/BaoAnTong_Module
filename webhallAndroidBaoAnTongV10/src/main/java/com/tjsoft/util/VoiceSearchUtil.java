package com.tjsoft.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.tjsoft.webhall.ui.wsbs.WSBS1;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("NewApi")
public class VoiceSearchUtil {
	private Context mContext;
	private EditText textSearch;
	private Button searchTv;
	/**
	 * ==================
	 */
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private SharedPreferences mSharedPreferences;
    public static final String PREFER_NAME = "com.tjsoft.msfw.guangdongshenzhenbaoan";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
	public VoiceSearchUtil(Context context,EditText textSearch,Button searchTv){
		this.mContext=context;
		this.textSearch=textSearch;
		this.searchTv=searchTv;
		Init();
	}
    
    /**
     * 自己的5858b2b1  如自己demo要运行 需要更改自己的     华讯的5837aeb8
     */
	public void Init(){
		SpeechUtility.createUtility(mContext, SpeechConstant.APPID +"=5837aeb8");   
		mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);

        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        mSharedPreferences = mContext.getSharedPreferences(PREFER_NAME, mContext.MODE_PRIVATE);
	}
	/**
	 * 重新设置可以回调自动点击搜索
	 */
	public void setCallClick(boolean isClick){
		isCallClick = isClick;
	}
	/**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("SPS==","SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
            	DialogUtil.showUIToast(mContext, "初始化失败，错误码：" + code);
//                dialogHelper.toast("初始化失败，错误码：" + code, Toast.LENGTH_SHORT);
            }
        }
    };


    public void startListenVoice() {
    	textSearch.setText(null);// 清空显示内容
        mIatResults.clear();
        // 设置参数
        setParam();
        mIatDialog.setListener(recognizerDialogListener);
        mIatDialog.show();
    }


    /**
     * 识别参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式仅为pcm，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");

        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA, mSharedPreferences.getString("iat_dwa_preference", "0"));
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
//            dialogHelper.toast(error.getPlainDescription(true),Toast.LENGTH_SHORT);
        }

    };

    //发现这里会回调多次，加一个状态码，防止多次执行callOnClick
    private boolean isCallClick = false;

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        textSearch.setText(resultBuffer.toString());
        textSearch.setSelection(textSearch.length());
        if (!isCallClick) {
            searchTv.callOnClick();
            isCallClick = true;
        }

    }
}

package com.tjsoft.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.tjsoft.webhall.constants.Constants;

import android.os.Handler;
import android.os.Message;

/**
 * WebService访问方式的工具类
 */
public class WebServiceUtils {
    // 访问的服务器是否由dotNet开发
    public static boolean isDotNet = false;
    // 线程池的大小
    private static int threadSize = 5;
    // 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程
    private static ExecutorService threadPool = Executors.newFixedThreadPool(threadSize);
    // 连接响应标示
    public static final int SUCCESS_FLAG = 0;
    public static final int ERROR_FLAG = 1;

    /**
     * 调用WebService接口，此方法只访问过用Java写的服务器
     *
     * @param endPoint        WebService服务器地址
     * @param nameSpace       命名空间
     * @param methodName      WebService的调用方法名
     * @param mapParams       WebService的参数集合，可以为null
     * @param reponseCallBack 服务器响应接口
     */
    public static void call(final String service,
                            final String methodName,
                            final JSONObject param,
                            final Response reponseCallBack) {
        // 1.创建HttpTransportSE对象，传递WebService服务器地址
        final HttpTransportSE transport = new HttpTransportSE(Constants.DOMAIN+"services/"+service);
        transport.debug = true;
        // 2.创建SoapObject对象用于传递请求参数
        final SoapObject request = new SoapObject(Constants.NAMESPACE, methodName);
        
        request.addProperty("param", param.toString());
        // 3.实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = isDotNet; // 设置是否调用的是.Net开发的WebService
        envelope.setOutputSoapObject(request);

        // 4.用于子线程与主线程通信的Handler，网络请求成功时会在子线程发送一个消息，然后在主线程上接收
        final Handler responseHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 根据消息的arg1值判断调用哪个接口
                if (msg.arg1 == SUCCESS_FLAG)
                    reponseCallBack.onSuccess(msg.obj.toString());
                else
                    reponseCallBack.onError((Exception) msg.obj);
            }

        };

        // 5.提交一个子线程到线程池并在此线种内调用WebService
        if (threadPool == null || threadPool.isShutdown())
            threadPool = Executors.newFixedThreadPool(threadSize);
        threadPool.submit(new Runnable() {

            @Override
            public void run() {
                String result = "";
                try {
                    // 解决EOFException
                    System.setProperty("http.keepAlive", "false");
                    // 连接服务器
                    transport.call(null, envelope);
                    if (envelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        result = envelope.getResponse().toString();
                    }
                } catch (IOException e) {
                    // 当call方法的第一个参数为null时会有一定的概念抛IO异常
                    // 因此需要需要捕捉此异常后用命名空间加方法名作为参数重新连接
                    e.printStackTrace();
                    try {
                        transport.call(Constants.NAMESPACE + methodName, envelope);
                        if (envelope.getResponse() != null) {
                            // 获取服务器响应返回的SoapObject
                        	result = envelope.getResponse().toString();
                        }
                    } catch (Exception e1) {
                        // e1.printStackTrace();
                        responseHandler.sendMessage(responseHandler.obtainMessage(0, ERROR_FLAG, 0, e1));
                    }
                } catch (XmlPullParserException e) {
                    // e.printStackTrace();
                    responseHandler.sendMessage(responseHandler.obtainMessage(0, ERROR_FLAG, 0, e));
                } finally {
                    // 将获取的消息利用Handler发送到主线程
                    responseHandler.sendMessage(responseHandler.obtainMessage(0, SUCCESS_FLAG, 0, result));
                }
            }
        });
    }

    /**
     * 设置线程池的大小
     *
     * @param threadSize
     */
    public static void setThreadSize(int threadSize) {
        WebServiceUtils.threadSize = threadSize;
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(WebServiceUtils.threadSize);
    }

    /**
     * 服务器响应接口，在响应后需要回调此接口
     */
    public interface Response {
        public void onSuccess(String result);

        public void onError(Exception e);
    }

}
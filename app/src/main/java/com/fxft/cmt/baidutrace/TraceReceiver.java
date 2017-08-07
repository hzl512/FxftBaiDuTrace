package com.fxft.cmt.baidutrace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;

import static com.fxft.cmt.baidutrace.Constants.BAIDU_TRACE_SERVICE_ID;
import static com.fxft.cmt.baidutrace.Constants.DEFAULT_PACK_INTERVAL;
import static com.fxft.cmt.baidutrace.Constants.LOC_INTERVAL;

/**
 * 百度鹰眼服务启动的广播监听
 */
public class TraceReceiver extends BroadcastReceiver {

    private static final String TAG = "baiduTrace";

    // 初始化轨迹服务
    private Trace mTrace=null;
    // 初始化轨迹服务客户端
    private LBSTraceClient mTraceClient = null;
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    private boolean mIsNeedObjectStorage = false;
    //轨迹服务监听器
    private OnTraceListener mTraceListener = null;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;
        LogUtil.v(TAG,"TraceReceiver onReceive");
        boolean isInit=intent.getBooleanExtra("isInit",false);
        if (isInit){
            LogUtil.v(TAG," TraceReceiver deviceID： "+intent.getStringExtra("deviceID"));
            location(intent.getStringExtra("deviceID"));
        }
    }

    /**
     * 百度鹰眼定位
     * @param deviceID
     */
    private void location(String deviceID) {
        // 初始化轨迹服务
        mTrace = new Trace(BAIDU_TRACE_SERVICE_ID, deviceID, mIsNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(mContext);
        // 高精度定位模式，GPS与网络综合定位
        mTraceClient.setLocationMode(LocationMode.High_Accuracy);
        // 设置定位和打包周期
        mTraceClient.setInterval(LOC_INTERVAL, DEFAULT_PACK_INTERVAL);
        // 开启服务
        mTraceClient.startTrace(mTrace, mTraceListener);
        // 开启采集
        mTraceClient.startGather(mTraceListener);
        // 初始化轨迹服务监听器
        mTraceListener = new OnTraceListener() {

            /**
             * 绑定服务回调接口
             * @param errorNo  状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>1：失败</pre>
             */
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                LogUtil.v(TAG,String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 开启服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>10000：请求发送失败</pre>
             *                <pre>10001：服务开启失败</pre>
             *                <pre>10002：参数错误</pre>
             *                <pre>10003：网络连接失败</pre>
             *                <pre>10004：网络未开启</pre>
             *                <pre>10005：服务正在开启</pre>
             *                <pre>10006：服务已开启</pre>
             */
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                LogUtil.v(TAG, String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 停止服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>11000：请求发送失败</pre>
             *                <pre>11001：服务停止失败</pre>
             *                <pre>11002：服务未开启</pre>
             *                <pre>11003：服务正在停止</pre>
             */
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                LogUtil.v(TAG, String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 开启采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>12000：请求发送失败</pre>
             *                <pre>12001：采集开启失败</pre>
             *                <pre>12002：服务未开启</pre>
             */
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                LogUtil.v(TAG, String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 停止采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>13000：请求发送失败</pre>
             *                <pre>13001：采集停止失败</pre>
             *                <pre>13002：服务未开启</pre>
             */
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                LogUtil.v(TAG, String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 推送消息回调接口
             *
             * @param messageType 状态码
             * @param pushMessage 消息
             *                  <p>
             *                  <pre>0x01：配置下发</pre>
             *                  <pre>0x02：语音消息</pre>
             *                  <pre>0x03：服务端围栏报警消息</pre>
             *                  <pre>0x04：本地围栏报警消息</pre>
             *                  <pre>0x05~0x40：系统预留</pre>
             *                  <pre>0x41~0xFF：开发者自定义</pre>
             */
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {
            }
        };
    }

}

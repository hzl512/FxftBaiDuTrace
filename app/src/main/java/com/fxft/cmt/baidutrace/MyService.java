package com.fxft.cmt.baidutrace;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import static com.fxft.cmt.baidutrace.Constants.TAG;


/**
 * 百度鹰眼定位的服务（启动获取设备ID的广播）
 */
public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //启动百度鹰眼
                sendBroadcast(new Intent("Fxft_Cmt_Mutual").putExtra("Action_Type", 1));
                LogUtil.v(TAG,"MyService onCreate");
            }
        }, 5 * 1000);
        improvePriority();
    }

    private void improvePriority() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MyService.class), 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Foreground Service")
                .setContentText("Foreground Service Started.")
                .setSmallIcon(R.mipmap.ic_launcher).build();
        notification.contentIntent = contentIntent;
        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);//从前台状态删除此服务，允许它被杀死
    }
}

package com.sky.android.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sky.android.aidl.common.DemoAidlInterface;
import com.sky.android.aidl.common.DemoAidlListener;
import com.sky.android.aidl.common.DemoData;
import com.sky.android.aidl.common.util.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sky on 16-10-10.
 */

public class DemoRemoteService extends Service {

    public static final String TAG = DemoRemoteService.class.getSimpleName();

    private List<DemoAidlListener> mListeners;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new DemoAidlStub();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "创建远程服务");

        if (mListeners == null) {
            mListeners = new ArrayList<>();
            mTimer = new Timer("demo");
            mTimer.schedule(new DemoTask(), 1000, 8000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "停止远程服务");

        if (mListeners != null) {
            mTimer.cancel();
            mListeners.clear();
            mListeners = null;
        }
    }

    private class DemoAidlStub extends DemoAidlInterface.Stub {

        @Override
        public int getVersionCode(String packageName) throws RemoteException {
            return AppUtil.getAppVersionCode(getBaseContext(), packageName);
        }

        @Override
        public String getVersionName(String packageName) throws RemoteException {
            return AppUtil.getAppVersionName(getBaseContext(), packageName);
        }

        @Override
        public void register(DemoAidlListener listener) throws RemoteException {
            mListeners.add(listener);
            Log.d(TAG, "添加监听");
        }

        @Override
        public void unregister(DemoAidlListener listener) throws RemoteException {
            mListeners.remove(listener);
            Log.d(TAG, "移除监听");
        }
    }

    private class DemoTask extends TimerTask {

        @Override
        public void run() {

            if (mListeners == null || mListeners.isEmpty()) return ;

//            Log.d(TAG, "开始执行任务");

            for (DemoAidlListener listener : mListeners) {
                onHandler(listener);
            }

//            Log.d(TAG, "结束执行任务");
        }

        private void onHandler(DemoAidlListener listener) {

            if (listener == null) return ;

            try {
                // 模拟定时通知
                String msg = "Time: " + System.currentTimeMillis();
                DemoData data = new DemoData("demo", "123456");
                listener.onNotification(msg, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

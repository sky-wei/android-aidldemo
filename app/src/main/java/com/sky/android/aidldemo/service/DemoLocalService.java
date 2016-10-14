package com.sky.android.aidldemo.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sky.android.aidl.common.DemoAidlInterface;
import com.sky.android.aidl.common.DemoAidlListener;
import com.sky.android.aidl.common.DemoData;

/**
 * Created by sky on 16-10-10.
 */

public class DemoLocalService extends Service {

    private static final String TAG = DemoLocalService.class.getSimpleName();

    private DemoLocalService.RemoteServiceConnection mRemoteServiceConnection;
    private static DemoAidlInterface mDemoAidlInterface;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static DemoAidlInterface getDemoAidlInterface() {
        return mDemoAidlInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mRemoteServiceConnection == null) {
            Intent intent = new Intent("action.com.sky.aidldemo.DemoRemoteService");
            intent.setPackage("com.sky.android.aidl.service");
            mRemoteServiceConnection = new DemoLocalService.RemoteServiceConnection();
            bindService(intent, mRemoteServiceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mRemoteServiceConnection != null) {
            unregister();
            unbindService(mRemoteServiceConnection);
            mRemoteServiceConnection = null;
        }
    }

    private void register() {

        if (mDemoAidlInterface == null) return ;

        try {
            mDemoAidlInterface.register(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void unregister() {

        if (mDemoAidlInterface == null) return ;

        try {
            mDemoAidlInterface.unregister(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mDemoAidlInterface = null;
    }

    private final DemoAidlListener.Stub listener = new DemoAidlListener.Stub() {

        @Override
        public void onNotification(String msg, DemoData data) throws RemoteException {
            // 通知
            Log.d(TAG, "Notification: " + msg + " --> " + data);
        }
    };

    private final class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDemoAidlInterface = DemoAidlInterface.Stub.asInterface(service);
            register();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDemoAidlInterface = null;
        }
    }
}

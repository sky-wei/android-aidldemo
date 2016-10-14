package com.sky.android.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sky.android.aidl.common.DemoAidlInterface;
import com.sky.android.aidl.common.DemoAidlListener;
import com.sky.android.aidl.common.DemoData;
import com.sky.android.aidldemo.event.DemoEvent;
import com.sky.android.aidldemo.service.DemoLocalService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RemoteServiceConnection mRemoteServiceConnection;
    private DemoAidlInterface mDemoAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DemoLocalService.class);
        startService(intent);

        findViewById(R.id.btn_test1).setOnClickListener(this);
        findViewById(R.id.btn_test2).setOnClickListener(this);
    }

    private void testAidl(DemoAidlInterface demoAidlInterface) {

        if (demoAidlInterface == null) return ;

        try {
            int versionCode = demoAidlInterface.getVersionCode(getPackageName());
            String versionName = demoAidlInterface.getVersionName(getPackageName());

            Toast.makeText(this, "VN: " + versionName + ", VC: " + versionCode, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        if (mRemoteServiceConnection == null) {
            Intent intent = new Intent("action.com.sky.aidldemo.DemoRemoteService");
            intent.setPackage("com.sky.android.aidl.service");
            mRemoteServiceConnection = new RemoteServiceConnection();
            bindService(intent, mRemoteServiceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

        if (mRemoteServiceConnection != null) {
            unregister();
            unbindService(mRemoteServiceConnection);
            mRemoteServiceConnection = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, DemoLocalService.class);
        stopService(intent);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(DemoEvent event) {

        switch (event.getEventId()) {
            case DemoEvent.BIND_SUCCESS:
                // 注册
                register();
                break;
            case DemoEvent.BIND_FAILURE:
                mDemoAidlInterface = null;
                mRemoteServiceConnection = null;
                break;
            case DemoEvent.NOTIFICATION:
                Toast.makeText(this, "" + event.getData(), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private final DemoAidlListener.Stub listener = new DemoAidlListener.Stub() {

        @Override
        public void onNotification(String msg, DemoData data) throws RemoteException {
            // 通知
            EventBus.getDefault().post(new DemoEvent(DemoEvent.NOTIFICATION, msg + ", " + data.getName()));
        }
    };

    @Override
    public void onClick(View v) {

        if (R.id.btn_test1 == v.getId()) {

            // 测试1
            testAidl(mDemoAidlInterface);
        } else if (R.id.btn_test2 == v.getId()) {

            // 测试2
            testAidl(DemoLocalService.getDemoAidlInterface());
        }
    }

    private final class RemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDemoAidlInterface = DemoAidlInterface.Stub.asInterface(service);
            EventBus.getDefault().post(new DemoEvent(DemoEvent.BIND_SUCCESS));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            EventBus.getDefault().post(new DemoEvent(DemoEvent.BIND_FAILURE));
        }
    }
}

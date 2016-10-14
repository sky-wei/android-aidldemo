// DemoAidlInterface.aidl
package com.sky.android.aidl.common;

// Declare any non-default types here with import statements
import com.sky.android.aidl.common.DemoAidlListener;

interface DemoAidlInterface {

    int getVersionCode(String packageName);

    String getVersionName(String packageName);

    void register(in DemoAidlListener listener);

    void unregister(in DemoAidlListener listener);
}

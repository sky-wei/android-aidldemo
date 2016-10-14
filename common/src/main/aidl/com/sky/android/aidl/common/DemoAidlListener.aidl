// DemoAidlListener.aidl
package com.sky.android.aidl.common;

// Declare any non-default types here with import statements
import com.sky.android.aidl.common.DemoData;

interface DemoAidlListener {

    void onNotification(String msg,in DemoData data);
}

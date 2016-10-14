package com.sky.android.aidl.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sky on 16-10-10.
 */

public class DemoData implements Parcelable {

    private String name;
    private String value;

    public DemoData() {}

    public DemoData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    protected DemoData(Parcel in) {
        name = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DemoData> CREATOR = new Creator<DemoData>() {
        @Override
        public DemoData createFromParcel(Parcel in) {
            return new DemoData(in);
        }

        @Override
        public DemoData[] newArray(int size) {
            return new DemoData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DemoData{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

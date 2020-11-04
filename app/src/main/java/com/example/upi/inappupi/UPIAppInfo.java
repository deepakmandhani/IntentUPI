package com.example.upi.inappupi;

import android.os.Parcel;
import android.os.Parcelable;

public class UPIAppInfo implements Parcelable{

    public String appName;
    public String packageName;
    public int orderNo;

    public UPIAppInfo() {
    }

    protected UPIAppInfo(Parcel in) {
        appName = in.readString();
        packageName = in.readString();
        orderNo = in.readInt();
    }

    public static final Creator<UPIAppInfo> CREATOR = new Creator<UPIAppInfo>() {
        @Override
        public UPIAppInfo createFromParcel(Parcel in) {
            return new UPIAppInfo(in);
        }

        @Override
        public UPIAppInfo[] newArray(int size) {
            return new UPIAppInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(packageName);
        dest.writeInt(orderNo);
    }
}

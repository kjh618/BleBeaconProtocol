package kr.hs.gshs.blebeaconprotocol;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjh on 2017-10-05.
 */

public class DataItem implements Parcelable{
    int length;
    String type, data;

    public DataItem(String type, String data) {
        this.type = type;
        this.data = data;
        length = data.length() + 1;
    }

    public DataItem(Parcel src) {
        length = src.readInt();
        type = src.readString();
        data = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(length);
        dest.writeString(type);
        dest.writeString(data);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        length = data.length() + 1;
    }
}

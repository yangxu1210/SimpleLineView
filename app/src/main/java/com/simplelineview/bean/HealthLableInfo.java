package com.simplelineview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by xjj on 2017/5/22.
 */

public class HealthLableInfo implements Parcelable {
    public ArrayList<Obj> bodys;

    public static class Obj implements Parcelable {
        public String title; //餐前血糖（0.0mmol/L）
        public String unit;//单位
        public String word; //唯一关键值，上传数据用
        public double default_num;//默认值
        public double max;//最大值
        public double min;//最小值
        public int digits;//保留几位小数 ，0，1，2，3
        public int type ; //0=圆，1=尺，2=手输

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.unit);
            dest.writeString(this.word);
            dest.writeDouble(this.default_num);
            dest.writeDouble(this.max);
            dest.writeDouble(this.min);
            dest.writeInt(this.digits);
            dest.writeInt(this.type);
        }

        public Obj() {
        }

        protected Obj(Parcel in) {
            this.title = in.readString();
            this.unit = in.readString();
            this.word = in.readString();
            this.default_num = in.readDouble();
            this.max = in.readDouble();
            this.min = in.readDouble();
            this.digits = in.readInt();
            this.type = in.readInt();
        }

        public static final Creator<Obj> CREATOR = new Creator<Obj>() {
            @Override
            public Obj createFromParcel(Parcel source) {
                return new Obj(source);
            }

            @Override
            public Obj[] newArray(int size) {
                return new Obj[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.bodys);
    }

    public HealthLableInfo() {
    }

    protected HealthLableInfo(Parcel in) {
        this.bodys = new ArrayList<Obj>();
        in.readList(this.bodys, Obj.class.getClassLoader());
    }

    public static final Creator<HealthLableInfo> CREATOR = new Creator<HealthLableInfo>() {
        @Override
        public HealthLableInfo createFromParcel(Parcel source) {
            return new HealthLableInfo(source);
        }

        @Override
        public HealthLableInfo[] newArray(int size) {
            return new HealthLableInfo[size];
        }
    };
}

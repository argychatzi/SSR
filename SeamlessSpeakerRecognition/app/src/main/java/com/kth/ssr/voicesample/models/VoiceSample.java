package com.kth.ssr.voicesample.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by argychatzi on 3/23/14.
 */
public class VoiceSample implements Parcelable {

    private File mFile;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mFile);
    }

    public VoiceSample() {
    }

    public VoiceSample(File f){
        mFile = f;
    }

    private VoiceSample(Parcel in) {
        this.mFile = (File) in.readSerializable();
    }

    public static Creator<VoiceSample> CREATOR = new Creator<VoiceSample>() {
        public VoiceSample createFromParcel(Parcel source) {
            return new VoiceSample(source);
        }

        public VoiceSample[] newArray(int size) {
            return new VoiceSample[size];
        }
    };

    public File getFile() {
        return mFile;
    }

    public String getFileName() {
        return mFile.getName();
    }
}

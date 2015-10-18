package swipe.android.berkeleyfacial.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {


    private String imagePath;

    private String id;
    public boolean isrecommend;

    public Data(String imagePath, String id, boolean isrecommend) {
        this.imagePath = imagePath;
        this.id = id;
        this.isrecommend = isrecommend;
    }
public boolean isIsrecommend(){return isrecommend;};
    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {



        dest.writeString(imagePath);
        dest.writeString(id);
        dest.writeString(String.valueOf(isrecommend));
    }

    // Creator
    public static final Parcelable.Creator<Data> CREATOR
            = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    // "De-parcel object
    public Data(Parcel in) {
        imagePath = in.readString();
        id = in.readString();
        isrecommend = Boolean.valueOf(in.readString());
    }
}
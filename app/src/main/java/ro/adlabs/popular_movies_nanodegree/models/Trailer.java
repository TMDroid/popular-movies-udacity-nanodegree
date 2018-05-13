package ro.adlabs.popular_movies_nanodegree.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {
    private String id;
    private String name;
    private String key;

    public Trailer(String id, String name,  String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public Trailer setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Trailer setName(String name) {
        this.name = name;
        return this;
    }
    public String getKey() {
        return key;
    }

    public Trailer setKey(String key) {
        this.key = key;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
    }

    protected Trailer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.key = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}

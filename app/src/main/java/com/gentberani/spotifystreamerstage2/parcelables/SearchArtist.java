package com.gentberani.spotifystreamerstage2.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gentberani on 6/14/15.
 */

public class SearchArtist implements Parcelable {

    public String artistName = "", albumName = "", url = "", previewURL = "", realArtistName = "";

    public SearchArtist(String name, String albumName, String url, String realArtistName, String previewURL) {
        this.artistName = name;
        this.albumName = albumName;
        this.url = url;
        this.realArtistName = realArtistName;
        this.previewURL = previewURL;
    }

    private SearchArtist(Parcel in) {
        artistName = in.readString();
        albumName = in.readString();
        url = in.readString();
        realArtistName = in.readString();
        previewURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(albumName);
        dest.writeString(url);
        dest.writeString(previewURL);
        dest.writeString(realArtistName);
    }

    public static final Creator<SearchArtist> CREATOR = new Creator<SearchArtist>() {
        public SearchArtist createFromParcel(Parcel in) {
            return new SearchArtist(in);
        }

        public SearchArtist[] newArray(int size) {
            return new SearchArtist[size];
        }
    };


}

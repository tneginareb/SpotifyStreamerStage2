package com.gentberani.spotifystreamerstage2.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gentberani on 6/14/15.
 */

public class TopTracks implements Parcelable {

    public String songName = "", albumName = "", url = "", previewURL = "", realArtistName = "";

    public TopTracks(String name, String albumName, String url, String previewURL, String realArtistName) {
        this.songName = name;
        this.albumName = albumName;
        this.url = url;
        this.previewURL = previewURL;
        this.realArtistName = realArtistName;
    }

    private TopTracks(Parcel in) {
        songName = in.readString();
        albumName = in.readString();
        url = in.readString();
        previewURL = in.readString();
        realArtistName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songName);
        dest.writeString(albumName);
        dest.writeString(url);
        dest.writeString(previewURL);
        dest.writeString(realArtistName);
    }

    public static final Creator<TopTracks> CREATOR = new Creator<TopTracks>() {
        public TopTracks createFromParcel(Parcel in) {
            return new TopTracks(in);
        }

        public TopTracks[] newArray(int size) {
            return new TopTracks[size];
        }
    };


}

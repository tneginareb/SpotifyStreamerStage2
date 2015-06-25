package com.gentberani.spotifystreamerstage2.callbacks;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by gentberani on 6/7/15.
 */
public interface TopTenCallback {
    void onTopTenResults(boolean success, List<Track> artistArrayList);
}

package com.gentberani.spotifystreamerstage2.callbacks;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by gentberani on 6/7/15.
 */
public interface SearchCallback {
    void onSearchResult(boolean success, List<Artist> artistArrayList);
}

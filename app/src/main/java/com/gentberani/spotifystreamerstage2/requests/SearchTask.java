package com.gentberani.spotifystreamerstage2.requests;

import android.os.AsyncTask;

import com.gentberani.spotifystreamerstage2.callbacks.SearchCallback;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by gentberani on 6/7/15.
 */

public class SearchTask extends AsyncTask<Void, Void, ArtistsPager> {
    public SearchTask() {
    }

    SearchCallback callback;
    String pattern = "";

    public SearchTask(String pattern, SearchCallback callback) {
        this.callback = callback;
        this.pattern = pattern;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ArtistsPager doInBackground(Void... params) {
        try {
            ArtistsPager results;
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            results = spotify.searchArtists(pattern);
            return results;
        } catch (Exception e) {
            return null;
        }


    }

    @Override
    protected void onPostExecute(ArtistsPager results) {
        try {
            if (results.artists.items != null) {
                callback.onSearchResult(true, results.artists.items);
            } else {
                callback.onSearchResult(false, null);
            }
        } catch (Exception e) {
        }
    }
}

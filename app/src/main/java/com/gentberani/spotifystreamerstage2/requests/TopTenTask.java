package com.gentberani.spotifystreamerstage2.requests;

import android.os.AsyncTask;

import com.gentberani.spotifystreamerstage2.callbacks.TopTenCallback;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by gentberani on 6/7/15.
 */

public class TopTenTask extends AsyncTask<Void, Void, Tracks> {
    TopTenCallback callback;
    String id = "";

    public TopTenTask(String id, TopTenCallback callback) {
        this.callback = callback;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Tracks doInBackground(Void... params) {
        try {
            Tracks results;
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            results = spotify.getArtistTopTrack(id);
            return results;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Tracks result) {
        try {
            if (result.tracks != null) {
                callback.onTopTenResults(true, result.tracks);
            } else {
                callback.onTopTenResults(false, result.tracks);
            }
        } catch (Exception e) {
        }

    }
}

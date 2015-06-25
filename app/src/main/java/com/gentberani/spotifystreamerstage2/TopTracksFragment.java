package com.gentberani.spotifystreamerstage2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gentberani.spotifystreamerstage2.callbacks.ItemClickedCallback;
import com.gentberani.spotifystreamerstage2.callbacks.TopTenCallback;
import com.gentberani.spotifystreamerstage2.listadapters.TopTracksAdapter;
import com.gentberani.spotifystreamerstage2.parcelables.TopTracks;
import com.gentberani.spotifystreamerstage2.requests.TopTenTask;
import com.gentberani.spotifystreamerstage2.staticclasses.API;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;


/**
 * Created by gentberani on 6/7/15.
 */
public class TopTracksFragment extends Fragment {


    private String artistName = "";
    private String artistID = "";
    @InjectView(R.id.rcv_toptenList)
    RecyclerView rcv_toptenList;
    private TopTracksAdapter toptenAdapter;
    private ArrayList<TopTracks> arrayTopTen = new ArrayList<>();

    private View gv;
    @InjectView(R.id.progressBarTop)
    ProgressBar progressBarTop;

    public static ItemClickedCallback positionCallback;

    public static TopTracksFragment staticActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        gv = inflater.inflate(R.layout.toptracks_fragment, container, false);
        ButterKnife.inject(this, gv);
        //initializeActionBar();

        staticActivity = this;
        artistID = getArguments().getString("artist_id");


        rcv_toptenList.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_toptenList.setLayoutManager(linearLayoutManager);
        toptenAdapter = new TopTracksAdapter(getActivity());
        rcv_toptenList.setAdapter(toptenAdapter);

        positionCallback = new ItemClickedCallback() {
            @Override
            public void onItemClicked(String id, String artistName) {
                getTopTen(id, savedInstanceState);
            }
        };

        getTopTen(artistID, savedInstanceState);
        return gv;
    }

    private void getTopTen(String artistID, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelableArrayList("topTrackList") != null) {
                arrayTopTen = savedInstanceState.getParcelableArrayList("topTrackList");
            }
            toptenAdapter.notifyAdapter(arrayTopTen);
        } else {
            arrayTopTen = new ArrayList<>();
            TopTenTask task = new TopTenTask(artistID, new TopTenCallback() {
                @Override
                public void onTopTenResults(boolean success, List<Track> artistArrayList) {
                    progressBarTop.setVisibility(View.GONE);
                    if (success) {
                        for (int k = 0; k < artistArrayList.size(); k++) {
                            if (artistArrayList.get(k).album.images.size() > 0) {
                                arrayTopTen.add(new TopTracks(artistArrayList.get(k).name, artistArrayList.get(k).album.name, artistArrayList.get(k).album.images.get(0).url, artistArrayList.get(k).preview_url, artistArrayList.get(k).artists.get(0).name));
                            }
                        }
                        toptenAdapter.notifyAdapter(arrayTopTen);
                    } else {
                        Toast.makeText(getActivity(), "An error has occurred. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (API.isNetworkAvailable(getActivity())) {
                progressBarTop.setVisibility(View.VISIBLE);
                if (artistID.length() > 0)
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("topTrackList", arrayTopTen);
        super.onSaveInstanceState(outState);
    }

    private void intializeActionbar() {
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Top 10 Tracks");
        actionBar.setSubtitle(artistName);
    }

    private void hideCurrentKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            getActivity().finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

}

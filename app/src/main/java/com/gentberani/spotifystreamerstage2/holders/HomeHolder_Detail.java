package com.gentberani.spotifystreamerstage2.holders;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.gentberani.spotifystreamerstage2.R;
import com.gentberani.spotifystreamerstage2.TopTracksFragment;

/**
 * Created by gentberani on 6/21/15.
 */
public class HomeHolder_Detail extends ActionBarActivity {
    private String artistName = "";
    private String artistID = "";

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_holder_detail);

        artistName = getIntent().getStringExtra("artist_name");
        artistID = getIntent().getStringExtra("artist_id");
        intializeActionbar(artistName);
        createFragment();


    }

    private void createFragment() {
        TopTracksFragment newFragment = new TopTracksFragment();
        Bundle args = new Bundle();
        args.putString("artist_id", artistID);
        args.putString("artist_name", artistName);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.rl_container, newFragment);
        transaction.commit();
    }

    private void intializeActionbar(String artist) {
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Spotify Streamer");
        actionBar.setSubtitle(artist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}




package com.gentberani.spotifystreamerstage2.holders;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.gentberani.spotifystreamerstage2.R;
import com.gentberani.spotifystreamerstage2.TopTracksFragment;

/**
 * Created by gentberani on 6/21/15.
 */
public class HomeHolder extends ActionBarActivity {

    public static boolean twoPane = false;
    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_holder);

        intializeActionbar();
        if (isTwoPane()) {
            twoPane = true;
            createFragment(savedInstanceState);
        } else {
            twoPane = false;
        }


    }

    public boolean isTwoPane() {
        if (findViewById(R.id.searchFragment) != null) {
            return true;
        } else {
            return false;
        }
    }

    private void createFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            TopTracksFragment newFragment = new TopTracksFragment();
            Bundle args = new Bundle();
            args.putString("artist_id", "");
            args.putString("artist_name", "");
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.rl_container, newFragment);
            transaction.commit();
        }
    }

    private void intializeActionbar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle("Spotify Streamer");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



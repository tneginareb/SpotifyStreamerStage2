package com.gentberani.spotifystreamerstage2.listadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentberani.spotifystreamerstage2.R;
import com.gentberani.spotifystreamerstage2.SearchFragment;
import com.gentberani.spotifystreamerstage2.TopTracksFragment;
import com.gentberani.spotifystreamerstage2.holders.HomeHolder;
import com.gentberani.spotifystreamerstage2.holders.HomeHolder_Detail;
import com.gentberani.spotifystreamerstage2.staticclasses.API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by gentberani on 6/10/15.
 */


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context ctx;
    private ArrayList<Artist> arrayResults = new ArrayList<>();
    private Toast toast;


    public SearchAdapter(Context ctx, List<Artist> arrayProds) {
        this.ctx = ctx;
        this.arrayResults.addAll(arrayProds);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_search, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return arrayResults.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(arrayResults.get(position).name);
        if (arrayResults.get(position).images.size() > 0) {
            Picasso.with(ctx).load(arrayResults.get(position).images.get(0).url).into(viewHolder.img_artist);
        }
    }

    private void showToast(String arg) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(ctx, "" + arg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean checkInternet() {
        if (API.isNetworkAvailable(ctx)) {
            return true;
        } else {
            showToast("No internet connection, please try again!");
            return false;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.txt_artistName)
        TextView txt_artistName;
        @InjectView(R.id.img_artist)
        ImageView img_artist;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.inject(this, v);
        }

        public TextView getTextView() {
            return txt_artistName;
        }

        @Override
        public void onClick(View v) {

            if (checkInternet())
                beginFragment();
            SearchFragment.hideCurrentKeyboard();
        }

        private void beginFragment() {
            if (HomeHolder.twoPane) {

                if (TopTracksFragment.positionCallback != null) {
                    TopTracksFragment.positionCallback.onItemClicked(arrayResults.get(getPosition()).id, arrayResults.get(getPosition()).name);
                    if (HomeHolder.actionBar != null) {
                        HomeHolder.actionBar.setSubtitle(arrayResults.get(getPosition()).name);
                    }

                } else {
                    ifNull();
                }
            } else {
                Intent i = new Intent(ctx, HomeHolder_Detail.class);
                i.putExtra("artist_id", arrayResults.get(getPosition()).id);
                i.putExtra("artist_name", arrayResults.get(getPosition()).name);
                ctx.startActivity(i);
            }


        }

        private void ifNull() {

            TopTracksFragment newFragment = new TopTracksFragment();
            Bundle args = new Bundle();
            args.putString("artist_id", arrayResults.get(getPosition()).id);
            args.putString("artist_name", arrayResults.get(getPosition()).name);
            newFragment.setArguments(args);

            FragmentTransaction transaction = ((ActionBarActivity) ctx).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.rl_container, newFragment);
            transaction.commit();
        }
    }

}

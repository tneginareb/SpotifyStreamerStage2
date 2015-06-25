package com.gentberani.spotifystreamerstage2.listadapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gentberani.spotifystreamerstage2.dialogs.MusicDialogActivity;
import com.gentberani.spotifystreamerstage2.dialogs.MusicDialogFragment;
import com.gentberani.spotifystreamerstage2.R;
import com.gentberani.spotifystreamerstage2.TopTracksFragment;
import com.gentberani.spotifystreamerstage2.holders.HomeHolder;
import com.gentberani.spotifystreamerstage2.parcelables.TopTracks;
import com.gentberani.spotifystreamerstage2.staticclasses.API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by gentberani on 6/10/15.
 */

public class TopTracksAdapter extends RecyclerView.Adapter<TopTracksAdapter.ViewHolder> {

    ArrayList<TopTracks> arrayResults = new ArrayList<>();
    Context ctx;

    public TopTracksAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void notifyAdapter(ArrayList<TopTracks> trackArrayList) {
        this.arrayResults = trackArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.txt_artistNametopTen)
        TextView txt_artistNametopTen;
        @InjectView(R.id.txt_albumName)
        TextView txt_albumName;
        @InjectView(R.id.img_artisttopTen)
        ImageView img_artisttopTen;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.inject(this, v);
        }

        public TextView getArtistName() {
            return txt_artistNametopTen;
        }

        public TextView getAlbumName() {
            return txt_albumName;
        }

        @Override
        public void onClick(View v) {

            if (checkInternet()) {
                if (imTablet())
                    createDialogInterface(getPosition(), arrayResults);
                else
                    createIntentInterface(getPosition(), arrayResults);

            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_topten, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return arrayResults.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getArtistName().setText(arrayResults.get(position).songName);
        viewHolder.getAlbumName().setText(arrayResults.get(position).albumName);
        Picasso.with(ctx).load(arrayResults.get(position).url).into(viewHolder.img_artisttopTen);
    }

    private void createDialogInterface(int position, ArrayList<TopTracks> arrayResults) {
        MusicDialogFragment newFragment = new MusicDialogFragment().newInstance(position, arrayResults);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        newFragment.show(TopTracksFragment.staticActivity.getFragmentManager(), null);
    }

    private void createIntentInterface(int position, ArrayList<TopTracks> arrayResults) {

        Intent dialogIntent = new Intent(ctx, MusicDialogActivity.class);
        dialogIntent.putExtra("position", position);
        dialogIntent.putParcelableArrayListExtra("arrayList", arrayResults);
        ctx.startActivity(dialogIntent);

    }

    private boolean imTablet() {
        if (HomeHolder.twoPane)
            return true;
        else
            return false;
    }

    private Toast toast;

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
}
package com.gentberani.spotifystreamerstage2.dialogs;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gentberani.spotifystreamerstage2.R;
import com.gentberani.spotifystreamerstage2.parcelables.TopTracks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gentberani on 6/22/15.
 */
public class MusicDialogFragment extends DialogFragment implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {


    public MediaPlayer mediaPlayer;
    private ArrayList<TopTracks> arrayResults = new ArrayList<>();
    private int position = 0;
    int musicFileInMili = 0;
    Handler handler;

    View gv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gv = inflater.inflate(R.layout.music_dialog, container, false);
        ButterKnife.inject(this, gv);
        getFromBundle();
        handler = new Handler();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        initializePlayer(position, false);


        sb_seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaPlayer.isPlaying()) {
                    int touchProg = sb_seekBar.getProgress() * (musicFileInMili / 100);
                    mediaPlayer.seekTo(touchProg);
                }
                return false;
            }
        });

        return gv;
    }

    @OnClick(R.id.imb_Next)
    public void nextClick() {

        position++;
        if (position == arrayResults.size()) {
            position = 0;
        }
        initializePlayer(position, false);

    }

    @OnClick(R.id.imb_Previus)
    public void clickPrevius() {
        position--;
        if (position < 0) {
            position = arrayResults.size() - 1;
        }
        initializePlayer(position, false);
    }

    @OnClick(R.id.imb_Play)
    public void clickPlay() {
        initializePlayer(position, true);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        sb_seekBar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextClick();
    }

    private void getFromBundle() {
        position = getArguments().getInt("position", 0);
        arrayResults = getArguments().getParcelableArrayList("arrayList");
    }

    public static MusicDialogFragment newInstance(int position, ArrayList<TopTracks> arrayResults) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("arrayList", arrayResults);
        args.putInt("position", position);
        MusicDialogFragment fragment = new MusicDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @InjectView(R.id.imb_Previus)
    ImageButton imb_Previus;
    @InjectView(R.id.imb_Play)
    ImageButton imb_play;
    @InjectView(R.id.imb_Next)
    ImageButton imb_Next;
    @InjectView(R.id.img_thumbnailArt)
    ImageView img_thumbnailArt;

    @InjectView(R.id.txt_musicArtist)
    TextView txt_musiArtist;
    @InjectView(R.id.txt_musicAlbum)
    TextView txt_musicAlbum;
    @InjectView(R.id.txt_musicSongName)
    TextView txt_musicSongName;

    @InjectView(R.id.txt_START)
    TextView txt_START;
    @InjectView(R.id.txt_END)
    TextView txt_END;
    @InjectView(R.id.sb_seekBar)
    SeekBar sb_seekBar;

    public void initializePlayer(int _position, boolean imPlaying) {

        if (!imPlaying) {

            imb_play.setImageResource(android.R.drawable.ic_media_pause);
            txt_musiArtist.setText(arrayResults.get(_position).realArtistName);
            txt_musicAlbum.setText(arrayResults.get(_position).albumName);
            txt_musicSongName.setText(arrayResults.get(_position).songName);
            Picasso.with(getActivity()).load(arrayResults.get(_position).url).resize(400, 400).centerCrop().into(img_thumbnailArt);
            getData(_position);

        } else {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                imb_play.setImageResource(android.R.drawable.ic_media_play);
                bargProgress();
            } else {

                mediaPlayer.start();
                imb_play.setImageResource(android.R.drawable.ic_media_pause);
                bargProgress();
            }
        }


    }


    private void bargProgress() {

        if (mediaPlayerExist()) {
            sb_seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / musicFileInMili) * 100));
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayerExist()) {
                            bargProgress();
                            txt_START.setText(getStringFormat(mediaPlayer.getCurrentPosition()));
                        }
                    }
                }, 100);
            }
        }
    }

    private String getStringFormat(int milisecond) {
        return String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(milisecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milisecond)),
                TimeUnit.MILLISECONDS.toSeconds(milisecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milisecond)));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mediaPlayerExist()) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
            }
        }
    }

    private boolean mediaPlayerExist() {
        if (mediaPlayer != null)
            return true;
        else
            return false;
    }

    private Toast toast;

    private void showToast(String arg) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(getActivity(), "" + arg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getData(final int _pos) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(arrayResults.get(_pos).previewURL);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    musicFileInMili = mediaPlayer.getDuration();
                    bargProgress();
                } catch (Exception e) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                txt_END.setText(getStringFormat(musicFileInMili));
                txt_START.setText("0:00");
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

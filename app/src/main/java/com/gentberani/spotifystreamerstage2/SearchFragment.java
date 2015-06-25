package com.gentberani.spotifystreamerstage2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.gentberani.spotifystreamerstage2.callbacks.SearchCallback;
import com.gentberani.spotifystreamerstage2.listadapters.SearchAdapter;
import com.gentberani.spotifystreamerstage2.requests.SearchTask;
import com.gentberani.spotifystreamerstage2.staticclasses.API;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;


public class SearchFragment extends Fragment {

    @InjectView(R.id.rcv_searchList)
    RecyclerView rcv_searchList;
    @InjectView(R.id.sv_searchText)
    SearchView sv_searchText;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    private SearchAdapter searchAdapter;
    private ArrayList<Artist> arrayResults = new ArrayList<>();

    SearchTask task = new SearchTask();
    Toast toast;

    public static SearchFragment staticActivity;

    private View gv;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gv = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.inject(this, gv);
        staticActivity = this;
        sv_searchText.setIconified(false);




        int searchPlateId = sv_searchText.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = sv_searchText.findViewById(searchPlateId);
        if (searchPlateView != null) {
            searchPlateView.setBackgroundColor(getResources().getColor(R.color.cellColor));
        }
        sv_searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String pattern) {
                hideCurrentKeyboard();
                if (pattern.toString().equalsIgnoreCase(" ")) {
                    sv_searchText.setQuery("", false);
                }
                getResults(pattern, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String pattern) {

                if (pattern.equalsIgnoreCase(" ")) {
                    sv_searchText.setQuery("", false);
                }
                getResults(pattern, false);
                return false;
            }
        });


        rcv_searchList.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_searchList.setLayoutManager(linearLayoutManager);


        searchAdapter = new SearchAdapter(getActivity(), arrayResults);
        rcv_searchList.setAdapter(searchAdapter);


        return gv;
    }

    private void getResults(final String searchPattern, final boolean imKeyDown) {

        if (task != null) {
            task.cancel(true);
        }


        task = new SearchTask(searchPattern, new SearchCallback() {
            @Override
            public void onSearchResult(boolean success, List<Artist> artistArrayList) {
                progressBar.setVisibility(View.GONE);
                if (success) {
                    searchAdapter = new SearchAdapter(getActivity(), artistArrayList);
                    rcv_searchList.setAdapter(searchAdapter);
                    if (imKeyDown && artistArrayList.size() == 0) {
                        cancelToast();
                        toast = Toast.makeText(getActivity(), "Sorry, Artist with name : " + searchPattern + " has not found. Please try again!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    cancelToast();
                    toast = Toast.makeText(getActivity(), "An error has occurred. Please try again!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        if (API.isNetworkAvailable(getActivity())) {
            if (!searchPattern.equalsIgnoreCase("") && searchPattern.length() > 0) {
                progressBar.setVisibility(View.VISIBLE);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            cancelToast();
            toast = Toast.makeText(getActivity(), "No internet connection, please try again!", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    public static void hideCurrentKeyboard() {
        try {
            View view = staticActivity.getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) staticActivity.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }


    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }


}

package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

//load a list of news using AsyncTask to perform the network request to the given url
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    //tag for log messages
    // private static final String LOG_TAG = NewsLoader.class.getName();

    //query url
    private String qUrl;

    /*
     * constructs a new NewsLoader
     * @param context - for the activity
     * @param url - to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        qUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (qUrl == null) {
            return null;
        }

        //perform the network request, parse the response, extract a list of news
        List<News> news = Utils.fetchNewsData(qUrl);
        return news;
    }
}

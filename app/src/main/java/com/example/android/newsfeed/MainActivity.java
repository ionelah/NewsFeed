package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    //constant value for the news loader (can be any value)
    private static final int NEWS_LOADER_ID = 1;

    //adapter for the list of news
    private NewsAdapter nAdapter;

    //tag for the log messages
    private static final String LOG_TAG = MainActivity.class.getName();

    //Url the query the guardian for books information
    private static final String guardian_request_url = "https://content.guardianapis.com/search?q=debates&section=books&show-tags=contributor&api-key=test";

    // TextView that is displayed when the list is empty
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find a reference for the ListView in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        //create a new adapter that takes an empty list of news as an input
        nAdapter = new NewsAdapter(this, new ArrayList<News>());

        //set the adapter on the ListView so the list can be populated in the interface
        newsListView.setAdapter(nAdapter);

        /*
         * set an item click listener on the ListView which sends an intent to the web browser
         * to open a new website with more information about that news
         */
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //find the current news what has been clicked
                News currentNews = nAdapter.getItem(position);

                //convert the String Url to URI object to pass the intent constructor
                Uri newsUri = Uri.parse(currentNews.getUrl());

                //create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                //send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        emptyTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //get details on the currently active default data network
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        //if there is a network conection fetch the data
        if (networkInfo != null && networkInfo.isConnected()) {
            //get a reference to the LoaderManager to interact with the Loader
            LoaderManager loaderManager = getLoaderManager();

            //initialize the loader
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            emptyTextView.setText(R.string.no_internet_conection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //create a new Loader for the given URL
        return new NewsLoader(this, guardian_request_url);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        //set empty TextView to display - No books (data) found
        emptyTextView.setText(R.string.no_data_to_display);

        //clear the adapter of previous news
        nAdapter.clear();

        //if there is a valid list of news then add them to the adapter data set
        if (data != null && !data.isEmpty()) {
            nAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //loader reset
        nAdapter.clear();
    }
}

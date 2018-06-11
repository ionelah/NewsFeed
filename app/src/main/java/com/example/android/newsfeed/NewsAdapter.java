package com.example.android.newsfeed;

/* NewsAdapter knows how to create a list item layout for each news
 * in the data source a list of news object
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    //constructs a new NewsAdapter
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /* returns a list of items that display the information about the news
     * at a given position on a list of news
     */
    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {

        //check if the existing View is being reused, otherwise inflate the View
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //the the news object located at the position in the list
        News currentNews = getItem(position);


        //find the TextView for the name of section for that article
        TextView sectionNameTextView = (TextView) listItemView.findViewById(R.id.name_of_section_text_view);
        //display the current section name for the article
        sectionNameTextView.setText(currentNews.getSectionName());

        //find TextView for the author name and display it for current news
        TextView authorNameTextView = (TextView) listItemView.findViewById(R.id.author_name_text_view);
        authorNameTextView.setText(currentNews.getAuthorName());

        //find TextView for the title of article
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_of_article_text_view);
        //display the current title for the article
        titleTextView.setText(currentNews.getTitle());

        //separate the data published - in two Strings for data(finalData) and time(finalTime)
        String finalData = "";
        String finalTime = "";
        //get the data from the json response for the current news
        String webData = currentNews.getData();
        //check if contains the separator "T" - the place were we split the String data in 2 strings - data and time
        if (webData.contains("T")) {
            String[] parts = webData.split("T");
            finalData = parts[0];
            finalTime = parts[1];
        }

        //find TextView for the data info
        TextView dataTextView = (TextView) listItemView.findViewById(R.id.data_info_text_view);
        //display the current data info for the article
        dataTextView.setText(finalData);

        //find TextView for the time info
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_info_text_view);
        //display the current time info for the article
        timeTextView.setText(finalTime);

        return listItemView;
    }
}

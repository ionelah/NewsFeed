package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

//this are methods used to help us get the information from internet - the guardian
public final class Utils {

    //tag for the log messages
    private static final String LOG_TAG = Utils.class.getName();

    //create a private constructor because no one should ever create a Utils object
    private Utils() {
    }

    //query the guardian dataset and return a list of news object
    public static List<News> fetchNewsData(String requestUrl) {

        //create an url object
        URL url = createUrl(requestUrl);

        //perform HTTP request to the url and receive the JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        //extract relevant fields from the JSON response and create a news object
        List<News> news = extractFeatureFromJsonResponse(jsonResponse);

        //return the list of news
        return news;
    }

    //return new url object from the given string url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url", e);
        }
        return url;
    }

    //make HTTP request to the given url and return a String as a response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if the url is null than return early
        if (url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /*
             * if the request was succesful (response code = 200)
             * then read the input stream and parse the response
             */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Json Response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /*
     * convert the InputStream into a String which contains the whole
     * Json response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /*
     * returns a list of object by parsing out information about the first
     * news from the input newsJson String
     */
    private static List<News> extractFeatureFromJsonResponse(String newsJson) {

        //if the Json response is null then return early
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        //create an array list that we can start adding news
        List<News> news = new ArrayList<>();

        /*
         * Try to parse the Json response string. If there is a problem with the way the Json
         * response is formatted that the JSONException will be thrown
         * We will catch the exception that the app doesn't crash and print the error message
         */
        try {
            //create a JSONObject from the Json response string
            JSONObject baseJsonResponse = new JSONObject(newsJson);

            //extract the Json object associated with the key "response"
            JSONObject jsonResponse = baseJsonResponse.getJSONObject("response");

            //extract the Json array associated with the key "results"
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            //for each news in the newsArray create a news object
            for (int i = 0; i < resultsArray.length(); i++) {

                //get a single news at position i within the list of news
                JSONObject currentNews = resultsArray.getJSONObject(i);

                //extract the value for the key word webTitle  - for title of article
                String titleOfArticle = currentNews.getString("webTitle");

                //extract the value for the key word sectionName - for section name
                String sectionName = currentNews.getString("sectionName");

                //extract the value for the key word webPublicationDate - for date of publish
                String datePublished = currentNews.getString("webPublicationDate");

                //extract the value for the key word webUrl
                String webUrl = currentNews.getString("webUrl");

                //extract the author name which is under tags array, key word webTitle
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                String authorName = "";

                if (tagsArray.length() > 0) {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        JSONObject newObject = tagsArray.getJSONObject(j);
                        authorName = newObject.getString("webTitle");
                    }
                }

                //create a new news object with the title, section name, date and url from Json response
                News newNews = new News(titleOfArticle, sectionName, datePublished, webUrl, authorName);
                news.add(newNews);
            }
        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the Json response", e);
        }
        return news;
    }
}

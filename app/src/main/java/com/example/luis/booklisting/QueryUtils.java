package com.example.luis.booklisting;

/**
 * Created by Luis on 9/10/2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper methods related to requuesting and receiving earthquake data from google books
 */
public final class QueryUtils {
    /** Tag for the log messages*/
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils}object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not neede).
     */
    private QueryUtils(){
    }

    /**
     * Query the google books data set and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(String requestUrl){
        // Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
        // Extract relevant fields from the JSON response and creat a list of {@links Books}
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Books}
        return books;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
            return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null){
            return  jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds */);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read  the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the book JSON resuls.", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                // Closing the input stream could throw an IOExcepltion, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link Book} object that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON){
        // initialize author variable.
        String author;
        // IF the JSON string is empty or null, then return early.
        if(TextUtils.isEmpty(bookJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();
        //Try to parse the JSON response string . If there's a problem with the way the JSON
        // is formatted, a JSONException  exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Create a JSONObject from the JSON response string
            JSONObject rootJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items"
            // which represents a list of objects (or books)
            JSONArray bookArray = rootJsonResponse.getJSONArray("items");

            // for each book in the bookArray, create an {@link Book} object
            for (int i = 0; i < bookArray.length(); i++){
                //Get a single book at position i  with in the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);
                // for a given book, extract the JSONObject associate with the
                // key call "volumeInfo", which represent a list of all the info
                // about the book
                JSONObject currentInfo = currentBook.getJSONObject("volumeInfo");

                //check if the JSONObject currentInfo has the key called "authors"
                if(currentInfo.has("authors")) {
                    JSONArray authors = currentInfo.getJSONArray("authors");
                    // Extract the first value in the jsonArray "authors"
                    author = authors.getString(0);
                } else {
                     author = "Anonymous";
                }
                // Extract the value for the key "title"
                String title = currentInfo.getString("title");

                // Create a new {@link Book} object with the author and title
                Book book = new Book(author, title);

                books.add(book);

            }
        }catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the book JSON resuls", e);
        }
        return books;

    }

}

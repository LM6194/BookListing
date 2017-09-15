package com.example.luis.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 9/13/2017.
 */

public class SearchBookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = SearchBookActivity.class.getName();
    /** URL for books data from the google books API*/


    String requestUrl = MainActivity.booksRequestUrl;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes to play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**Adapter for the list of books */
    private BookAdapter mAdapter;

    /** TextView that is displayed when the list is empty*/
    private TextView mEmptyStateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that take an  empty list of books an input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        mEmptyStateView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateView);

        // Bet a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            //Get reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass is this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state eith no connection error message
            mEmptyStateView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args){
        //Create a new loader for the given url
        return new BookLoader(this, requestUrl);
    }
    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquake found."
        mEmptyStateView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        //If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // date set. This will trigger the ListView to update.
        if(books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();

    }

}

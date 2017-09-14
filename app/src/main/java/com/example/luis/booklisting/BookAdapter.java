package com.example.luis.booklisting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luis on 9/10/2017.
 */

public class BookAdapter extends ArrayAdapter<Book>{

    public static final String LOG_TAG = MainActivity.class.getName();

    public BookAdapter(Context context, List<Book> bookList){

        super(context, 0, bookList);
    }

    /**
     * Returns a list item view that displays information about the book at
     * a given position in the list of book
     */
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        // check if there is an existing list view (called convertView) that we can reuse
        // otherwise, if convertView is null, then inflate a new list item layout
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }
        // find book at a given position in the book list
        Book currentBook = getItem(position);

        // find TextView for id title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        // find the title from the current book
        String title = currentBook.getTitle();
        // display the title in the current book title TextView
        titleTextView.setText(title);

        // find TestView for id author
        TextView authorTextView = (TextView)listItemView.findViewById(R.id.book_author);
        //find the author for the current book
        String author = currentBook.getAutor();
        // display the title in the current book author TextView
        authorTextView.setText(author);
        Log.i(LOG_TAG, "test :"+listItemView);

        // return the list item view that is now showing the appropriate data
        return listItemView;
    }
}

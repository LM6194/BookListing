package com.example.luis.booklisting;

/**
 * Created by Luis on 9/10/2017.
 */

public class Book {

    // name of the author
    private String mAutor;
    // title of the book
    private String mTitle;

    /**
     * Create a new object
     * @param author is the name of the author
     * @param title is the title of the book
     */
    public Book(String author, String title){
        mAutor = author;
        mTitle = title;
    }
    /**
     * @return the name of the author
     */
    public String getAutor() {
        return mAutor;
    }
    /**
     * @return the title of the book
     */
    public String getTitle() {
         return mTitle;
    }
}

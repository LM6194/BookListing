package com.example.luis.booklisting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    /** URL for books data from the google books API*/
    public static String booksRequestUrl;

    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.book_topic);
                value = editText.getText().toString();

                if (!value.equals("")) {
                    booksRequestUrl = "https://www.googleapis.com/books/v1/volumes?q="+value+"&maxResults=40";

                    Intent i = new Intent(MainActivity.this, SearchBookActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), "Type a Topic", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}

package com.example.luis.booklisting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        Log.i(LOG_TAG, " Test: Start Main Activity");
        Log.i(LOG_TAG, " Test: value of value "+ value);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.book_topic);
                value = editText.getText().toString();
                Log.i("MainActivity","value of value: "+value);

                if (!value.equals("")) {
                    booksRequestUrl = "https://www.googleapis.com/books/v1/volumes?q="+value+"&maxResults=40";
                    Log.i(LOG_TAG,"value of booksRequestUrl: "+ booksRequestUrl);

                    Intent i = new Intent(MainActivity.this, SearchBookActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), "Type a Topic", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}

package com.alexandrite.first;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Databases extends AppCompatActivity {

    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databases);

        mDbHelper = new FeedReaderDbHelper(this);

        Button saveFeedButton = (Button) findViewById(R.id.saveFeedButton);

        saveFeedButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText title = (EditText) findViewById(R.id.title);
                        EditText subTitle = (EditText) findViewById(R.id.subTitle);

                        saveFeed(title.getText().toString(), subTitle.getText().toString());
                    }
                }
        );

    }

    public void saveFeed(String title, String subTitle) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
    }

}

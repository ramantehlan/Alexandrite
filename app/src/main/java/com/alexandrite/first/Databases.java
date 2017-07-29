package com.alexandrite.first;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        values.put(ContractFeed.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(ContractFeed.FeedEntry.COLUMN_NAME_SUBTITLE, subTitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ContractFeed.FeedEntry.TABLE_NAME, null, values);
        Toast.makeText(getBaseContext(), Long.toString(newRowId) + " row created ", Toast.LENGTH_SHORT).show();
    }

    public void displayFeed() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specify which column from the database
        // you will actually use after this query
        String[] projection = {
                ContractFeed.FeedEntry._ID,
                ContractFeed.FeedEntry.COLUMN_NAME_TITLE,
                ContractFeed.FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // Filter results WHERE "title" = 'my_title'
        String selection = ContractFeed.FeedEntry.COLUMN_NAME_TITLE + " = ? ";
        String[] selectionArgs = {"My Title"};

        // How you want the results sorted in the result cursor
        String sortOrder = ContractFeed.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                ContractFeed.FeedEntry.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter the row groups
                sortOrder  // the sort order
        );
    }



}

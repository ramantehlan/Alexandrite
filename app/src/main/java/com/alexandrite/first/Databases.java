package com.alexandrite.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Databases extends AppCompatActivity {

    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databases);

        mDbHelper = new FeedReaderDbHelper(this);


    }
}

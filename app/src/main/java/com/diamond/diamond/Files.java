package com.diamond.diamond;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;

public class Files extends AppCompatActivity {

    // This is the tag to be used for creating a Log
    //private static final String FILES_TAG = "tag.files";
    // This is the view to display the list/tree of directories
    private TextView heading;
    private TextView directoryTree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        // Once the view is created, now we can assign the directoryTree
        directoryTree = (TextView) findViewById(R.id.directoryTree);
        heading = (TextView) findViewById(R.id.subTitle);
        directoryTree.setText(displayDirectoryTree(this.getFilesDir().getParent()));
        // Just to print some properties about the main directory
        String property = "";
        property += "[Last Modified: " + Long.toString(this.getFilesDir().lastModified()) + "]";
        property += "[Free Space: " + Long.toString(this.getFilesDir().getFreeSpace()) + "/" + Long.toString(this.getFilesDir().getTotalSpace()) + "]";
        heading.setText(heading.getText().toString() + this.getFilesDir().getParent() + property);

    }


    // This function is called to display the directories
    public String displayDirectoryTree(String path) {
        File file = new File(path);
        String[] fileDirs = file.list();
        String list = "";

        // This is to create the list of directories
        for (String tempDirName : fileDirs) {

            File tempDir = new File(tempDirName);
            list += "\n> " + tempDirName;
            list += (tempDir.isDirectory()) ? "[Dir]" : "";
            list += (tempDir.isFile()) ? "[File]" : "";
            list += (tempDir.canRead()) ? "[R]" : "[NR]";
            list += (tempDir.isHidden()) ? "[Hidden]" : "[Not Hidden]";
            list += (tempDir.exists()) ? "[Existing]" : "[Non Existing]";

        }

        // finally return the list of directories
        return list;
    }


}
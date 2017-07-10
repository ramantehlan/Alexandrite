package com.diamond.diamond;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import java.io.File;

public class Files extends AppCompatActivity {

    // This is the tag to be used for creating a Log
    //private static final String FILES_TAG = "tag.files";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        // Once the view is created, now we can assign the directoryTree
        TextView directoryTree = (TextView) findViewById(R.id.localDirectoryTree);
        TextView directoryTree2 = (TextView) findViewById(R.id.sdDirectoryTree);
        TextView heading = (TextView) findViewById(R.id.subTitle);
        TextView heading2 = (TextView) findViewById(R.id.subTitle2);
        String property = "\n";
        String property2 = "\n";
        //
        File root = this.getFilesDir();
        File root2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


        // For Internal Storage
        directoryTree.setText(displayDirectoryTree(root.getAbsolutePath()));
        // Just to print some properties about the main directory
        property += "[Last Modified: " + Long.toString(root.lastModified()) + "]";
        property += "[Free Space: " + Long.toString(root.getFreeSpace()) + "/" + Long.toString(root.getTotalSpace()) + "]";
        heading.setText(heading.getText().toString() + root.getParent() + property);

        // For External Storage
        directoryTree2.setText(displayDirectoryTree(root2.getParent()));
        // Just to print some properties about the main directory
        property2 += "[Last Modified: " + Long.toString(root2.lastModified()) + "]";
        property2 += "[Free Space: " + Long.toString(root2.getFreeSpace()) + "/" + Long.toString(root2.getTotalSpace()) + "]";
        heading2.setText(heading2.getText().toString() + root2.getParent() + property2);



    }


    // This function is called to display the directories
    public String displayDirectoryTree(String path) {
        File file = new File(path);
        String[] fileDirs = file.list();
        String list = "";

        // This is to create the list of directories
        for (String tempDirName : fileDirs) {

            File tempDir = new File(tempDirName);
            list += "> " + tempDirName;
            list += (tempDir.isDirectory()) ? "[Dir]" : "";
            list += (tempDir.isFile()) ? "[File]" : "";
            list += (tempDir.canRead()) ? "[R]" : "[NR]";
            //list += (tempDir.isHidden()) ? "[Hidden]" : "[Not Hidden]";
            //list += (tempDir.exists()) ? "[Existing]" : "[Non Existing]";
            list += "\n";

        }

        // finally return the list of directories
        return list;
    }


}
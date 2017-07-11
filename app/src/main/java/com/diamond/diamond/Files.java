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
        TextView sdPrivateDirectoryTree = (TextView) findViewById(R.id.sdPrivateDirectoryTree);
        TextView sdPublicDirectoryTree = (TextView) findViewById(R.id.sdPublicDirectoryTree);
        TextView heading = (TextView) findViewById(R.id.subTitle);
        TextView heading2 = (TextView) findViewById(R.id.subTitle2);
        TextView heading3 = (TextView) findViewById(R.id.subTitle3);

        // Properties
        String property = "\n";
        String property2 = "\n";
        String property3 = "\n";

        // Files
        File root = this.getFilesDir().getParentFile();
        File root2 = this.getExternalFilesDir(null).getParentFile();
        File root3 = Environment.getExternalStorageDirectory();
        String rootString = root.getPath();
        String rootString2 = root2.getPath();
        String rootString3 = root3.getPath();

        // For Internal Storage
        directoryTree.setText(displayDirectoryTree(rootString));
        // Just to print some properties about the main directory
        property += "[Last Modified: " + Long.toString(root.lastModified()) + "]";
        property += "[Free Space: " + Long.toString(root.getFreeSpace()) + "/" + Long.toString(root.getTotalSpace()) + "]";
        heading.setText(heading.getText().toString() + rootString + property);

        // For External Storage
        if (isExternalStorageReadable()) {
            // This is to print private directory files
            sdPrivateDirectoryTree.setText(displayDirectoryTree(rootString2));
            // Just to print some properties about the main directory
            property2 += "[Last Modified: " + Long.toString(root2.lastModified()) + "]";
            property2 += "[Free Space: " + Long.toString(root2.getFreeSpace()) + "/" + Long.toString(root2.getTotalSpace()) + "]";
            heading2.setText(heading2.getText().toString() + rootString2 + property2);

            // This is to print public directory files
            sdPublicDirectoryTree.setText(displayDirectoryTree(rootString3));
            // Just to print some properties about the main directory
            property3 += "[Last Modified: " + Long.toString(root3.lastModified()) + "]";
            property3 += "[Free Space: " + Long.toString(root3.getFreeSpace()) + "/" + Long.toString(root3.getTotalSpace()) + "]";
            heading3.setText(heading3.getText().toString() + rootString3 + property3);
        }

        // This is to update the external board
        TextView exRead = (TextView) findViewById(R.id.exRead);
        TextView exWrite = (TextView) findViewById(R.id.exWrite);

        String exReadable = exRead.getText().toString();
        exReadable = (isExternalStorageReadable()) ? exReadable + " Readable" : exReadable + " Not Readable";
        exRead.setText(exReadable);

        String exWritable = exWrite.getText().toString();
        exWritable = (isExternalStorageWritable()) ? exWritable + " Writable" : exWritable + " Not Writable";
        exWrite.setText(exWritable);

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
        if (list != "") {
            return list;
        } else {
            return "Empty Directory";
        }
    }

    // Check if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    // Check if external storage is available for at least read
    public boolean isExternalStorageReadable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

}
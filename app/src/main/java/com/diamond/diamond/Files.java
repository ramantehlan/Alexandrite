package com.diamond.diamond;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;

import static com.diamond.diamond.R.id.sdPrivateDirectoryTree;
import static com.diamond.diamond.R.id.sdPublicDirectoryTree;

public class Files extends AppCompatActivity {

    // Array of TextView to display the directory list
    public TextView[] directoriesView = {null};
    // Array of TextView to display properties of rootFiles
    public TextView[] propertiesView;
    // Array of String to store properties
    // This is also used to indicate if rootFile is Internal or External storage
    public String[] properties;
    // Array of Files to store rootFiles
    public File[] rootFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);


        TextView[] directoriesView = {(TextView) findViewById(R.id.localDirectoryTree),
                (TextView) findViewById(sdPrivateDirectoryTree),
                (TextView) findViewById(sdPublicDirectoryTree)
        };


        TextView[] propertiesView = {(TextView) findViewById(R.id.subTitle),
                (TextView) findViewById(R.id.subTitle2),
                (TextView) findViewById(R.id.subTitle3)
        };


        String[] properties = {"internal", "external", "external"};


        File[] rootFiles = {this.getFilesDir().getParentFile(),
                this.getExternalFilesDir(null).getParentFile(),
                Environment.getExternalStorageDirectory()
        };

        for (int i = 0; i < rootFiles.length; i++) {

            // To display the directories of rootFile by calling displayDirectoryTree function
            directoriesView[i].setText(displayDirectoryTree(rootFiles[i].toString()));

            // To get properties of rootFile
            // We also need to check properties since it's first purpose of telling us about storage is over
            properties[i] = "\n";
            properties[i] += "[Last Modified: " + Long.toString(rootFiles[i].lastModified()) + "]";
            properties[i] += "[Free Space: " + Long.toString(rootFiles[i].getFreeSpace()) + "/" + Long.toString(rootFiles[i].getTotalSpace()) + "]";

            // To print properties
            propertiesView[i].setText(propertiesView[i].getText().toString() + rootFiles[i].toString() + properties[i]);

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
            list += "- " + tempDirName;
            list += (tempDir.isDirectory()) ? "[Dir]" : "";
            list += (tempDir.isFile()) ? "[File]" : "";
            list += (tempDir.canRead()) ? "[R]" : "[NR]";
            //list += (tempDir.isHidden()) ? "[Hidden]" : "[Not Hidden]";
            //list += (tempDir.exists()) ? "[Existing]" : "[Non Existing]";
            list += "\n";

        }

        // finally return the list of directories
        if (!list.equals("")) {
            return list;
        } else {
            return "Empty Directory";
        }
    }


    // Check if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        return  (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    // Check if external storage is available for at least read
    public boolean isExternalStorageReadable() {
       return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()));
    }

}
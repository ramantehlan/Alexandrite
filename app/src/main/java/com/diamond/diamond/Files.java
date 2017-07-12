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

    // Tag to log
    //private static final String FILES_TAG = "tag.files";
    // Array of TextView to display the directory list
    public TextView[] directoriesView;
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

        directoriesView = new TextView[]{(TextView) findViewById(R.id.localDirectoryTree),
                (TextView) findViewById(sdPrivateDirectoryTree),
                (TextView) findViewById(sdPublicDirectoryTree)
        };

        propertiesView = new TextView[]{(TextView) findViewById(R.id.subTitle),
                (TextView) findViewById(R.id.subTitle2),
                (TextView) findViewById(R.id.subTitle3)
        };

        properties = new String[]{"internal", "external", "external"};

        rootFiles = new File[]{ this.getFilesDir().getParentFile(),
                this.getExternalFilesDir(null).getParentFile(),
                Environment.getExternalStorageDirectory()
        };

        for (int i = 0; i < rootFiles.length; i++) {
            if (properties[i].equals("external")) {
                if (this.isExternalStorageReadable()) {
                    this.displayDirectoryTree(i);
                } else {
                    directoriesView[i].setText(getResources().getText(R.string.external_storage_error));
                }
            } else {
                this.displayDirectoryTree(i);
            }
        }

        // This is to update the external board
        TextView exRead = (TextView) findViewById(R.id.exRead);
        TextView exWrite = (TextView) findViewById(R.id.exWrite);
        TextView exEmulated = (TextView) findViewById(R.id.exEmulated);

        String exReadable = exRead.getText().toString();
        exReadable += (isExternalStorageReadable()) ?  " Readable" : " Not Readable";
        exRead.setText(exReadable);

        String exWritable = exWrite.getText().toString();
        exWritable += (isExternalStorageWritable()) ?  " Mounted" : " Not Mounted";
        exWrite.setText(exWritable);

        String emulated = exEmulated.getText().toString();
        emulated += (Environment.isExternalStorageEmulated())?  " Emulated" : " Not Emulated";
        exEmulated.setText(emulated);
    }


    // This function is called to display the directories
    public String getDirectoryTree(String path) {
        File file = new File(path);
        String[] fileDirs = file.list();
        String list = "";

        if(file.exists() && fileDirs != null){
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
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    // Check if external storage is available for at least read
    public boolean isExternalStorageReadable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()));
    }

    public void displayDirectoryTree(int position) {
        // To display the directories of rootFile by calling displayDirectoryTree function
        directoriesView[position].setText(this.getDirectoryTree(rootFiles[position].toString()));

        // To get properties of rootFile
        // We also need to check properties since it's first purpose of telling us about storage is over
        properties[position] = "\n";
        properties[position] += "[Last Modified: " + Long.toString(rootFiles[position].lastModified()) + "]";
        properties[position] += "[Free Space: " + Long.toString(rootFiles[position].getFreeSpace()) + "/" + Long.toString(rootFiles[position].getTotalSpace()) + "]";

        // To print properties
        propertiesView[position].setText(propertiesView[position].getText().toString() + rootFiles[position].toString() + properties[position]);
    }

}
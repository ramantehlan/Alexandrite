package com.diamond.diamond;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.diamond.diamond.R.id.sdPrivateDirectoryTree;
import static com.diamond.diamond.R.id.sdPublicDirectoryTree;


public class Files extends AppCompatActivity {

    /**
     * Global variables are declared here
     */
    // Tag to log
    private static final String FILES_TAG = "tag.files";
    // Array of TextView to display the directory list
    public TextView[] directoriesView;
    // Array of TextView to display properties of rootFiles
    public TextView[] propertiesView;
    // Array of String to store properties
    // This is also used to indicate if rootFile is Internal or External storage
    public String[] properties;
    // Array of Files to store rootFiles
    public File[] rootFiles;

    /**
     * Main program start from here
     */

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

        rootFiles = new File[]{this.getCacheDir(),
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

        // This is to test if cache some file to internal cache
        if (this.createCacheFile(this.getCacheDir(), "inCacheFile", "this is cache content of internal cache")) {
            Log.e(FILES_TAG, "Internal cache file created successfully.");
        } else {
            Log.e(FILES_TAG, "Failed to create internal cache file.");
        }

        if(this.createCacheFile(this.getExternalCacheDir() , "exCacheFile" , "this is external cache content")){
            Log.e(FILES_TAG, "External cache file created successfully");
        } else {
            Log.e(FILES_TAG, "Failed to create external cache file.");
        }


        // This is to update the external board
        TextView exRead = (TextView) findViewById(R.id.exRead);
        TextView exWrite = (TextView) findViewById(R.id.exWrite);
        TextView exEmulated = (TextView) findViewById(R.id.exEmulated);

        String exReadable = exRead.getText().toString();
        exReadable += (isExternalStorageReadable()) ? " Readable" : " Not Readable";
        exRead.setText(exReadable);

        String exWritable = exWrite.getText().toString();
        exWritable += (isExternalStorageWritable()) ? " Mounted" : " Not Mounted";
        exWrite.setText(exWritable);

        String emulated = exEmulated.getText().toString();
        emulated += (Environment.isExternalStorageEmulated()) ? " Emulated" : " Not Emulated";
        exEmulated.setText(emulated);
    }


    /**
     * To get list of directories
     * @param path path of which directory list is requested
     * @return string which contain list of directory list
     */
    public String getDirectoryTree(String path) {
        File file = new File(path);
        String[] fileDirs = file.list();
        String list = "";

        if (file.exists() && fileDirs != null) {
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


    /**
     * To check if external storage is mounted or not
     * @return boolean
     */
    public boolean isExternalStorageWritable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    /**
     * To if external storage is mounted and readable
     * @return boolean
     */
    public boolean isExternalStorageReadable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()));
    }

    /**
     * To display directory according to the position
     * @param position this is the number which specify the index in following arrays
     *                 directoriesView
     *                 propertiesView
     *                 properties
     *                 rootFiles
     */
    private void displayDirectoryTree(int position) {
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

    /**
     * To create a cache file
     * @param cacheDirectoryPath path of cache directory
     * @param cacheFileName name of cache file
     * @param cacheContent content of cache file
     * @return boolean
     */
    public boolean createCacheFile(File cacheDirectoryPath, String cacheFileName, String cacheContent) {
        try {
            if (cacheDirectoryPath.exists()) {
                cacheDirectoryPath = File.createTempFile(cacheFileName, null, cacheDirectoryPath);
                File cacheFile = new File(cacheDirectoryPath, cacheFileName);

                FileWriter cacheFileWriter = new FileWriter(cacheFile.getAbsoluteFile());
                BufferedWriter cacheFileBuffWriter = new BufferedWriter(cacheFileWriter);
                cacheFileBuffWriter.write(cacheContent);
                cacheFileBuffWriter.close();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            // To print the error
            // We can also use file to store the error
            e.printStackTrace();
            return false;
        }
    }

    /**
     * To read a cache file
     * @param cacheDirectoryPath path of cache directory
     * @param cacheFileName name of cache file
     * @return content of cache file
     */
    public String readCacheFile(File cacheDirectoryPath, String cacheFileName) {
        String finalCacheData = "";
        try {
            cacheDirectoryPath = new File(cacheDirectoryPath, cacheFileName);
            // Check if cache file we are trying to read exists or not
            if(cacheDirectoryPath.exists()) {
                FileReader cacheReader = new FileReader(cacheDirectoryPath.getAbsoluteFile());
                BufferedReader br = new BufferedReader(cacheReader);

                String cacheDataLine;
                while ((cacheDataLine = br.readLine()) != null) {
                    finalCacheData += cacheDataLine + "\n";
                }

                br.close();
            }else{
                finalCacheData = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalCacheData;
    }

}
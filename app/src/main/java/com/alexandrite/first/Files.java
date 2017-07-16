package com.alexandrite.first;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.util.Log.e;
import static com.alexandrite.first.R.id.sdPrivateDirectoryTree;
import static com.alexandrite.first.R.id.sdPublicDirectoryTree;


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
        setContentView(com.alexandrite.first.R.layout.activity_files);

        directoriesView = new TextView[]{(TextView) findViewById(com.alexandrite.first.R.id.localDirectoryTree),
                (TextView) findViewById(sdPrivateDirectoryTree),
                (TextView) findViewById(sdPublicDirectoryTree)
        };

        propertiesView = new TextView[]{(TextView) findViewById(com.alexandrite.first.R.id.subTitle),
                (TextView) findViewById(com.alexandrite.first.R.id.subTitle2),
                (TextView) findViewById(com.alexandrite.first.R.id.subTitle3)
        };

        properties = new String[]{"internal", "external", "external"};

        rootFiles = new File[]{ this.getFilesDir().getParentFile(),
                this.getExternalFilesDir(null).getParentFile().getAbsoluteFile(),
                Environment.getExternalStorageDirectory().getAbsoluteFile()
        };

        for (int i = 0; i < rootFiles.length; i++) {
            if (properties[i].equals("external")) {
                if (this.isExternalStorageReadable()) {
                    this.displayDirectoryTree(i);
                } else {
                    directoriesView[i].setText(getResources().getText(com.alexandrite.first.R.string.external_storage_error));
                }
            } else {
                this.displayDirectoryTree(i);
            }
        }


        TextView cachingTestInternal = (TextView) findViewById(com.alexandrite.first.R.id.checkInternal);
        TextView cachingTestExternal = (TextView) findViewById(com.alexandrite.first.R.id.checkExternal);

        cachingTestInternal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkInternalCache();
                    }
                }
        );

        cachingTestExternal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkExternalCache();
                    }
                }
        );

        // This is to update the external board
        TextView exRead = (TextView) findViewById(com.alexandrite.first.R.id.exRead);
        TextView exWrite = (TextView) findViewById(com.alexandrite.first.R.id.exWrite);
        TextView exEmulated = (TextView) findViewById(com.alexandrite.first.R.id.exEmulated);

        String exReadable = exRead.getText().toString();
        exReadable += (isExternalStorageReadable()) ? " Readable" : " Not Readable";
        exRead.setText(exReadable);

        String exWritable = exWrite.getText().toString();
        exWritable += (isExternalStorageWritable()) ? " Mounted" : " Not Mounted";
        exWrite.setText(exWritable);

        String emulated = exEmulated.getText().toString();
        emulated += (Environment.isExternalStorageEmulated()) ? " Emulated" : " Not Emulated";
        exEmulated.setText(emulated);


        // This is to create file according to the name
        Button save = (Button) findViewById(com.alexandrite.first.R.id.saveFile);

        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView newFileName = (TextView) findViewById(com.alexandrite.first.R.id.newFileName);
                        TextView newFileContent = (TextView) findViewById(com.alexandrite.first.R.id.newFileContent);
                        Log.i(FILES_TAG , "File Name - " + newFileName.getText().toString() + " File Content - " + newFileContent.getText().toString());

                        if(newFileName.getText().toString().equals("") || newFileContent.getText().toString().equals("")){
                            Toast.makeText(getBaseContext() , "No Field can be empty" , Toast.LENGTH_SHORT).show();
                        }else {
                            writeCacheFile(getFilesDir(), newFileName.getText().toString(), newFileContent.getText().toString());
                        }
                    }
                }
        );

        if(new File(this.getFilesDir() + "/test.txt").exists()){
        	Toast.makeText(getBaseContext() , readCacheFile(getFilesDir(), "test.txt") , Toast.LENGTH_SHORT).show();
        }else{
        	 writeCacheFile(getFilesDir(), "test.txt", "This content is inside test.txt and if you can read this then test is clear!");
        }

    }


    /**
     * To get list of directories
     *
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
                list += (tempDir.isAbsolute())? "[Absolute]" : "";
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
     *
     * @return boolean
     */
    public boolean isExternalStorageWritable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    /**
     * To if external storage is mounted and readable
     *
     * @return boolean
     */
    public boolean isExternalStorageReadable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()));
    }

    /**
     * To display directory according to the position
     *
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
     *
     * @param cacheDirectoryPath path of cache directory
     * @param cacheFileName      name of cache file
     * @return boolean
     */
    public boolean createCacheFile(File cacheDirectoryPath, String cacheFileName) {
        try {
            if (cacheDirectoryPath.exists()) {
                File freshCacheFile = File.createTempFile(cacheFileName, null, cacheDirectoryPath);
                Log.i(FILES_TAG , "creating " + freshCacheFile.toString());
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
     * @param cacheDirectoryPath path of cache directory
     * @param cacheFileName      name of cache file
     * @param cacheContent       Content of cache file
     * @return boolean
     */
    public boolean writeCacheFile(File cacheDirectoryPath, String cacheFileName, String cacheContent) {
        try {
            File cacheFile = new File(cacheDirectoryPath, cacheFileName);
            // Check if cache file we are trying to read exists or not
            if (cacheDirectoryPath.exists()) {
                FileWriter fileWriter = new FileWriter(cacheFile.getAbsoluteFile());

                Log.i(FILES_TAG , "Writing to " + cacheFile.getAbsoluteFile());

                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(cacheContent);
                bufferedWriter.close();
                return true;
            } else {
                Log.e(FILES_TAG, "file not found while writing - " + cacheDirectoryPath.toString());
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(FILES_TAG, "IOException in Writing file");
            return false;
        }
    }

    /**
     * To read a cache file
     *
     * @param cacheDirectoryPath path of cache directory
     * @param cacheFileName      name of cache file
     * @return content of cache file
     */
    public String readCacheFile(File cacheDirectoryPath, String cacheFileName) {
        String finalCacheData = "";
        try {
            File cacheFile = new File(cacheDirectoryPath, cacheFileName);
            // Check if cache file we are trying to read exists or not
            if (cacheDirectoryPath.exists()) {
                FileReader cacheReader = new FileReader(cacheFile.getAbsoluteFile());
                BufferedReader br = new BufferedReader(cacheReader);

                Log.i(FILES_TAG , "Reading from " + cacheFile.getAbsoluteFile());

                String cacheDataLine;
                while ((cacheDataLine = br.readLine()) != null) {
                    finalCacheData += cacheDataLine + "\n";
                }

                br.close();
            } else {
                finalCacheData = null;
                Log.e(FILES_TAG, "file not found while reading - " + cacheDirectoryPath.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(FILES_TAG, "IOException in reading file");
        }

        return finalCacheData;
    }

    /**
     * to check caching
     *
     * @param storageType where you wanna check caching
     *                    internal or external
     * @return boolean
     */
    public boolean testCaching(String storageType) {

        String cacheFileName;
        File cacheFilePath;
        String content = "this is sample content of a cache file" +
                "\n just testing out few things";

        boolean create_result;
        boolean write_result;
        boolean read_result;

        switch (storageType) {
            case "internal":

                cacheFileName = "inCache";
                cacheFilePath = this.getCacheDir();

                if (this.createCacheFile(cacheFilePath, cacheFileName)) {
                    Log.i(FILES_TAG, "Internal cache file created successfully.");
                    create_result = true;
                } else {
                    e(FILES_TAG, "Failed to create internal cache file.");
                    create_result = false;
                }

                if (this.writeCacheFile(cacheFilePath, cacheFileName, content)) {
                    Log.i(FILES_TAG, "Internal cache file written.");
                    write_result = true;
                } else {
                    e(FILES_TAG, "Failed to write to internal cache file.");
                    write_result = false;
                }

                String internalCache = this.readCacheFile(cacheFilePath, cacheFileName);

                if (internalCache != null) {
                    Log.i(FILES_TAG, internalCache);
                    read_result = true;
                } else {
                    e(FILES_TAG, "Error reading internal cache");
                    read_result = false;
                }
                break;
            case "external":

                cacheFileName = "enCache";
                cacheFilePath = this.getExternalCacheDir();

                if (this.createCacheFile(cacheFilePath, cacheFileName)) {
                    Log.i(FILES_TAG, "External cache file created successfully");
                    create_result = true;
                } else {
                    e(FILES_TAG, "Failed to create external cache file.");
                    create_result = false;
                }

                if (this.writeCacheFile(cacheFilePath, cacheFileName, content)) {
                    Log.i(FILES_TAG, "External cache file written.");
                    write_result = true;
                } else {
                    e(FILES_TAG, "Failed to write to external cache file.");
                    write_result = false;
                }

                String externalCache = this.readCacheFile(cacheFilePath, cacheFileName);

                if (externalCache != null) {
                    Log.i(FILES_TAG, externalCache);
                    read_result = true;
                } else {
                    e(FILES_TAG, "Error reading external cache");
                    read_result = false;
                }

                break;
            default:
                Log.e(FILES_TAG, "Wrong storageType in testCaching");
                create_result = false;
                write_result = false;
                read_result = false;
                break;
        }

        return (create_result && write_result && read_result);
    }

    public void checkInternalCache() {
        TextView display = (TextView) findViewById(com.alexandrite.first.R.id.checkInternal);
        if (this.testCaching("internal")) {
            display.setText(getResources().getString(com.alexandrite.first.R.string.internalCachingSuccess));
        } else {
            display.setText(getResources().getString(com.alexandrite.first.R.string.internalCachingFailed));
        }
    }

    public void checkExternalCache() {
        TextView display = (TextView) findViewById(com.alexandrite.first.R.id.checkExternal);
        if (this.testCaching("external")) {
            display.setText(getResources().getString(com.alexandrite.first.R.string.externalCachingSuccess));
        } else {
            display.setText(getResources().getString(com.alexandrite.first.R.string.externalCachingFailed));
        }
    }

}
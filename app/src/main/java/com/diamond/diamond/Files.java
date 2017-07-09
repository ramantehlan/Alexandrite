package com.diamond.diamond;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Files extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);


           //Toast.makeText(getBaseContext() , this.getFilesDir().toString() , Toast.LENGTH_SHORT).show();


      // Comman variables
        String filename = "newfile.txt";
        FileOutputStream writeToFile;
        FileInputStream readFromFile;
        TextView display = (TextView) findViewById(R.id.fileDisplay);



        // This is to store data and read data from the internal storage
        try{
            writeToFile = openFileOutput(filename , Context.MODE_PRIVATE);
            writeToFile.write("sf as1231232!@#@!#@#$#$%$".getBytes());
            writeToFile.close();

            // This is to read from that file
            readFromFile = openFileInput(filename);

            int c;
            String temp = "";
            while ((c = readFromFile.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            readFromFile.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        // This is to create the temp file
        File file;
        try{
            String tempFilename = Uri.parse("http://www.google.com/file/test/back/new.html").getLastPathSegment();
            file = File.createTempFile(tempFilename , null , this.getCacheDir());
            // Above will create new.html
        }catch (IOException e){
            e.printStackTrace();
        }


        // This is to create new directory
        String path = this.getFilesDir().toString() +  "/userfiles/check.txt";
        File f = new File(path);
        if(f.mkdirs()){
           // Toast.makeText(getBaseContext() , "created " + path , Toast.LENGTH_LONG).show();
        }else{
            // Toast.makeText(getBaseContext() , "failed" , Toast.LENGTH_LONG).show();
            f.delete();
        }


        // This is to get list of all the files and directories
        String full = this.getFilesDir().getParent();
        File l = new File(full);
        String[] fileDir = l.list();
        String list = "";
        for(int a = 0; a < fileDir.length ; a++){
            list = list + "\n" + fileDir[a];
        }

        display.setText(list);

        Toast.makeText(getBaseContext() , full , Toast.LENGTH_LONG).show();


    }

}

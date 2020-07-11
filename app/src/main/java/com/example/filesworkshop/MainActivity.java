package com.example.filesworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final int APP_SPEC_INT = 1;
    private static final int APP_SPEC_EXT = 2;
    private static final int PUB_EXT = 3;

    EditText mInputTxt;
    Button mSaveBtn;
    Button mReadBtn;

    File mTargetFile;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //savedInstanceState --> method variable
        //Distinguish them using an "m"

        mInputTxt = (EditText) findViewById(R.id.inputTxt);

        mSaveBtn =
                (Button) findViewById(R.id.btnSave);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFile(); //every time click the button to save, it will write to the file
            }
        });


        mReadBtn = (Button) findViewById(R.id.btnRead);
        //reading --> when click the read button, it will read from the file (readFromFile)
        mReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromFile();
            }
        });

        String filePath = "SampleFolder";
        String fileName = "SampleFile.txt";
        mTargetFile = new File(getFilesDir(), filePath + "/" + fileName);
        //on target, we use this directory
        //this.getFilesDir() --> get the app-specific internal directory
    }

    protected void writeToFile() {
        try {
            // Make sure that the parent folder exists
            File parent = mTargetFile.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }

            // Write to file --> code here is to write to the specific internal apps
            //therefore, only need Step 1 and Step 2
            //How to get to target file? --> specified as a member variable as mTargetFile
            //Android convention: whatever the member variable, we put an "m" in front of it
            //Helps us to distinguish between the method variables (instance variable)
            FileOutputStream fos = new FileOutputStream(mTargetFile);
            fos.write(mInputTxt.getText().toString().getBytes());
            fos.close();
            //With the file object, can write to the file using the FileOutputStream
            //Write the whole text, from the text we get to the string, and from the string get the byte of the string
            //Then write to the file the output stream
            //after this, the file will be stored in the internal (app=-specific) storage

            mInputTxt.setText("");

            //In order to see the file itself go to view > tool windows > device file explorer
            //App-specific internal storage --> data > data > look for package name > files > SampleFile.txt
            //This is the file that we created

            Toast.makeText(this, "Write file ok!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void readFromFile() {
        String data = ""; //data is empty here
        try {
            // from the file itself, create a file input stream
            FileInputStream fis = new FileInputStream(mTargetFile);
            //then create a data stream
            DataInputStream in = new DataInputStream(fis);
            //And then create a buffered reader
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            //then get the string
            String strLine;
            //every time we read, must read every single line
            while ((strLine = br.readLine()) != null) {
                // then write the line to data
                data = data + strLine;
            }
            in.close();

            //then set the input with setText
            mInputTxt.setText(data);

            Toast.makeText(this, "Read file ok!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


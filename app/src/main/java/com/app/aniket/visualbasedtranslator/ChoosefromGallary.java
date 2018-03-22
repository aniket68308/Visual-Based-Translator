package com.app.aniket.visualbasedtranslator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChoosefromGallary extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "ScanActivity";
    InputStream image_stream;
    String datapath = "";
    Context context=this;
    Bitmap bitmap;
    ImageView imageView;
    static TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ScanActivity.part=2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosefrom_gallary);
        imageView = (ImageView)findViewById(R.id.imgView);
        textView = (TextView)findViewById(R.id.OCRTextView);
        findViewById(R.id.button4).setOnClickListener((View.OnClickListener) context);
    }
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
    @Override
    public void onClick(View v) {
        openImageChooser();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    ((ImageView) findViewById(R.id.imgView)).setImageURI(selectedImageUri);
                }
                try {
                    image_stream = getContentResolver().openInputStream(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(image_stream);
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if(!textRecognizer.isOperational()){
                    Toast.makeText(getApplicationContext(),"could not get the text",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for(int i =0;i<items.size();++i)
                    {
                        TextBlock myitem = items.valueAt(i);
                        sb.append(myitem.getValue());
                        sb.append("\n");
                    }
                    String languagePair = "en-fr";
                    String text = sb.toString();
                    //Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();

                    AsyncTask<String, Void, String> result = Translate(text,languagePair);
                    //textView.setText(sb.toString());
                }
            }
        }
    }

    AsyncTask<String, Void, String> Translate(String textToBeTranslated, String languagePair){

        TranslatorBackgroundTask translatorBackgroundTask= new TranslatorBackgroundTask(context);


        AsyncTask<String, Void, String> translationResult = translatorBackgroundTask.execute(textToBeTranslated,languagePair);


        Log.d("Translation Result", String.valueOf(translationResult)); // Logs the result in Android Monitor


        return translationResult;
    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}

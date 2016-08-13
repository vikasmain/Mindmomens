package com.example.dell.mindmomens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends Activity {

    private static final int CAMERA_REQUEST = 2000;
    ImageView img;
    Button share;
    Bitmap photo;
    Bitmap finalBitmap;
    Button addTag;
    String text = "";
    Bitmap bitmapResult;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.cameraimage);
        share = (Button) findViewById(R.id.share);
        Button camera = (Button) findViewById(R.id.camera);
        addTag = (Button) findViewById(R.id.tag);
        addTag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MainActivity.this);
                alert.setTitle("Enter Tag Line");
                final EditText input = new EditText(MainActivity.this);
                int maxLength = 14;
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                input.setFilters(FilterArray);
                alert.setView(input);
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                text = input.getText().toString();
                                TextView tv = new TextView(
                                        MainActivity.this);
                                LayoutParams params = new LinearLayout.LayoutParams(
                                        LayoutParams.MATCH_PARENT, 20);
                                tv.setLayoutParams(params);
                                tv.setText(text);
                                tv.setTextColor(Color.BLACK);
                                tv.setGravity(Gravity.CENTER);
                                tv.setBackgroundColor(Color.argb(50, 21, 27,
                                        141));
                                Bitmap textBitmap = Bitmap.createBitmap(
                                        photo.getWidth(), 20, Config.ARGB_8888);
                                Canvas textDraw = new Canvas(textBitmap);
                                tv.layout(0, 0, photo.getWidth(), 20);
                                tv.draw(textDraw);
                                Canvas c = new Canvas(bitmapResult);
                                c.drawBitmap(photo, 0, 0, null);
                                c.drawBitmap(textBitmap, 0,
                                        bitmapResult.getHeight() - 20, null);
                                // Paint p = new Paint();
                                // p.setColor(Color.BLACK);
                                // p.setAntiAlias(true);
                                // p.setDither(true);
                                //
                                // c.drawText(text, 10,
                                // bitmapResult.getHeight() - 30, p);
                                img.setImageBitmap(bitmapResult);
                            }
                        });
                alert.show();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String filepath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();
                String extraPath = String.valueOf(System.currentTimeMillis());
                filepath += "/" + extraPath + ".jpg";

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(filepath);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                bitmapResult.compress(CompressFormat.PNG, 100, fos);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.fromFile(new File(filepath)));
                startActivity(Intent.createChooser(share, "Share Image"));

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            photo = (Bitmap) data.getExtras().get("data");
            bitmapResult = Bitmap.createBitmap(photo.getWidth(),
                    photo.getHeight(), Config.ARGB_8888);
            Canvas c = new Canvas(bitmapResult);
            c.drawBitmap(photo, 0, 0, null);
            share.setEnabled(true);

        }
    }
}

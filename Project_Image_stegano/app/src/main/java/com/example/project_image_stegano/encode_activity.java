package com.example.project_image_stegano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class encode_activity extends AppCompatActivity {
    ImageView img_encode;
    Button encode_button;
    EditText msgText;
    EditText secretKeyText;
    private static final int Image_pick_code = 1000;
    private static final int permission_code_ = 1001;
    private ProgressDialog save;
    private Handler sectetKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_activity);
        //views..
        img_encode = findViewById(R.id.img_encode);

        //handle image clicks..
        img_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check run time permissions..
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        //permission not granted,request it..
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission..
                        requestPermissions(permission, permission_code_);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system os is less then marshmalloow
                    pickImageFromGallery();
                }
            }
        });
        encode_button = findViewById(R.id.encode_btn);
        encode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encodeData();
            }
        });
        msgText = findViewById(R.id.enter_msg_id);
        secretKeyText = findViewById(R.id.enter_key_id);

    }
        private void encodeData () {

            String message = msgText.getText().toString();
            String secretKey = secretKeyText.getText().toString();
            //Check if message or secret key is empty or not
            if (message.length()==0 ||secretKey.length()==0) {
                Toast.makeText(encode_activity.this, "Enter message and secret key ", Toast.LENGTH_SHORT).show();
            }
            else {
                Bitmap bmap;
                //convert image to bitmap
                BitmapDrawable bmapD = (BitmapDrawable) img_encode.getDrawable();
                bmap = bmapD.getBitmap();
                Bitmap operation = Bitmap.createBitmap(bmap.getWidth(), bmap.getHeight(), bmap.getConfig());
                String s = message;
                String d = new String();
                String m = new String();
                int c = 0;
                int l = 0;
                //to store pixels
                int p, r, b, g;
                for (int i = 0; i < bmap.getWidth(); i++) {
                    for (int j = 0; j < bmap.getHeight() - 3; j++) {
                        if (c < s.length()) {
                            String binary = Integer.toBinaryString((int) (s.charAt(c)));
                            //first pixel

                            p=bmap.getPixel(i,j);
                            r= Color.red(p);
                            d=Integer.toBinaryString(r);
                            d=d.substring(0,d.length()-1);

                            d+=binary.charAt(0);
                            m+=binary.charAt(0);
                            r=Integer.parseInt(d,2);

                            g=Color.green(p);
                            d=Integer.toBinaryString(g);
                            d=d.substring(0,d.length()-1);
                            d+=binary.charAt(1);
                            m+=binary.charAt(1);
                            g=Integer.parseInt(d,2);
                            b=Color.blue(p);
                            d=Integer.toBinaryString(b);
                            d=d.substring(0,d.length()-1);
                            d+=binary.charAt(2);
                            m+=binary.charAt(2);
                            b=Integer.parseInt(d,2);

                           operation.setPixel(i,j,Color.rgb(r,g,b));


                           //2nd pixel
                            j++;
                            p=bmap.getPixel(i,j);
                            r= Color.red(p);
                            d=Integer.toBinaryString(r);
                            d=d.substring(0,d.length()-1);

                            d+=binary.charAt(3);
                            m+=binary.charAt(3);
                            r=Integer.parseInt(d,2);

                            g=Color.green(p);
                            d=Integer.toBinaryString(g);
                            d=d.substring(0,d.length()-1);
                            d+=binary.charAt(4);
                            m+=binary.charAt(4);
                            g=Integer.parseInt(d,2);
                            b=Color.blue(p);
                            d=Integer.toBinaryString(b);
                            d=d.substring(0,d.length()-1);
                            d+=binary.charAt(5);
                            m+=binary.charAt(5);
                            b=Integer.parseInt(d,2);

                            operation.setPixel(i,j,Color.rgb(r,g,b));

                            //3rd pixel
                            j++;
                            p=bmap.getPixel(i,j);
                            r= Color.red(p);
                            d=Integer.toBinaryString(r);
                            d=d.substring(0,d.length()-1);

                            d+=binary.charAt(6);
                            m+=binary.charAt(6);
                            r=Integer.parseInt(d,2);

                            g=Color.green(p);
                            b=Color.blue(p);
                            operation.setPixel(i,j,Color.rgb(r,g,b));
                            c++;
                        }
                        else {

                            operation.setPixel(i,j,Color.rgb(0,0,0));

                        }
                    }
                }
                p=bmap.getPixel(bmap.getWidth()-1,bmap.getHeight()-1);
                r=Color.red(p);
                g=Color.red(p);
                b=Color.red(p);
                r=s.length();
                operation.setPixel(bmap.getWidth()-1,bmap.getHeight()-1,Color.rgb(r,g,b));
                img_encode.setImageBitmap(operation);
                System.out.println("done"+m);
                save();
            }
        }


    private void save()
    {
        BitmapDrawable bitmapDrawable=(BitmapDrawable) img_encode.getDrawable();
        Bitmap encoded_image=bitmapDrawable.getBitmap();

        final Bitmap imgToSave = encoded_image;
        Thread PerformEncoding = new Thread(new Runnable() {
            @Override
            public void run() {
                saveToInternalStorage(imgToSave);
            }
        });
        save = new ProgressDialog(encode_activity.this);
        save.setMessage("Saving, Please Wait...");
        save.setTitle("Saving Image");
        save.setIndeterminate(false);
        save.setCancelable(false);
        save.show();
        PerformEncoding.start();
    }
    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;


        Date currentTime = Calendar.getInstance().getTime();
        String s=currentTime.toString();
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), s + ".PNG"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            sectetKey.post(new Runnable() {
                @Override
                public void run() {
                    save.dismiss();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void pickImageFromGallery(){
            //intent to pick image
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, Image_pick_code);
        }
//handle result of runtime permission
        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){

            //handle result of picked image.


            switch (requestCode) {
                case permission_code_: {
                    if (grantResults.length > 0 && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                        //permission was granted
                        pickImageFromGallery();
                    } else {
                        //permission was denied
                        Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == Image_pick_code) {
                //set image to image view
                img_encode.setImageURI(data.getData());
            }
        }
    }

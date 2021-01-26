package com.example.project_image_stegano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class encode_activity extends AppCompatActivity {
    ImageView img_encode;

    private static final int Image_pick_code=1000;
    private static final int permission_code_=1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_activity);
        //views..
        img_encode=findViewById(R.id.img_encode);

        //handle image clicks..
        img_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check run time permissions..
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not granted,request it..
                        String[] permission ={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission..
                        requestPermissions(permission,permission_code_);
                    }
                    else
                    {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    //system os is less then marshmalloow
                    pickImageFromGallery();
                }
            }
        });


    }
    private void  pickImageFromGallery(){
        //intent to pick image
        Intent i=new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,Image_pick_code);
    }
//handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

      //handle result of picked image.


        switch(requestCode){
            case permission_code_:{
                if(grantResults.length >0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
                    //permission was granted
                    pickImageFromGallery();
                }
                else {
                    //permission was denied
                    Toast.makeText(this,"Permission denied...",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Image_pick_code) {
            //set image to image view
            img_encode.setImageURI(data.getData());
        }
    }
}

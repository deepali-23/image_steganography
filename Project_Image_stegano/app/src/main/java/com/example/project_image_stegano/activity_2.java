package com.example.project_image_stegano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_2 extends AppCompatActivity {
  private Button encode;
  private Button decode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        encode=(Button)findViewById(R.id.encode_id);
        decode=(Button)findViewById(R.id.decode_id);

        encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activity_2.this,encode_activity.class);
                startActivity(i);
            }
        });

        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activity_2.this,decode_.class);
                startActivity(i);
            }
        });

    }
}

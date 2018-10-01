package com.example.atique.divideimage;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Match extends AppCompatActivity {

    ImagesData getImages;
    BitmapFactory.Options options;
    ArrayList<String> bitmaps;
    String checkImg;
    ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        getImages = new ImagesData();
        final Intent intent = getIntent();
        checkImg = intent.getExtras().getString("picNumber");
        bitmaps = getImages.getAllImages(getApplicationContext(), checkImg);
        options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        GridView view = (GridView)findViewById(R.id.gridview);
        view.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 20;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null){
                    imageView = new ImageView(getApplicationContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
                    imageView.setPadding(8, 8, 8, 8);
                    imageView.setCropToPadding(true);
                }
                else{
                    imageView = (ImageView) convertView;
                }
                    imageView.setImageBitmap(BitmapFactory.decodeFile(bitmaps.get(position), options));
                return imageView;
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(bitmaps.get(position) == checkImg){
                    Toast.makeText(getApplicationContext(), "This is correct", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wrong guess", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }
}

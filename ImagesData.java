package com.example.atique.divideimage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by atique on 27/01/2018.
 */

public class ImagesData {

    SQLiteDatabase database;
    String path, getPath;
    int position;

    //Create database for images and get all images from mobile
    public void fetchImages(Activity activity) {
        database = activity.openOrCreateDatabase("ImageDatabase", activity.MODE_WORLD_WRITEABLE, null);
        Cursor c = database.rawQuery("Select name from sqlite_master where type='table' and name='ImageTable'",null);
        database.execSQL("Create Table if not exists ImageTable(ImagePath VARCHAR)");
        String[] projections = {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.MIME_TYPE};
        Cursor [] cursor = {activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projections, null,
                null, null), activity.getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, projections, null,
                null, null)};
        MergeCursor mergeCursor = new MergeCursor(cursor);
        mergeCursor.moveToFirst();
        int index = mergeCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        for (int i=0; i<mergeCursor.getCount(); i++) {
            getPath = mergeCursor.getString(index);
            database.execSQL("Insert into ImageTable values('" + getPath + "')");
            mergeCursor.moveToNext();
        }
        mergeCursor.close();
    }

    //getting image path randomly from database
    public String getImagePath(Context activity) {
        database = activity.openOrCreateDatabase("ImageDatabase", activity.MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select ImagePath from ImageTable", null);
        cursor.moveToFirst();
        position = new Random().nextInt(cursor.getCount()-1);
        cursor.moveToPosition(position);
        path = cursor.getString(cursor.getColumnIndexOrThrow("ImagePath"));
        cursor.close();

        return path;
    }

    public ArrayList<String> getAllImages(Context context, String imagePath){
        ArrayList<String> bitmapsID = new ArrayList<>();
        database = context.openOrCreateDatabase("ImageDatabase", context.MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("Select ImagePath from ImageTable", null);
        cursor.moveToFirst();
        for(int i=0; i<20; i++){
            position = new Random().nextInt(cursor.getCount() - 1);
            cursor.move(position);
            bitmapsID.add(cursor.getString(cursor.getColumnIndexOrThrow("ImagePath")));
            cursor.moveToFirst();
        }
        bitmapsID.add((int)Math.round(Math.random()*20), imagePath);
        cursor.close();
        return bitmapsID;
    }
}

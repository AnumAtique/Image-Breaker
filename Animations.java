package com.example.atique.divideimage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class Animations extends AppCompatActivity {

    RelativeLayout layout;
    ImageView view;
    Bitmap bitmap;
    int width, height, i = 0;
    AnimatorSet set;
    int divideBy3, getHeight, getWidth, left, top, x = 0;
    Button stop;
    ImagesData getPath;
    String picNumber;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);
        getPath = new ImagesData();

        stop = (Button)findViewById(R.id.stop);
        picNumber = getPath.getImagePath(getApplicationContext());
        if(picNumber != null) {
            bitmap = BitmapFactory.decodeFile(picNumber);
        }
        else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        }
        layout = (RelativeLayout) findViewById(R.id.animlayout);

        Intent intent = getIntent();
        final String number = intent.getExtras().getString("number");
        final String duration = intent.getExtras().getString("duration");
        set = new AnimatorSet();
        if(number!= null && duration!= null) {

            set.playSequentially(imagePieces(bitmap, Integer.parseInt(number), Integer.parseInt(duration)));
        }
        else{
            set.playSequentially(imagePieces(bitmap, 4, 2));
        }
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                layout.removeAllViews();
                if (getPath.getImagePath(getApplicationContext()) != null) {
                    picNumber = getPath.getImagePath(getApplicationContext());
                    bitmap = BitmapFactory.decodeFile(picNumber);
                }
                set.playSequentially(imagePieces(bitmap,
                        Integer.parseInt(number), Integer.parseInt(duration)));
                set.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "match", Toast.LENGTH_LONG).show();
                    Intent intentSend = new Intent(getApplicationContext(), Match.class);
                    intentSend.putExtra("picNumber", picNumber);
                    startActivity(intentSend);
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)

    protected ObjectAnimator[] imagePieces(Bitmap bitmap, int number, int duration) {

        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, 2000, 2000, false);

        x = 0;
            divideBy3 = (int) Math.round(number / 3);
            int[] rows = {divideBy3, divideBy3, number - (divideBy3 + divideBy3)};
            width = bitmap.getWidth();
            height = bitmap.getHeight();

            ArrayList<ObjectAnimator> animators = new ArrayList<>();
            top = 0;
            for (int i = 0; i < rows.length; i++) {

                    getHeight = (int) ((Math.ceil(Math.random() * (height - top - 2))) + top);
               // getHeight = (int)Math.floor(height/3);
                getHeight -= top;

                if(getHeight > (height/3)){
                    getHeight -= 400;
                }

                left = 0;

                if (getHeight == 0) {
                    getHeight = 2;
                }

                if (i == (rows.length - 1)) {
                    getHeight = height - top;
                }

                for (int j = 0; j < rows[i]; j++) {

                        getWidth = (int) (Math.random() * (width - left - 2) + left);
                    getWidth -= left;

                    if(getWidth > (width/3)){
                        getWidth -= 200;
                    }

                    if (getWidth == 0) {
                        getWidth = 2;
                    }

                    if (j == (rows[i] - 1)) {
                        getWidth = width - left;
                    }
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap, left, top, getWidth, getHeight);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getWidth / 4, getHeight / 4);
                    view = new ImageView(Animations.this);
                    view.setScaleType(ImageView.ScaleType.MATRIX);
                    view.setX(left / 4 + 90);
                    view.setY(0 - 1100);
                    view.setImageBitmap(bitmap1);
                    view.setAdjustViewBounds(true);
                    layout.addView(view, params);
                    animators.add(x, ObjectAnimator.ofFloat(view, "y", 100 + (int) Math.ceil(top / 4)));
                    animators.get(x).setDuration(duration * 1000);
                    x++;
                    left += getWidth;
                }
                top += getHeight;
            }

            ObjectAnimator[] objectAnimator = new ObjectAnimator[animators.size()];
            i = 0;
            while (animators.size() > 0) {
                int piecesSequence = (int) (Math.random() * (animators.size() - 1));
                objectAnimator[i] = animators.get(piecesSequence);
                objectAnimator[i].setInterpolator(new LinearInterpolator());
                animators.remove(piecesSequence);
                i++;
            }
        return objectAnimator;
    }


}
package com.example.welcome.iomt;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
ImageView iv;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv=(ImageView)findViewById(R.id.imageView2);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_screen);
animation.start();

        iv.setAnimation(animation);
        pb=(ProgressBar)findViewById(R.id.progressBar);

        ObjectAnimator anim1=ObjectAnimator.ofInt(pb,"progress",0,100);
        anim1.setDuration(4000);
        anim1.setInterpolator(new DecelerateInterpolator());
        anim1.start();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Thread.sleep(3000);
                    Intent i=new Intent(MainActivity.this,LoginActivity.class);

                    startActivity(i);
                }catch (Exception e){

                }
            }
        }.start();




    }
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    }


package com.example.welcome.iomt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ProcessImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_image);
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri uri=data.getData();
            Bitmap  mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            detectEdges(mBitmap);
        }catch (Exception e){
            Toast.makeText(ProcessImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void detectEdges(Bitmap bitmap) {
        try {

            Mat rgba = new Mat();
            Utils.bitmapToMat(bitmap, rgba);

            Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
            Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
            Imgproc.Canny(edges, edges, 80, 100);

            // Don't do that at home or work it's for visualization purpose.
            // BitmapHelper.showBitmap(this, bitmap, imageView);
            Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            iv.setImageBitmap(resultBitmap);
            Utils.matToBitmap(edges, resultBitmap);
        }catch(Exception e){
            Log.d("exception",e.getMessage());
            Toast.makeText(ProcessImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      //  BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
    }



}

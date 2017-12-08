package com.example.welcome.iomt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    ImageView ivCamera, ivGallery, ivUpload;
    String uuid = " ";
    LinearLayout linearMain;
    // String encodedString = " ";
    GalleryPhoto galleryPhoto;
    final int GALLERY_REQUEST = 1200;
    final String TAG = this.getClass().getSimpleName();
    String email = " ";
    String uid;
    ArrayList<String> imageList = new ArrayList<>();
    String encodedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        String status = sp.getString("lstatus", "no values");
        if (status.contains("logged in")) {
            email = sp.getString("email", "email");
        }

        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        ivCamera = (ImageView) findViewById(R.id.ivcamera);
        ivGallery = (ImageView) findViewById(R.id.ivgallery);
        ivUpload = (ImageView) findViewById(R.id.ivupload);


        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 10);
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = galleryPhoto.openGalleryIntent();
                startActivityForResult(in, GALLERY_REQUEST);
            }
        });
        final MyCommand myCommand = new MyCommand(getApplicationContext());


        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  RequestQueue rq = Volley.newRequestQueue(HomeActivity.this);
                uuid = generate();
                for (String imagePath : imageList) {
                    try {
                        Bitmap bitmap = PhotoLoader.init().from(imagePath).requestSize(400, 400).getBitmap();
                        encodedString = ImageBase64.encode(bitmap);
                        String url = "http://iomtcloud.esy.es/iomt/upload.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "error while uploading image" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("image", encodedString);
                                params.put("email", email);
                                params.put("uid", uuid);
                                return params;
                            }
                        };


                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                20000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        myCommand.add(stringRequest);

                        final Handler handler = new Handler();


                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "error while uploading image", Toast.LENGTH_SHORT).show();
                    }
                }
                myCommand.execute();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                galleryPhoto.setPhotoUri(data.getData());
                String photoPath = galleryPhoto.getPath();

                imageList.add(photoPath);

                Log.d(TAG, photoPath);
                try {
                    Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(400, 400).getBitmap();

                    ImageView imageView = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(layoutParams);

                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(0, 0, 0, 10);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);
                    linearMain.addView(imageView);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "error while loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String generate() {
        Random rand = new Random();
        int number = rand.nextInt(1000) + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(email.split("\\@")[0] + number);
        uid = sb.toString();
        return uid;
    }

}

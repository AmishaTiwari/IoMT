package com.example.welcome.iomt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText e1, e2;
    Button b1, b2,b3;
    String email, pass, url = "http://www.iomtcloud.esy.es/iomt/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        e1 = (EditText) findViewById(R.id.editText2);
        e2 = (EditText) findViewById(R.id.editText3);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3=  (Button)findViewById(R.id.button4);

        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        String status = sp.getString("rstatus", "no values");
        if (status.contains("registered"))

            e1.setText(sp.getString("email", "email").toString());


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        email = e1.getText().toString();
        pass = e2.getText().toString();
        switch (v.getId()) {
            case R.id.button:
                login();
               // startActivity(new Intent(LoginActivity.this,ProcessImage.class));
                break;
            case R.id.button2:
                register();
                break;

        }

    }

    public void login() {
        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
        StringRequest sq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            //responselistener for client side error
            //error listener for server error d

            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();

                if (response.contains("logged in")) {
                    SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
                    //session xml file is created

                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("lstatus", "logged in");
                    ed.putString("email", email);

                    //strs status to be lgd in
                    ed.commit();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);



               /*now the same status is pased on the 2nd activity
                String status=sp.getString("lstatus",)
               * if(status.consists()"novalue");
               * {homeactivity}*/

                    startActivity(i);
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })

//session management handles all usere settings
                //in prev yrs data strg was file
                //hence we hv xml tags like <ringtone>casio</ringtone> hence saved permanently
                //so agn n agn dont hv 2 sv ringtone
                //better usg f memory
                //so we create seesion so ech time a user opns app dont hv to login agn n agn

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pass", pass);
                return params;
            }
        };
        rq.add(sq);
    }

    public void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


}
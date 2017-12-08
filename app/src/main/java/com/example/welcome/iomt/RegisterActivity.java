package com.example.welcome.iomt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

EditText name,dob,age,email,phone,add,pass,pass2,comment;

    String sname;
    String sdob;
    String sage;
    String semail;
    String sphone;
    String sadd;
    String spass;
    String spass2;
    String scomment;

    Button save;

    String url = "http://iomtcloud.esy.es/iomt/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=(EditText)findViewById(R.id.editText);
        dob=(EditText)findViewById(R.id.editText4);
        age=(EditText)findViewById(R.id.editText5);
        email=(EditText)findViewById(R.id.editText6);
        phone=(EditText)findViewById(R.id.editText7);
        add=(EditText)findViewById(R.id.editText8);
        pass=(EditText)findViewById(R.id.editText9);
        pass2=(EditText)findViewById(R.id.editText10);
        comment=(EditText)findViewById(R.id.editText11);
        save=(Button)findViewById(R.id.button3);

        save.setOnClickListener(this);

        pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String p=s.toString();
                String p2=pass.getText().toString();
                if(!p2.equals(p)){
                    pass2.setError("Not Match");
                }

            }
        });
    }




    @Override
    public void onClick(View v) {

        sname = name.getText().toString();
        sdob = dob.getText().toString();
        sage = age.getText().toString();
        semail = email.getText().toString();
        sphone = phone.getText().toString();
        sadd = add.getText().toString();
        spass = pass.getText().toString();
        spass2 = pass2.getText().toString();
        scomment = comment.getText().toString();


        RequestQueue rq= Volley.newRequestQueue(RegisterActivity.this);
        StringRequest sq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this,response,Toast.LENGTH_LONG).show();

                if(response.contains("registered")) {
                    SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);

                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("rstatus", "registered");

                    ed.commit();
                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {   @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String>params=new HashMap<>();
            params.put("name",sname);
            params.put("dob",sdob);
            params.put("age",sage);
            params.put("email",semail);
            params.put("phone",sphone);
            params.put("add",sadd);
            params.put("pass",spass);
            params.put("comment",scomment);
            return params;
        }
        };;
    }
    }


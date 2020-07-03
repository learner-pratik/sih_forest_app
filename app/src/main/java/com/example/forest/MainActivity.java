package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText etUsername,etPassword;
    Button btnLogin;
    private String urlJsonArry = "https://forestweb.herokuapp.com/applogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendrequest(etUsername,etPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent a = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(a);

            }
        });
    }

    private void sendrequest(EditText user,EditText pass) throws JSONException {
        JSONObject rem=new JSONObject();
//        rem.remove("status");
        rem.put("username",user.getText());
        rem.put("password",pass.getText());
        Log.d("Param",rem.toString());
        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST,urlJsonArry,rem,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject data){
                        Log.d("response",data.toString());
                        try {
                            Log.d("response",data.get("id").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        FileOutputStream fileout= null;
                        try {
                            fileout = openFileOutput("mytextfile.txt",MODE_APPEND);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                            outputWriter.write(data.get("id").toString());
                            Log.d("opening","file");
                            outputWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Appcontroller.getInstance().addToRequestQueue(req);
    }
}
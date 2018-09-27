package ru.nicetoh8u.hotelbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity implements OnClickListener {

    Button buttonRegistration;
    EditText loginText;
    EditText passwordText;
    EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        buttonRegistration.setOnClickListener(this);
        loginText = (EditText) findViewById(R.id.registrationLogin);
        passwordText = (EditText) findViewById(R.id.registrationPassword);
        emailText = (EditText) findViewById(R.id.registrationEmail);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRegistration:
                //if not empty
                if ((loginText.getText().length()!=0) & (passwordText.getText().length()!=0) &
                        (emailText.getText().length()!=0)) {

                    String url = "http://10.0.2.2:8080/registration/";

                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }
                    ) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            HashMap<String, String> user = new HashMap<String, String>();
                            user.put("login", loginText.getText().toString());
                            user.put("email", emailText.getText().toString());
                            user.put("hash", passwordText.getText().toString());
                            return new JSONObject(user).toString().getBytes();
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    Volley.newRequestQueue(this).add(postRequest);


                    //Intent main = new Intent(this, MainActivity.class);
                    // startActivity(main);

                }

                break;
        }
    }
}

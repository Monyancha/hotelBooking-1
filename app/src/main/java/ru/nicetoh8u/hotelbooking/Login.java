package ru.nicetoh8u.hotelbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends AppCompatActivity implements OnClickListener {

    private Button buttonToRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        buttonToRegistration = (Button) findViewById(R.id.buttonToRegistration);
        buttonToRegistration.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonToRegistration:
                Intent registration = new Intent(this, Registration.class);
                startActivity(registration);
                break;
        }
    }
}

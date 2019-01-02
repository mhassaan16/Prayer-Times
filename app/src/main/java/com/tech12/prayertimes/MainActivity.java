package com.tech12.prayertimes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler db;
    EditText e1,e2,e3;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Sign Up");
        //session handling
        final User user=new User(MainActivity.this);
        if(user.getName()!="")
        {
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            intent.putExtra("name",user.getName());
            startActivity(intent);
            MainActivity.this.finish();
        }
        else {
            setContentView(R.layout.activity_main);
            getSupportActionBar().setTitle("Sign Up");
            db = new DatabaseHandler(this);
            e1 = (EditText) findViewById(R.id.username);
            e2 = (EditText) findViewById(R.id.pass);
            e3 = (EditText) findViewById(R.id.cpass);
            TextView tv = (TextView) findViewById(R.id.textView2);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });
            b = (Button) findViewById(R.id.register);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s1 = e1.getText().toString();
                    String s2 = e2.getText().toString();
                    String s3 = e3.getText().toString();
                    if (s1.equals("") || s2.equals("") || s3.equals("")) {
                        Toast.makeText(getApplicationContext(), "Empty Fields!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (s2.equals(s3)) {
                            Boolean checkUsername = db.checkUsername(s1);
                            if (checkUsername == true) {
                                Boolean insert = db.insert(s1, s2);
                                if (insert == true)
                                    Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password did not match!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        }
}

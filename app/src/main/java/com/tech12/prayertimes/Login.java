package com.tech12.prayertimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    DatabaseHandler db;
    EditText e1,e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHandler(this);
        e1=(EditText) findViewById(R.id.username);
        e2=(EditText) findViewById(R.id.pass);
        TextView tv = (TextView)findViewById(R.id.reg);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                Login.this.finish();
            }
        });
        Button b = (Button)findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1= e1.getText().toString();
                String s2= e2.getText().toString();
                Boolean userExists = db.userExists(s1,s2);
                if(userExists==true)
                {
                    Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    Login.this.finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Username or Password!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

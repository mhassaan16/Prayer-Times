package com.tech12.prayertimes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Log";
    String url;
    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;

    TextView fajr, dhuhr, asr, maghrib, isha, location, date;
    EditText search_et;
    Button search_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fajr = (TextView) findViewById(R.id.fajr);
        dhuhr = (TextView) findViewById(R.id.dhuhr);
        asr = (TextView) findViewById(R.id.asr);
        maghrib = (TextView) findViewById(R.id.maghrib);
        isha = (TextView) findViewById(R.id.isha);
        location = (TextView) findViewById(R.id.location);
        date = (TextView) findViewById(R.id.date);
        search_et= (EditText) findViewById(R.id.search);
        search_btn = (Button) findViewById(R.id.search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myLocation = search_et.getText().toString().trim();
                if(myLocation.isEmpty()){
                    Toast.makeText(HomeActivity.this, "Please enter location", Toast.LENGTH_SHORT).show();
                }
                else{
                    url = "http://muslimsalat.com/"+myLocation+".json?key=7f19cfbd644c7f067a758f00b35464df";
                    searchLocation();
                }
            }
        });
    }

    private void searchLocation() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Get location
                            String country = response.get("country").toString();
                            String state = response.get("state").toString();
                            String city = response.get("city").toString();
                            String Location = city +", "+state+", "+country;

                            String Date = response.getJSONArray("items").getJSONObject(0).get("date_for").toString();

                            //Get prayer timings
                            String Fajar = response.getJSONArray("items").getJSONObject(0).get("fajr").toString();
                            String Dhuhr = response.getJSONArray("items").getJSONObject(0).get("dhuhr").toString();
                            String Asr = response.getJSONArray("items").getJSONObject(0).get("asr").toString();
                            String Maghrib = response.getJSONArray("items").getJSONObject(0).get("maghrib").toString();
                            String Isha = response.getJSONArray("items").getJSONObject(0).get("isha").toString();

                            //Set data to the Textviews
                            fajr.setText(Fajar);
                            dhuhr.setText(Dhuhr);
                            asr.setText(Asr);
                            maghrib.setText(Maghrib);
                            isha.setText(Isha);
                            location.setText(Location);
                            date.setText(Date);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.duas:
                Intent intent = new Intent(this, DuasActivity.class);
                this.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

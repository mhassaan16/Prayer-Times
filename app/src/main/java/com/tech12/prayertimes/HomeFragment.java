package com.tech12.prayertimes;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    ViewPager viewPager;


    private static final String TAG = "Log";
    String url;
    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";
    ProgressDialog pDialog;

    TextView fajr, dhuhr, asr, maghrib, isha, location, date;
    EditText search_et;
    ImageView search_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fajr = (TextView) view.findViewById(R.id.fajr);
        dhuhr = (TextView) view.findViewById(R.id.dhuhr);
        asr = (TextView) view.findViewById(R.id.asr);
        maghrib = (TextView) view.findViewById(R.id.maghrib);
        isha = (TextView) view.findViewById(R.id.isha);
        location = (TextView) view.findViewById(R.id.location);
        date = (TextView) view.findViewById(R.id.date);
        search_et = (EditText) view.findViewById(R.id.search);
        search_btn = (ImageView) view.findViewById(R.id.search_btn);
        //imageslider
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext());
        viewPager.setAdapter(viewPagerAdapter);
        //automatic movment of pics
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);

       search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myLocation = search_et.getText().toString().trim();
                if (myLocation.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter location", Toast.LENGTH_SHORT).show();
                } else {
                    url = "http://muslimsalat.com/" + myLocation + ".json?key=7f19cfbd644c7f067a758f00b35464df";
                    searchLocation();
                }
            }
        });
        return view;
    }

    private void searchLocation() {

        pDialog = new ProgressDialog(getContext());
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
                            String Location = city + ", " + state + ", " + country;

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
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((HomeActivity) getActivity()).setActionBarTitle("Home");
    }
    //automatic movment of pics
    public class MyTimerTask extends TimerTask {


        @Override
        public void run() {
            HomeFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                    }
                    else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                    }
                    else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }
}


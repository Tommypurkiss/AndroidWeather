package com.tommypurkissdev.weather;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    public TextView tvTemperature;
    public TextView tvLocation;
    public TextView tvDescription;
    public RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTemperature = findViewById(R.id.tv_temp);
        tvLocation = findViewById(R.id.tv_location);
        tvDescription = findViewById(R.id.tv_description);

        mRequestQueue = Volley.newRequestQueue(this);


        //OnCreate loads up weather data
        getWeather();



        //pulling down the screen refreshes the weather data
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();

                pullToRefresh.setRefreshing(false); // keep on false to keep refreshing? 
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




    }

    //calls get data and shows a toast message
    private void refreshData() {
        getWeather();
        //TODO change toast text later on
        Toast.makeText(this, "Refreshed Weather Data Complete", Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //weather api calls updated every 10 minutes from openweather
    private void getWeather() {

        String url = "https://api.openweathermap.org/data/2.5/find?q=London,uk&units=metric&appid=7b35bff27c44e02a0ed4347c65c2ffa8";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    //list array
                    JSONArray jsonArray = response.getJSONArray("list");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);


                    //main object
                    JSONObject main = jsonObject.getJSONObject("main");


                    //weather array inside list array
                    JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);


                    //main get string calls temp
                    String temp = main.getString("temp");
                    String tempFormat = String.valueOf(temp).split("\\.")[0];
                    tvTemperature.setText(tempFormat);


                    //json object has name
                    String city = jsonObject.getString("name");
                    tvLocation.setText(city);

                    //json object1 get string calls description from array
                    String desc = jsonObject1.getString("description");
                    tvDescription.setText(desc);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

}


/* MARK: - SPARE CODE


 */


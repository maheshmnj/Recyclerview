package com.example.recyclerview;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO:Call http request and add data to List
        setContentView(R.layout.activity_main);
        getData();
    }

    public void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://randomuser.me/api/?results=100";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("Response class:" + response.substring(0, 10), response.substring(10, 20));
//                        Toast.makeText(getApplicationContext(), response.substring(0, 10), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                mNames.add(user.getJSONObject("name").getString("first") + " " + user.getJSONObject("name").getString("last"));
                                mImages.add(user.getJSONObject("picture").getString("large"));
//                                System.out.println("json Array length = " + jsonArray.length() + user.getJSONObject("name").getString("first"));
//                                Log.d(user.getJSONObject("name").getString("first"), "USer onResponse: ");
                            }
                            initRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(error.toString(), "onErrorResponse:");
                Toast.makeText(getApplicationContext(), "failed to get Response", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    public void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImages, mNames, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
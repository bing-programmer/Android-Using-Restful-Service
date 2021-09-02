/*
 * Author: Bing He (Carol)
 * Date: May 10, 2021
 * Course: Threaded Project for OOSD (PROJ-207-A) Term 3
 * Project: Workshop 8 --- CMPP264 Android
 * Purpose: This file is MainActivity.java, which defines the methods for activity_main.xml
 */

package com.example.workshop8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    //define variables
    ListView lvCustomers;
    Button btnAdd;
    RequestQueue requestQueue;
    //define onCreate() method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //restore UI state and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initiate new requestQueue using Volley
        requestQueue = Volley.newRequestQueue(this);
        //using variables to find views
        lvCustomers = findViewById(R.id.lvCustomers);
        btnAdd = findViewById(R.id.btnAdd);
        //execute GetCustomers() method to populate the lvCustomers list view
        Executors.newSingleThreadExecutor().execute(new GetCustomers());
        //define setOnItemClickListener for lvCustomers
        lvCustomers.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewCustomer customer = (ListViewCustomer) lvCustomers.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("listviewcustomer", customer);
                startActivity(intent);
            }
        });
        //define setOnclickListener for btnAdd button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }
        });
    }
    //define GetCustomers() method to serve in above execute() method to populate lvCustomers list view
    class GetCustomers implements Runnable {
        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.0.209:8080/WorkShop7_v2_war_exploded/api/customer/getallcustnames";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    ArrayAdapter<ListViewCustomer> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject cust = jsonArray.getJSONObject(i);
                            ListViewCustomer customer = new ListViewCustomer(cust.getInt("CustomerId"), cust.getString("CustFirstName"),
                                    cust.getString("CustLastName"));
                            adapter.add(customer);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //update ListView with the adapter of Agents
                    final ArrayAdapter<ListViewCustomer> finalAdapter = adapter;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvCustomers.setAdapter(finalAdapter);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.wtf(error.getMessage(), "utf-8");
                }
            });
            requestQueue.add(stringRequest);
        }
    }


} // end of class
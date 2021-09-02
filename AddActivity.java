/*
 * Author: Angelito Tuguinay
 * Date: May 10, 2021
 * Course: Threaded Project for OOSD (PROJ-207-A) Term 3
 * Project: Workshop 8 --- CMPP264 Android
 * Purpose: This file is DetailActivity.java, which defines the methods used in activity_detail.xml
 */
package com.example.workshop8;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.Executors;

public class AddActivity extends AppCompatActivity {
    TextView tvFirstName;
    TextView tvLastName;
    TextView tvAddress;
    TextView tvCity;
    TextView tvPostal;
    TextView tvCountry;
    TextView tvHomePhone;
    TextView tvBusPhone;
    TextView tvEmail;
    TextView tvAgentId;
    TextView tvProvince;
    Button btnSaveAdd;
    Button btnCancel;
    Button btnBack;

    RequestQueue requestQueue;


    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        requestQueue = Volley.newRequestQueue(this);

        btnSaveAdd = findViewById(R.id.btnSaveAdd);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvAddress = findViewById(R.id.tvAddress);
        tvCity = findViewById(R.id.tvCity);
        tvProvince = findViewById(R.id.tvProvince);
        tvPostal = findViewById(R.id.tvPostal);
        tvCountry = findViewById(R.id.tvCountry);
        tvHomePhone = findViewById(R.id.tvHomePhone);
        tvBusPhone = findViewById(R.id.tvBusPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAgentId = findViewById(R.id.tvAgentId);


        //---Add button -----------------------------------------------------------------------//
        //--adds to DB ------------------------------------------------------------------------//
        btnSaveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(tvFirstName.getText()) && TextUtils.isEmpty(tvLastName.getText())
                        && TextUtils.isEmpty(tvAddress.getText()) && TextUtils.isEmpty(tvCity.getText())
                && TextUtils.isEmpty(tvProvince.getText()) && TextUtils.isEmpty(tvPostal.getText())
                && TextUtils.isEmpty(tvCountry.getText()) && TextUtils.isEmpty(tvHomePhone.getText())
                && TextUtils.isEmpty(tvBusPhone.getText()) && TextUtils.isEmpty(tvEmail.getText())
                && TextUtils.isEmpty(tvAgentId.getText())) {
                    tvFirstName.setError("Please provide text");
                    tvLastName.setError("Please provide text");
                    tvAddress.setError("Please provide text");
                    tvCity.setError("Please provide text");
                    tvProvince.setError("Please provide text");
                    tvPostal.setError("Please provide text");
                    tvCountry.setError("Please provide text");
                    tvHomePhone.setError("Please provide text");
                    tvBusPhone.setError("Please provide text");
                    tvEmail.setError("Please provide text");
                    tvAgentId.setError("Please provide text");
                } else {
                    Customer customer = new Customer(0,
                            tvFirstName.getText().toString(), tvLastName.getText().toString(),
                            tvAddress.getText().toString(), tvCity.getText().toString(),
                            tvProvince.getText().toString(), tvPostal.getText().toString(),
                            tvCountry.getText().toString(), tvHomePhone.getText().toString(),
                            tvBusPhone.getText().toString(), tvEmail.getText().toString(),
                            Integer.parseInt(tvAgentId.getText().toString())
                    );
                    Executors.newSingleThreadExecutor().execute(new PutCustomer(customer));

                    //Add toast to show that adding was successful
                    Toast toast = Toast.makeText(AddActivity.this, "New Customer Added", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    openDialogue();

                    //Make the form fields empty
                    tvFirstName.setText("");
                    tvLastName.setText("");
                    tvAddress.setText("");
                    tvCity.setText("");
                    tvProvince.setText("");
                    tvPostal.setText("");
                    tvCountry.setText("");
                    tvHomePhone.setText("");
                    tvBusPhone.setText("");
                    tvEmail.setText("");
                    tvAgentId.setText("");
                }
            }
        });

        //--Back button --------------------------------------//
        //Sends user back to MainActivity -------------------//
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    
        //--Cancel button -------------------------------------//
        //--Clears the text fields----------------------------//
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFirstName.setText("");
                tvLastName.setText("");
                tvAddress.setText("");
                tvCity.setText("");
                tvProvince.setText("");
                tvPostal.setText("");
                tvCountry.setText("");
                tvHomePhone.setText("");
                tvBusPhone.setText("");
                tvEmail.setText("");
                tvAgentId.setText("");
            }
        });
    }

    //--Open custom_dialog ------------------//
    public void openDialogue(){
        dialog = new Dialog(AddActivity.this);
        dialog.setContentView(R.layout.custom_dialogue);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }, 1500);
    }
    
    //--Sends JSON data to REST API-------------------------// 
    class PutCustomer implements Runnable {
        private Customer customer;

        public PutCustomer(Customer customer)
        {
            this.customer = customer;
        }

        @Override
        public void run() {
            //send JSON data to REST (WS7) service
            String url = "http://10.0.0.209:8080/WorkShop7_v2_war_exploded/api/customer/putcustomer";
            JSONObject obj = new JSONObject();
            try {
                obj.put("CustomerId", customer.getCustomerId() + "");
                obj.put("CustFirstName", customer.getCustFirstName());
                obj.put("CustLastName", customer.getCustLastName());
                obj.put("CustAddress", customer.getCustAddress());
                obj.put("CustCity", customer.getCustCity());
                obj.put("CustProv", customer.getCustProv());
                obj.put("CustPostal", customer.getCustPostal());
                obj.put("CustCountry", customer.getCustCountry());
                obj.put("CustHomePhone", customer.getCustHomePhone());
                obj.put("CustBusPhone", customer.getCustBusPhone());
                obj.put("CustEmail", customer.getCustEmail());
                obj.put("AgentId", customer.getAgentId() + "");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("group1", "response=" + response);
                            VolleyLog.wtf(response.toString(), "utf-8");

                            //display result message
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("group1", "error=" + error);
                            VolleyLog.wtf(error.getMessage(), "utf-8");
                        }
                    });
            requestQueue.add(jsonObjectRequest);
        }
    }
}

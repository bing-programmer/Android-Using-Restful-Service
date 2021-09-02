/*
 * Author: (Group 1) Bing He, Gabriel Capobianco Rey, Angelito Tuguinay, Oluseyi Adepoju
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    //---- Gabriel Capobianco Rey ------------------------------------------------------------------
    // define variables-----------------------------------------------------------------------------
    EditText etCustomerId, etFirstName, etLastName, etAddress, etCity, etProvince,
            etPostalCode, etCountry, etHomePhone, etBusPhone, etEmail, etAgentId;

    Button btnUpdate, btnDelete;
    ListViewCustomer lvCustomers;

    RequestQueue requestQueue;

    Dialog confirmDeleteDialog;
    Dialog deletedDialog;
    Dialog confirmUpdateDialog;
    Dialog updatedDialog;

    Button btnYes, btnNo, btnUpdateYes, btnUpdateNo;

    // -------Gabriel Capobianco Rey and Angelito Tuguinay---define onCreate() method---------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // -------Gabriel Capobianco Rey------------------------------------------------------------
        //restore UI state and set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //initiate new requestQueue using Volley
        requestQueue = Volley.newRequestQueue(this);
        //define variables
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        etCustomerId = findViewById(R.id.etCustomerId);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etProvince = findViewById(R.id.etProvince);
        etPostalCode = findViewById(R.id.etPostalCode);
        etCountry = findViewById(R.id.etCountry);
        etHomePhone = findViewById(R.id.etHomePhone);
        etBusPhone = findViewById(R.id.etBusPhone);
        etEmail = findViewById(R.id.etEmail);
        etAgentId = findViewById(R.id.etAgentId);

        btnDelete = findViewById(R.id.btnDelete);

        // -------Angelito Tuguinay-----------------------------------------------------------------
        //Dialog///////////////////////////////////////////
        confirmDeleteDialog = new Dialog(DetailActivity.this);
        confirmDeleteDialog.setContentView(R.layout.confirm_dialog);
        confirmDeleteDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        confirmDeleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmDeleteDialog.setCancelable(false);

        confirmUpdateDialog = new Dialog(DetailActivity.this);
        confirmUpdateDialog.setContentView(R.layout.confirm_update_dialog);
        confirmUpdateDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        confirmUpdateDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmUpdateDialog.setCancelable(false);
        /////////////////////////////////////////////
        // -------Gabriel Capobianco Rey------------------------------------------------------------
        //use current Intent and get data from "listviewcustomer" for the selected customer
        Intent intent = getIntent();
        lvCustomers = (ListViewCustomer) intent.getSerializableExtra("listviewcustomer");
        //execute GetCustomer() method to populate the detail data for selected customer
        Executors.newSingleThreadExecutor().execute(new GetCustomer(lvCustomers.getCustomerId()));
        // -------Angelito Tuguinay-----------------------------------------------------------------
        /////DIALOG///////////////////////////////////////////////
        btnYes = confirmDeleteDialog.findViewById(R.id.btnYes);
        btnNo = confirmDeleteDialog.findViewById(R.id.btnNo);

        btnUpdateYes = confirmUpdateDialog.findViewById(R.id.btnUpdateYes);
        btnUpdateNo = confirmUpdateDialog.findViewById(R.id.btnUpdateNo);
        ////////////////////////////////////////////////
        // -------Angelito Tuguinay----------------------------------------------------------------
        //ConfirmDialog onClick listeners/////////////////////////////////
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new DeleteCustomer(lvCustomers.getCustomerId()));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                confirmDeleteDialog.dismiss();
                openDeletedDialogue();
            }
        });
        // -------Angelito Tuguinay----------------------------------------------------------------
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog.dismiss();
            }
        });
        ///////////////////////////////////////////////////////////
        // -------Angelito Tuguinay----------------------------------------------------------------
        //ConfirmUpdateDialog onClick listeners///////////////////////////////
        btnUpdateYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer customer = new Customer(Integer.parseInt(etCustomerId.getText().toString()),
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        etAddress.getText().toString(),
                        etCity.getText().toString(),
                        etProvince.getText().toString(),
                        etPostalCode.getText().toString(),
                        etCountry.getText().toString(),
                        etHomePhone.getText().toString(),
                        etBusPhone.getText().toString(),
                        etEmail.getText().toString(),
                        Integer.parseInt(etAgentId.getText().toString())
                );
                Executors.newSingleThreadExecutor().execute(new PostCustomer(customer));
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                confirmUpdateDialog.dismiss();
                openUpdatedDialogue();
            }
        });
        // -------Angelito Tuguinay----------------------------------------------------------------
        btnUpdateNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdateDialog.dismiss();
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////
        // -------Angelito Tuguinay----------------------------------------------------------------
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdateDialog.show();
            }
        });

        // -------Angelito Tuguinay-----------------------------------------------------------------
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog.show();
            }
        });
    }// end of oncreate

    // -------Angelito Tuguinay---------------------------------------------------------------------
    public void openDeletedDialogue(){
        deletedDialog = new Dialog(DetailActivity.this);
        deletedDialog.setContentView(R.layout.deleted_dialog);
        deletedDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        deletedDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        deletedDialog.setCancelable(false);
        deletedDialog.show();
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            if(deletedDialog.isShowing()){
                deletedDialog.dismiss();
            }
        }
    }, 1500);
    }
    // -------Angelito Tuguinay---------------------------------------------------------------------
    public void openUpdatedDialogue(){
        updatedDialog = new Dialog(DetailActivity.this);
        updatedDialog.setContentView(R.layout.updated_dialog);
        updatedDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        updatedDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        updatedDialog.setCancelable(false);
        updatedDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(updatedDialog.isShowing()){
                    updatedDialog.dismiss();
                }
            }
        }, 1500);
    }

// -------Gabriel Capobianco Rey--------------------------------------------------------------------
// Get customer class to populate detail data for the selected customer
class GetCustomer implements Runnable {
    private int customerId;

    public GetCustomer(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public void run() {
        //retrieve JSON data from REST service into StringBuffer
        StringBuffer buffer = new StringBuffer();
        String url = "http://10.0.0.209:8080/WorkShop7_v2_war_exploded/api/customer/getcustomer/" + customerId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VolleyLog.wtf(response, "utf-8");

                //convert JSON data from response string into an Agent
                JSONObject cust = null;
                try {
                    cust = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //update ListView with the adapter of Customers
                final JSONObject finalCust = cust;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            etCustomerId.setText(finalCust.getInt("CustomerId") + "");
                            etFirstName.setText(finalCust.getString("CustFirstName"));
                            etLastName.setText(finalCust.getString("CustLastName"));
                            etAddress.setText(finalCust.getString("CustAddress"));
                            etCity.setText(finalCust.getString("CustCity"));
                            etProvince.setText(finalCust.getString("CustProv"));
                            etPostalCode.setText(finalCust.getString("CustPostal"));
                            etCountry.setText(finalCust.getString("CustCountry"));
                            etHomePhone.setText(finalCust.getString("CustHomePhone"));
                            etBusPhone.setText(finalCust.getString("CustBusPhone"));
                            etEmail.setText(finalCust.getString("CustEmail"));
                            etAgentId.setText(finalCust.getInt("AgentId") + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
}// end of get customer class

// -------Bing He-----------------------------------------------------------------------------------
// PostCustomer class is to update customer detail data
    class PostCustomer implements Runnable {
        private Customer customer;

        public PostCustomer(Customer customer) {
            this.customer = customer;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.0.209:8080/WorkShop7_v2_war_exploded/api/customer/updatecustomer";
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
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
    }// end of PostCustomer class

    // -------Oluseyi Adepoju-----------------------------------------------------------------------
    // DeleteCustomer class is to deleted the selected customer
    class DeleteCustomer implements Runnable {
        private int customerId;

        public DeleteCustomer(int customerId) {
            this.customerId = customerId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.0.209:8080/WorkShop7_v2_war_exploded/api/customer/deletecustomer/" + customerId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
    } // end of delete class

}// end of class

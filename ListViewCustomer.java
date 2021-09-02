/*
 * Author: Bing He (Carol)
 * Date: May 10, 2021
 * Course: Threaded Project for OOSD (PROJ-207-A) Term 3
 * Project: Workshop 8 --- CMPP264 Android
 * Purpose: This file is ListViewCustomer.java, which defines the ListViewCustomer class
 * ListViewCustomer class is a scaled down Customer class
 */
package com.example.workshop8;

import java.io.Serializable;

public class ListViewCustomer implements Serializable {
    //define constant and variables
    private static final long serialVersionUID = 1L;

    private int customerId;

    private String custFirstName;

    private String custLastName;
    //define constructors
    public ListViewCustomer() {
    }

    public ListViewCustomer(int customerId, String custFirstName, String custLastName) {
        this.customerId = customerId;
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
    }
    //define getters and setters
    public int getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustFirstName() {
        return this.custFirstName;
    }

    public void setCustFirstName(String agtFirstName) {
        this.custFirstName = custFirstName;
    }

    public String getCustLastName() {
        return this.custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }
    //define toString() method
    @Override
    public String toString() {
        return custFirstName + ' ' + custLastName;
    }
}


/*
 * Author: Bing He (Carol)
 * Date: May 10, 2021
 * Course: Threaded Project for OOSD (PROJ-207-A) Term 3
 * Project: Workshop 8 --- CMPP264 Android
 * Purpose: This file is Customer.java, which defines the Customer class
 */
package com.example.workshop8;

import java.io.Serializable;

public class Customer implements Serializable {
    //define member variables
    private int CustomerId;
    private String CustFirstName;
    private String CustLastName;
    private String CustAddress;
    private String CustCity;
    private String CustProv;
    private String CustPostal;
    private String CustCountry;
    private String CustHomePhone;
    private String CustBusPhone;
    private String CustEmail;
    private int AgentId;

    //define constructor
    public Customer(int customerId, String custFirstName, String custLastName, String custAddress,
                    String custCity, String custProv, String custPostal, String custCountry,
                    String custHomePhone, String custBusPhone, String custEmail, int agentId) {

        CustomerId = customerId;
        CustFirstName = custFirstName;
        CustLastName = custLastName;
        CustAddress = custAddress;
        CustCity = custCity;
        CustProv = custProv;
        CustPostal = custPostal;
        CustCountry = custCountry;
        CustHomePhone = custHomePhone;
        CustBusPhone = custBusPhone;
        CustEmail = custEmail;
        AgentId = agentId;
    }
    //define getters and setters
    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getCustFirstName() {
        return CustFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        CustFirstName = custFirstName;
    }

    public String getCustLastName() {
        return CustLastName;
    }

    public void setCustLastName(String custLastName) {
        CustLastName = custLastName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getCustCity() {
        return CustCity;
    }

    public void setCustCity(String custCity) {
        CustCity = custCity;
    }

    public String getCustProv() {
        return CustProv;
    }

    public void setCustProv(String custProv) {
        CustProv = custProv;
    }

    public String getCustPostal() {
        return CustPostal;
    }

    public void setCustPostal(String custPostal) {
        CustPostal = custPostal;
    }

    public String getCustCountry() {
        return CustCountry;
    }

    public void setCustCountry(String custCountry) {
        CustCountry = custCountry;
    }

    public String getCustHomePhone() {
        return CustHomePhone;
    }

    public void setCustHomePhone(String custHomePhone) {
        CustHomePhone = custHomePhone;
    }

    public String getCustBusPhone() {
        return CustBusPhone;
    }

    public void setCustBusPhone(String custBusPhone) {
        CustBusPhone = custBusPhone;
    }

    public String getCustEmail() {
        return CustEmail;
    }

    public void setCustEmail(String custEmail) {
        CustEmail = custEmail;
    }

    public int getAgentId() {
        return AgentId;
    }

    public void setAgentId(int agentId) {
        AgentId = agentId;
    }
    //define toString() method
    @Override
    public String toString() {
        return CustFirstName + " " + CustLastName;
    }

    public String showCustomer() {
        return "Customer{" +
                "CustomerId=" + CustomerId +
                ", CustFirstName='" + CustFirstName + '\'' +
                ", CustLastName='" + CustLastName + '\'' +
                ", CustAddress='" + CustAddress + '\'' +
                ", CustCity='" + CustCity + '\'' +
                ", CustProv='" + CustProv + '\'' +
                ", CustPostal='" + CustPostal + '\'' +
                ", CustCountry='" + CustCountry + '\'' +
                ", CustHomePhone='" + CustHomePhone + '\'' +
                ", CustBusPhone='" + CustBusPhone + '\'' +
                ", CustEmail='" + CustEmail + '\'' +
                ", AgentId=" + AgentId +
                '}';
    }
}

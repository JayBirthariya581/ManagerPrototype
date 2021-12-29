package com.manage.app;

public class Service {
    String phone,date,vehicle_no,time,serviceStatus="onHold",serviceID;
    AddressHelper address;
    public Service(String phone, String date, String vehicle_no, AddressHelper address, String time, String serviceStatus, String serviceID) {
        this.phone = phone;
        this.date = date;
        this.vehicle_no = vehicle_no;
        this.address = address;
        this.time = time;
        this.serviceStatus = serviceStatus;
        this.serviceID = serviceID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public AddressHelper getAddress() {
        return address;
    }

    public void setAddress(AddressHelper address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }
}

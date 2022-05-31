package com.example.scannerqrapplication;

public class StateHolder {

    public static final StateHolder INSTANCE = new StateHolder();

    private String employeeCode;
    private String stationCode;

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
}

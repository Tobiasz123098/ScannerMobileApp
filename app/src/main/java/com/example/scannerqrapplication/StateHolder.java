package com.example.scannerqrapplication;

public class StateHolder {

    public static final StateHolder INSTANCE = new StateHolder();

    private String employeeCode;
    private String stationCode;
    private String payloadCode;
    private String pageNumber;

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

    public String getPayloadCode() {
        return payloadCode;
    }

    public void setPayloadCode(String payloadCode) {
        this.payloadCode = payloadCode;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}

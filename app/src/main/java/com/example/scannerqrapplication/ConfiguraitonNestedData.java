package com.example.scannerqrapplication;

public class ConfiguraitonNestedData {

    private String parentName;
    private String valueName;

    public ConfiguraitonNestedData(String parentName, String valueName) {
        this.parentName = parentName;
        this.valueName = valueName;
    }

    public String getParentName() {
        return parentName;
    }

    public String getValueName() {
        return valueName;
    }
}

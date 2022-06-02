package com.example.scannerqrapplication;

public class OrdersData {
    private String operationName;
    private String productName;
    private String productionOrderNumber;
    private String completionTerm;

    public OrdersData(String operationName, String productName, String productionOrderNumber, String completionTerm) {
        this.operationName = operationName;
        this.productName = productName;
        this.productionOrderNumber = productionOrderNumber;
        this.completionTerm = completionTerm;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductionOrderNumber() {
        return productionOrderNumber;
    }

    public String getCompletionTerm() {
        return completionTerm;
    }
}

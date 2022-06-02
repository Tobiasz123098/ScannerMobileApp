package com.example.scannerqrapplication;

public class OrdersData {
    private String operationName;
    private String productName;
    private String productionOrderNumber;
    private String completionTerm;
    private String remainingCount;
    private String totalCount;
    private String status;

    public OrdersData(String operationName, String productName, String productionOrderNumber, String completionTerm, String remainingCount, String totalCount, String status) {
        this.operationName = operationName;
        this.productName = productName;
        this.productionOrderNumber = productionOrderNumber;
        this.completionTerm = completionTerm;
        this.remainingCount = remainingCount;
        this.totalCount = totalCount;
        this.status = status;
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

    public String getRemainingCount() {
        return remainingCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public String getStatus() {
        return status;
    }
}
